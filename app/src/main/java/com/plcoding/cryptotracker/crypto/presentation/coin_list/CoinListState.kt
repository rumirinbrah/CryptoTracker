package com.plcoding.cryptotracker.crypto.presentation.coin_list

import androidx.compose.runtime.Immutable
import com.plcoding.cryptotracker.crypto.presentation.model.CoinUI

//dont compose unless the entire obj is changed
@Immutable
data class CoinListState(
    val loading : Boolean = false,
    val coins : List<CoinUI> = emptyList(),
    val selectedCoin : CoinUI? = null
)
