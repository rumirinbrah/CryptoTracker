package com.plcoding.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import com.plcoding.cryptotracker.core.presentation.util.toMessage
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Navigation(
    modifier: Modifier = Modifier ,
    coinListViewModel: CoinListViewModel = koinViewModel() ,
) {

    val coinListState by coinListViewModel.coinListState.collectAsStateWithLifecycle()
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val context = LocalContext.current
    ObserveAsEvents(events = coinListViewModel.events) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                Toast.makeText(
                    context ,
                    event.message.toMessage() ,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    coinListState = coinListState ,
                    onAction = {action->
                        coinListViewModel.onAction(action)
                        when(action){
                            is CoinListAction.OnCoinClick->{
                                navigator.navigateTo(
                                    pane=ListDetailPaneScaffoldRole.Detail
                                )
                            }
                            else->{

                            }
                        }
                    }
                )
            }
        } ,
        detailPane = {
            AnimatedPane {
                CoinDetailScreen(
                    coinListState = coinListState
                )
            }
        },
        modifier = modifier
    )


}