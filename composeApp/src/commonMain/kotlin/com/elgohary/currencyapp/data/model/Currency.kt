package com.elgohary.currencyapp.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

open class Currency : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var code: String = ""
    var value: Double = 0.0
}

@Serializable
data class CurrencyDto(
    val code: String,
    val value: Double
)

fun CurrencyDto.toRealmObject(): Currency {
    return Currency().apply {
        code = this@toRealmObject.code
        value = this@toRealmObject.value
    }
}

fun Currency.toDto(): CurrencyDto {
    return CurrencyDto(
        code = this.code,
        value = this.value
    )
}
