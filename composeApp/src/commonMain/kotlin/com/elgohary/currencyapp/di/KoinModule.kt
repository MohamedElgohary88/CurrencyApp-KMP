package com.elgohary.currencyapp.di

import com.elgohary.currencyapp.data.local.PreferencesRepositoryImpl
import com.elgohary.currencyapp.data.remote.CurrencyApiServiceImpl
import com.elgohary.currencyapp.data.remote.api.CurrencyApiService
import com.elgohary.currencyapp.data.repository.MongoRepositoryImpl
import com.elgohary.currencyapp.domain.repository.MongoRepository
import com.elgohary.currencyapp.domain.repository.PreferencesRepository
import com.elgohary.currencyapp.presentation.viewmodels.HomeViewModel
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single { Settings() }
    single<PreferencesRepository> { PreferencesRepositoryImpl(settings = get()) }
    single<CurrencyApiService> { CurrencyApiServiceImpl(preferences = get()) }
    single<MongoRepository> { MongoRepositoryImpl() }

    factory {
        HomeViewModel(
            preferencesRepository = get(),
            currencyApiService = get(),
            mongoRepository = get()
        )
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}