package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class ChartStyle(
    val chartLineColor: Color,
    val unselectedColor: Color,
    val selectedColor: Color,
    val helperLineThicknessPx: Float, //grid cells
    val axisLinesThicknessPx : Float, //XY axis
    val labelFontSize : TextUnit,
    val minYLabelSpacing : Dp ,//dist bw two Y - axisLine labels,
    val verticalPadding : Dp,
    val horizontalPadding : Dp,
    val minXLabelSpacing : Dp
)
