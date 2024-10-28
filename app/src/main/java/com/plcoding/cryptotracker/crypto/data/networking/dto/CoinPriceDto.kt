package com.plcoding.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceDto(
    val time : Long,
    val priceUsd : Double
)
