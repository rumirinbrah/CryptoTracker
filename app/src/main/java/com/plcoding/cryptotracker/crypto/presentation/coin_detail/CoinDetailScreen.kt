package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.components.InfoCard
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListState
import com.plcoding.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.plcoding.cryptotracker.crypto.presentation.model.toDisplayableNumber
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    modifier: Modifier = Modifier ,
    coinListState: CoinListState ,
) {


    Box(
        modifier = modifier
            .fillMaxSize() ,
        contentAlignment = Alignment.Center
    ) {

        if (coinListState.loading) {
            CircularProgressIndicator()
        } else if (coinListState.selectedCoin != null) {
            val coin = coinListState.selectedCoin
            Column(
                modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp) ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(coin.icon) ,
                    contentDescription = coin.name ,
                    modifier = Modifier.size(100.dp) ,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = coin.name ,
                    fontSize = 40.sp ,
                    fontWeight = FontWeight.Black ,
                    textAlign = TextAlign.Center ,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = coin.symbol ,
                    fontSize = 20.sp ,
                    textAlign = TextAlign.Center ,
                    color = MaterialTheme.colorScheme.onBackground
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth() ,
                    horizontalArrangement = Arrangement.Center

                ) {
                    InfoCard(
                        title = "Market Cap" ,
                        formattedValue = coin.marketCapUsd.formattedValue ,
                        icon = R.drawable.stock
                    )
                    InfoCard(
                        title = "Price" ,
                        formattedValue = coin.priceUsd.formattedValue ,
                        icon = R.drawable.dollar
                    )
                    val absoluteChangeFormatted =
                        (coin.priceUsd.value * (coin.changePercent24Hr.value / 100)).toDisplayableNumber()
                    val isPositive = coin.changePercent24Hr.value > 0.0
                    val contentColor = if(isPositive){Color.Green} else {MaterialTheme.colorScheme.error}
                    InfoCard(
                        contentColor = contentColor,
                        title = "Change last 24hr" ,
                        formattedValue = absoluteChangeFormatted.formattedValue ,
                        icon = if(isPositive) R.drawable.trending else R.drawable.trending_down
                    )
                }

                AnimatedVisibility(visible = coin.coinPriceHistory.isNotEmpty()) {

                    var selectedDataPoint by remember{ mutableStateOf<DataPoint?>(null) }
                    var labelWidth by remember{ mutableFloatStateOf(0f) }
                    var totalChartWidth by remember{ mutableFloatStateOf(0f) }
                    val amountOfVisiblePoints = if(labelWidth > 0){
                        ((totalChartWidth - 2.5*labelWidth)/labelWidth).toInt()
                    }else{
                        0
                    }
                    val startIndex = coin.coinPriceHistory.lastIndex - amountOfVisiblePoints
                        .coerceAtLeast(0)


                    LineChart(
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(16/9f)
                            .onSizeChanged { totalChartWidth = it.width.toFloat() },
                        dataPoints = coin.coinPriceHistory ,
                        chartStyle = ChartStyle(
                            chartLineColor =  MaterialTheme.colorScheme.primary,
                            unselectedColor =  MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                            selectedColor =  MaterialTheme.colorScheme.primary,
                            helperLineThicknessPx =  5f,
                            axisLinesThicknessPx =  5f,
                            labelFontSize =  14.sp,
                            minYLabelSpacing =  25.dp,
                            verticalPadding =  8.dp,
                            horizontalPadding = 8.dp ,
                            minXLabelSpacing = 8.dp
                        ) ,
                        visibleDataPointsIndices =  startIndex..coin.coinPriceHistory.lastIndex,
                        unit = "$" ,
                        onSelectDataPoint = {
                            selectedDataPoint = it
                        },
                        selectedDataPoint = selectedDataPoint,
                        showHelperLine = true,
                        onXLabelWidthChange = {
                            labelWidth = it
                        }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPrev() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            coinListState = CoinListState(selectedCoin = previewCoin) ,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}



