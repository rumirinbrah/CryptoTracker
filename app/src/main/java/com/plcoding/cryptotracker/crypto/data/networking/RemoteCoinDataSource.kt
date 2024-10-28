package com.plcoding.cryptotracker.crypto.data.networking

import com.plcoding.cryptotracker.core.data.networking.constructUrl
import com.plcoding.cryptotracker.core.data.networking.safeCall
import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.core.domain.util.Result
import com.plcoding.cryptotracker.core.domain.util.map
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinsPriceResponseDto
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinsResponseDto
import com.plcoding.cryptotracker.crypto.data.toCoin
import com.plcoding.cryptotracker.crypto.data.toCoinPrice
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class RemoteCoinDataSource (
    private val httpClient: HttpClient
): CoinDataSource {

    override suspend fun getCoins(): Result<List<Coin> , NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(constructUrl("/assets"))
        }.map {response->
            response.data.map {
                it.toCoin()
            } //data is the List<CoinDto>
        }
    }

    override suspend fun getCoinHistory(
        coinId: String ,
        start: ZonedDateTime ,
        end: ZonedDateTime
    ): Result<List<CoinPrice> , NetworkError> {

        val startMillis = start.withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant().toEpochMilli()
        val endMillis = end.withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant().toEpochMilli()

        return safeCall <CoinsPriceResponseDto>{
            httpClient.get(constructUrl("/assets/$coinId/history")){
                parameter("interval","h6")
                parameter("start",startMillis)
                parameter("end",endMillis)
            }//1729468800000  1729049757001
            // start - 1729049756997  1729080000000
        }.map {response->
            response.data.map {
                it.toCoinPrice()
            }
        }
    }
}