package com.elgohary.currencyapp.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.elgohary.currencyapp.data.model.Currency
import com.elgohary.currencyapp.data.model.RateStatus
import com.elgohary.currencyapp.data.remote.api.CurrencyApiService
import com.elgohary.currencyapp.domain.repository.MongoRepository
import com.elgohary.currencyapp.domain.repository.PreferencesRepository
import com.elgohary.currencyapp.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates : HomeUiEvent()
    data object SwitchCurrencies : HomeUiEvent()
}

class HomeViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val currencyApiService: CurrencyApiService,
    private val mongoRepository: MongoRepository
) : ScreenModel {
    private var _rateStatus: MutableState<RateStatus> = mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    private var _sourceCurrency: MutableState<RequestState<Currency>> =
        mutableStateOf(RequestState.Idle)
    val sourceCurrency: State<RequestState<Currency>> = _sourceCurrency

    private var _targetCurrency: MutableState<RequestState<Currency>> =
        mutableStateOf(RequestState.Idle)
    val targetCurrency: State<RequestState<Currency>> = _targetCurrency

    private var _allCurrencies = mutableStateListOf<Currency>()
    val allCurrencies: List<Currency> = _allCurrencies

    init {
        screenModelScope.launch {
            fetchNewRates()
            readSourceCurrency()
            readTargetCurrency()
        }
    }

    fun sendEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }

            is HomeUiEvent.SwitchCurrencies -> {
                switchCurrencies()
            }
        }
    }

    private fun switchCurrencies() {
        val source = _sourceCurrency.value
        val target = _targetCurrency.value
        _sourceCurrency.value = target
        _targetCurrency.value = source
    }

    private suspend fun fetchNewRates() {
        try {
            val localCache = mongoRepository.readCurrencyData().first()
            if (localCache.isSuccess()) {
                if (localCache.getSuccessData().isNotEmpty()) {
                    println("$TAG: Database is full")
                    _allCurrencies.clear()
                    _allCurrencies.addAll(localCache.getSuccessData())
                    if (!preferencesRepository.isDataFresh(
                            Clock.System.now().toEpochMilliseconds()
                        )
                    ) {
                        println("HomeViewModel: DATA NOT FRESH")
                        cacheTheData()
                    } else {
                        println("HomeViewModel: DATA IS FRESH")
                    }
                } else {
                    println("HomeViewModel: DATABASE NEEDS DATA")
                    cacheTheData()
                }
            } else if (localCache.isError()) {
                println("HomeViewModel: ERROR READING LOCAL DATABASE ${localCache.getErrorData()}")
            }

            getRateStatus()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun cacheTheData() {
        val fetchedData = currencyApiService.getLatestExchangeRates()
        if (fetchedData.isSuccess()) {
            mongoRepository.cleanUp()
            fetchedData.getSuccessData().forEach {
                println("HomeViewModel: ADDING ${it.code}")
                mongoRepository.insertCurrencyData(it)
            }
            println("HomeViewModel: UPDATING _allCurrencies")
            _allCurrencies.clear()
            _allCurrencies.addAll(fetchedData.getSuccessData())
        } else if (fetchedData.isError()) {
            println("HomeViewModel: FETCHING FAILED ${fetchedData.getErrorData()}")
        }
    }

    private suspend fun getRateStatus() {
        _rateStatus.value = if (
            preferencesRepository.isDataFresh(
                currentTimestamp = Clock.System.now().toEpochMilliseconds()
            )
        ) RateStatus.Fresh
        else RateStatus.Stale
    }

    private fun readSourceCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            preferencesRepository.readSourceCurrencyCode().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name }
                if (selectedCurrency != null) {
                    _sourceCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _sourceCurrency.value =
                        RequestState.Error(message = "Couldn't find the selected currency.")
                }
            }
        }
    }

    private fun readTargetCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            preferencesRepository.readTargetCurrencyCode().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name }
                if (selectedCurrency != null) {
                    _targetCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _targetCurrency.value =
                        RequestState.Error(message = "Couldn't find the selected currency.")
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}