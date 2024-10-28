package com.plcoding.cryptotracker.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.util.onError
import com.plcoding.cryptotracker.core.domain.util.onSuccess
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.DataPoint
import com.plcoding.cryptotracker.crypto.presentation.model.CoinUI
import com.plcoding.cryptotracker.crypto.presentation.model.toCoinUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

//presentation must not access data layer...thus we're using something from domain
class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _coinListState = MutableStateFlow(CoinListState())
    val coinListState = _coinListState
        .onStart { getCoins() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinListState()
        )

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()


    //getCoins
    private fun getCoins(){
        viewModelScope.launch {
            _coinListState.update {
                it.copy(
                    loading = true
                )
            }
            coinDataSource.getCoins()
                .onSuccess {coinsList->
                    println("The list size is ${coinsList.size}")
                    _coinListState.update {
                        it.copy(
                            loading = false,
                            coins = coinsList.map {coin->
                                coin.toCoinUI()
                            }
                        )
                    }
                }.onError {error->
                    _coinListState.update {
                        it.copy(
                            loading = false
                        )
                    }
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }

    //Actions
    fun onAction(action: CoinListAction){
        when(action){
            is CoinListAction.OnCoinClick -> {
                selectCoin(action.coinUI)
            }
            CoinListAction.OnRefresh -> TODO()
        }
    }

    private fun selectCoin(coinUI: CoinUI){
        _coinListState.update {
            it.copy(
                selectedCoin =  coinUI
            )
        }

        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinId = coinUI.id ,
                start = ZonedDateTime.now().minusDays(5) ,
                end = ZonedDateTime.now()
            ).onSuccess {history->
                val dataPoints =history
                    .sortedBy { it.time }
                    .map {
                        DataPoint(
                            x = it.time.hour.toFloat(),
                            y=  it.priceUsd.toFloat(),
                            xLabel = DateTimeFormatter.ofPattern("ha\nM/d")
                                .format(it.time)
                        )
                    }
                _coinListState.update {
                    it.copy(
                        selectedCoin = it.selectedCoin?.copy(coinPriceHistory = dataPoints)
                    )
                }
            }.onError { error->
                _events.send(CoinListEvent.Error(error))
            }
        }
    }



}