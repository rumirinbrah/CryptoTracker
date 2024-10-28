package com.plcoding.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.crypto.presentation.model.CoinUI
import com.plcoding.cryptotracker.crypto.presentation.model.DisplayableNumber
import com.plcoding.cryptotracker.crypto.presentation.model.toCoinUI
import com.plcoding.cryptotracker.crypto.presentation.util.VerticalSpace
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListItem(modifier: Modifier = Modifier,coinUI: CoinUI,onClick :()->Unit) {

    Row(
        modifier
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(85.dp),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = ImageVector.vectorResource(coinUI.icon) ,
            contentDescription = coinUI.name
        )
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = coinUI.symbol ,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = coinUI.name ,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$ ${coinUI.priceUsd.formattedValue}" ,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            VerticalSpace(8.dp)
            PriceChange(
                change = coinUI.changePercent24Hr
            )

        }



    }

}

@PreviewLightDark()
@Composable
private fun CoinListItemPreview(

) {
    CryptoTrackerTheme {
        CoinListItem(
            coinUI = previewCoin,
            onClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

internal val previewCoin = Coin(
    id = "bitcoin",
    name = "Bitcoin",
    rank = 1 ,
    symbol = "BTC",
    marketCapUsd = 123781274817.67,
    priceUsd = 63345.454,
    changePercent24Hr = -4.2
).toCoinUI()



