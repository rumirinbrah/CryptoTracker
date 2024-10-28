package com.plcoding.cryptotracker.crypto.domain

import com.plcoding.cryptotracker.crypto.presentation.model.CoinUI

data class Coin(
    val id : String,
    val rank : Int,
    val name : String,
    val symbol : String,
    val marketCapUsd : Double,
    val priceUsd : Double,
    val changePercent24Hr : Double
)



