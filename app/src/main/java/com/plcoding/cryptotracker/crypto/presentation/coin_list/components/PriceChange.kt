package com.plcoding.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.presentation.model.DisplayableNumber
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun PriceChange(
    change: DisplayableNumber
) {
    val contentColor = if (change.value < 0.0) {
        MaterialTheme.colorScheme.onErrorContainer
    } else Color.Green

    val backgroundColor = if (change.value < 0.0) {
        MaterialTheme.colorScheme.errorContainer
    } else Color(0xFF4EA151)


    Row (
        Modifier.clip(RoundedCornerShape(100))
            .background(backgroundColor)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = if (change.value < 0.0) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp ,
            contentDescription = change.formattedValue,
            modifier = Modifier.size(20.dp),
            tint = contentColor
        )
        Text(
            text = "${change.formattedValue} %" ,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )


    }
}


@PreviewLightDark
@Composable
private fun PriceChangePrev(

) {
    CryptoTrackerTheme {
        PriceChange(
            change = DisplayableNumber(
                value = -2.5 ,
                formattedValue = "2.5 %"
            )
        )
    }
}