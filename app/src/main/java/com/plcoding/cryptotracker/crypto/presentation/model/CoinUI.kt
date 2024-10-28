package com.plcoding.cryptotracker.crypto.presentation.model

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.core.presentation.util.getDrawableIdForCoin
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.DataPoint

data class CoinUI(
    val id : String,
    val rank : Int,
    val name : String,
    val symbol : String,
    val marketCapUsd : DisplayableNumber,
    val priceUsd : DisplayableNumber,
    val changePercent24Hr : DisplayableNumber,
    @DrawableRes val icon : Int,
    val coinPriceHistory : List<DataPoint> = emptyList()
)

data class DisplayableNumber(
    val value : Double,
    val formattedValue : String
)

fun Coin.toCoinUI() : CoinUI{
    return CoinUI(
        id = id,
        name = name,
        rank = rank ,
        symbol = symbol,
        marketCapUsd = marketCapUsd.toDisplayableNumber(),
        priceUsd = priceUsd.toDisplayableNumber(),
        changePercent24Hr = changePercent24Hr.toDisplayableNumber(),
        icon = getDrawableIdForCoin(symbol)
    )
}

fun Double.toDisplayableNumber() : DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance().apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    return DisplayableNumber(
        value = this,
        formattedValue = formatter.format(this)
    )
}




