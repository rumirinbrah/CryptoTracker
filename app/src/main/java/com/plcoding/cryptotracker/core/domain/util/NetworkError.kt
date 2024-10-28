package com.plcoding.cryptotracker.core.domain.util

//req errror , user not connected , api down
//empty interfaces r called marker interfaces
enum class NetworkError :Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION_ERROR,
    UNKNOWN
}