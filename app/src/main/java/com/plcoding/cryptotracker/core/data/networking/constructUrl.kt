package com.plcoding.cryptotracker.core.data.networking

import com.plcoding.cryptotracker.BuildConfig

fun constructUrl(url : String) : String{
    return when{
        url.contains(BuildConfig.BASE_URL)-> url // eg https://.......
        url.startsWith("/")-> BuildConfig.BASE_URL + url.drop(1) // eg /assets
        else-> BuildConfig.BASE_URL + url // eg assets
    }
}