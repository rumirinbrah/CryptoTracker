package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import android.widget.Toast
import androidx.collection.floatListOf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints: List<DataPoint> ,
    chartStyle: ChartStyle ,
    visibleDataPointsIndices: IntRange ,
    unit: String ,
    modifier: Modifier = Modifier ,
    selectedDataPoint: DataPoint? = null ,
    onSelectDataPoint: (DataPoint) -> Unit ,
    onXLabelWidthChange: (Float) -> Unit = {} ,
    showHelperLine: Boolean = true
) {

    val textStyle = LocalTextStyle.current.copy(
        fontSize = chartStyle.labelFontSize ,
        textAlign = TextAlign.Center
    )

    val visibleDataPoints = remember(dataPoints , visibleDataPointsIndices) {
        dataPoints.slice(visibleDataPointsIndices)
    }

    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }
    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }

    val textMeasurer = rememberTextMeasurer()

    var xLabelWidth by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }

    val selectedDataPointIndex = remember(selectedDataPoint) {
        dataPoints.indexOf(selectedDataPoint)
    }


    var drawPoints by remember { mutableStateOf(listOf<DataPoint>()) }

    var isShowingDataPoints by remember { mutableStateOf(selectedDataPoint != null) }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawPoints , xLabelWidth) {
                detectHorizontalDragGestures { change , _ ->
                    val newSelectedPoint = getSelectedDataPoint(
                        touchOffsetX = change.position.x ,
                        triggerWidth = xLabelWidth ,
                        drawPoints = drawPoints
                    )
                    isShowingDataPoints =
                        (newSelectedPoint + visibleDataPointsIndices.first) in visibleDataPointsIndices

                    if (isShowingDataPoints) {
                        onSelectDataPoint(dataPoints[newSelectedPoint])
                    }
                }
            }
    ) {
        //paddings
        val minYLabelSpacingPx = chartStyle.minYLabelSpacing.toPx()
        val minXLabelSpacingPx = chartStyle.minXLabelSpacing.toPx()
        val verticalPaddingPx = chartStyle.verticalPadding.toPx()
        val horizontalPaddingPx = chartStyle.horizontalPadding.toPx()

        val xLabelTextLayoutResults = visibleDataPoints.map {
            textMeasurer.measure(
                text = it.xLabel ,
                style = textStyle
            )
        }
        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount }
            ?: 0 //num of texts arranged vertically
        val xLabelLineHeight = if(maxXLabelLineCount>0){maxXLabelHeight / maxXLabelLineCount} else {0}

        val viewPortHeightPx = size.height - (
                maxXLabelHeight + 2 * verticalPaddingPx +
                        xLabelLineHeight + minXLabelSpacingPx
                )     //xLabelLH for selected point text

        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
        val labelCount = (labelViewPortHeightPx / (xLabelLineHeight + minYLabelSpacingPx)).toInt()

        val incrementValue = (maxYValue - minYValue) / labelCount

        val yLabels = (0..labelCount).map {
            ValueLabel(
                value = maxYValue - (it * incrementValue) ,
                unit = unit
            )
        }
        /*
        Ex if you have value from 0 to 50 then yLabels = [50,40,30,20,10,0]
         */
        val yLabelTextLayoutResults = yLabels.map {
            textMeasurer.measure(text = it.formatted() , style = textStyle)
        }
        val maxYLabelWidth = yLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0

        //view port
        val viewPortTopY =
            verticalPaddingPx + xLabelLineHeight + 10f  //y will go into -ve now and x->0
        val viewPortRightX = size.width
        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPortLeftX = 2f * horizontalPaddingPx + maxYLabelWidth

        val viewPort = Rect(
            left = viewPortLeftX ,
            right = viewPortRightX ,
            top = viewPortTopY ,
            bottom = viewPortBottomY
        )

//        drawRect(
//            color = vpColor ,
//            topLeft = viewPort.topLeft ,
//            size = viewPort.size
//        )

        // X DRAW TEXT
        xLabelWidth = maxXLabelWidth + minXLabelSpacingPx
        xLabelTextLayoutResults.forEachIndexed { index , result ->
            val x = viewPortLeftX + minXLabelSpacingPx / 2f + xLabelWidth * index
            drawText(
                textLayoutResult = result ,
                topLeft = Offset(
                    x = x ,
                    y = viewPortBottomY + minXLabelSpacingPx
                ) ,
                color = if (index == selectedDataPointIndex) {
                    chartStyle.selectedColor
                } else {
                    chartStyle.unselectedColor
                }
            )
            //helper lines
            if (showHelperLine) {
                drawLine(
                    color = if (selectedDataPointIndex == index) {
                        chartStyle.selectedColor
                    } else chartStyle.unselectedColor ,
                    start = Offset(
                        x = x + result.size.width / 2 ,
                        y = viewPortTopY
                    ) ,
                    end = Offset(
                        x = x + result.size.width / 2 ,
                        y = viewPortBottomY
                    ) ,
                    strokeWidth = chartStyle.helperLineThicknessPx
                )

            }
            //Selected point label
            if (selectedDataPointIndex == index) {
                val valueLabel = ValueLabel(
                    value = visibleDataPoints[index].y ,
                    unit = unit
                )
                val valueResult = textMeasurer.measure(
                    text = valueLabel.formatted() ,
                    style = textStyle.copy(
                        color = chartStyle.selectedColor
                    ) ,
                    maxLines = 1
                )
                val positionX = if (selectedDataPointIndex == visibleDataPointsIndices.last) {
                    x - valueResult.size.width
                } else {
                    x - valueResult.size.width / 2f
                } + result.size.width / 2f
                val isTextInVisibleRange =
                    (size.width - positionX).roundToInt() in 0..size.width.roundToInt()
                if (isTextInVisibleRange) {
                    drawText(
                        textLayoutResult = valueResult ,
                        topLeft = Offset(
                            x = positionX ,
                            y = viewPortTopY - valueResult.size.height - 10f
                        )
                    )
                }
            }
        }


        val heightRequiredByYLabels = xLabelLineHeight * (labelCount + 1)
        val remainingHeightForYLabels = labelViewPortHeightPx - heightRequiredByYLabels
        val spaceBetweenLabels =
            remainingHeightForYLabels / (labelCount) //just read it, it'll make some sense...a lil


        //Y DRAW TEXT
        yLabelTextLayoutResults.forEachIndexed { index , result ->
            val y =
                viewPortTopY + index * (xLabelLineHeight + spaceBetweenLabels) - xLabelLineHeight / 2f

            val x = horizontalPaddingPx + maxYLabelWidth - result.size.width.toFloat()
            println(x)
            drawText(
                textLayoutResult = result ,
                topLeft = Offset(
                    x = x ,
                    y = y
                ) ,
                color = chartStyle.unselectedColor

            )
            if (showHelperLine) {
                drawLine(
                    strokeWidth = chartStyle.helperLineThicknessPx ,
                    color = chartStyle.unselectedColor ,
                    start = Offset(x = viewPortLeftX , y = y + result.size.height / 2) ,
                    end = Offset(x = viewPortRightX , y = y + result.size.height / 2)
                )
            }

            drawPoints = visibleDataPointsIndices.map {
                val xPoint = viewPortLeftX + (it - visibleDataPointsIndices.first) *
                        xLabelWidth + xLabelWidth / 2
                val ratio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)

                val yPoint = viewPortBottomY - (ratio * viewPortHeightPx)
                DataPoint(
                    x = xPoint ,
                    y = yPoint ,
                    xLabel = dataPoints[it].xLabel
                )
            }
            //define em here
            val controlPoints1 = mutableListOf<DataPoint>()
            val controlPoints2 = mutableListOf<DataPoint>()
            for (i in 1 until drawPoints.size) {
                val p0 = drawPoints[i - 1]
                val p1 = drawPoints[i]
                val xPoint = (p1.x + p0.x) / 2f
                val y1 = p0.y
                val y2 = p1.y
                controlPoints1.add(DataPoint(x = xPoint , y = y1 , xLabel = ""))
                controlPoints2.add(DataPoint(x = xPoint , y = y2 , xLabel = ""))
            }

            val linePath = Path().apply {
                if (drawPoints.isNotEmpty()) {
                    moveTo(x = drawPoints.first().x , y = drawPoints.first().y)

                    for (i in 1 until drawPoints.size) {
                        cubicTo(
                            x1 = controlPoints1[i - 1].x ,
                            y1 = controlPoints1[i - 1].y ,
                            x2 = controlPoints2[i - 1].x ,
                            y2 = controlPoints2[i - 1].y ,
                            x3 = drawPoints[i].x ,
                            y3 = drawPoints[i].y
                        )

                    }
                }
            }
            drawPath(
                path = linePath ,
                color = chartStyle.chartLineColor ,
                style = Stroke(width = 5f , cap = StrokeCap.Round)
            )


            if (isShowingDataPoints) {
                drawPoints.forEachIndexed { index , dataPoint ->
                    val offset = Offset(x = dataPoint.x , y = dataPoint.y)
                    drawCircle(
                        color = chartStyle.selectedColor ,
                        radius = 10f ,
                        center = offset
                    )
                    if (selectedDataPointIndex == index) {
                        drawCircle(
                            color = Color.White ,
                            radius = 15f ,
                            center = offset
                        )
                        drawCircle(
                            color = chartStyle.selectedColor ,
                            radius = 15f ,
                            center = offset ,
                            style = Stroke(3f)
                        )
                    }
                }


            }

        }
    }

}

private fun getSelectedDataPoint(
    touchOffsetX: Float ,
    triggerWidth: Float ,
    drawPoints: List<DataPoint>
): Int {
    //we calculate the threshold here
    val triggerRangeLeft = touchOffsetX - triggerWidth / 2f
    val triggerRangeRight = touchOffsetX + triggerWidth / 2f

    return drawPoints.indexOfFirst {
        it.x in triggerRangeLeft..triggerRangeRight
    }
}

@Preview
@Composable
private fun ToduFoduCanvasPrev() {
    CryptoTrackerTheme {

        val coinHistoryRandom = remember {
            (1..100).map {
                CoinPrice(
                    time = ZonedDateTime.now().plusHours(it.toLong()) ,
                    priceUsd = Random.nextFloat() * 1000.0
                )
            }
        }

        val style = ChartStyle(
            chartLineColor = Color.Black ,
            unselectedColor = Color(0xFF737070) ,
            selectedColor = Color.Black ,
            helperLineThicknessPx = 5f ,
            axisLinesThicknessPx = 5f ,
            labelFontSize = 14.sp ,
            minYLabelSpacing = 25.dp ,
            verticalPadding = 8.dp ,
            horizontalPadding = 8.dp ,
            minXLabelSpacing = 8.dp

        )

        val dataPoints = remember {
            coinHistoryRandom.map {
                DataPoint(
                    x = it.time.hour.toFloat() ,
                    y = it.priceUsd.toFloat() ,               //hour and a= append AM/PM
                    xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.time)
                )
            }
        }

        LineChart(
            dataPoints = dataPoints ,
            chartStyle = style ,
            visibleDataPointsIndices = 0..19 ,
            onSelectDataPoint = {} ,
            unit = "$" ,
            modifier = Modifier
                .width(1000.dp)
                .height(300.dp)
                .background(Color.White) ,
            selectedDataPoint = dataPoints[1]

        )


    }

}




