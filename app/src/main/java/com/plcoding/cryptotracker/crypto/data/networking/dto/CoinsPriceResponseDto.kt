package com.plcoding.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinsPriceResponseDto(
    val data : List<CoinPriceDto>
)
