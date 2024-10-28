package com.plcoding.cryptotracker.core.presentation.util

import com.plcoding.cryptotracker.core.domain.util.NetworkError

fun NetworkError.toMessage() : String{
    return when(this){
        NetworkError.REQUEST_TIMEOUT -> {
            "Request timed out"
        }
        NetworkError.TOO_MANY_REQUESTS -> {"Too many requests"}
        NetworkError.NO_INTERNET -> {"No internet connection found"}
        NetworkError.SERVER_ERROR -> {"Server is not responding"}
        NetworkError.SERIALIZATION_ERROR -> {"Serialization error occurred"}
        NetworkError.UNKNOWN -> {"An unknown error occurred"}
    }
}