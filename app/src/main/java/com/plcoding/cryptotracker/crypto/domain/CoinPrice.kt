package com.plcoding.cryptotracker.crypto.domain

import java.time.ZonedDateTime

data class CoinPrice(
    val time : ZonedDateTime,
    val priceUsd : Double
)
