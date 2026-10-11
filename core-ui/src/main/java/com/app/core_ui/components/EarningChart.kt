package com.app.core_ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_4
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent


@Composable
fun EarningChart(
    values: List<Int>,
    valuesLabels: List<String>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    val maxYAxisValue by remember(values) {
        derivedStateOf {
            val maxValue = values.maxOrNull()
            maxValue?.plus(maxValue*0.5)
        }
    }


    val columnProvider = remember {
        ColumnCartesianLayer.ColumnProvider.series(
             LineComponent(
                fill = Fill(brush = Brush.verticalGradient(listOf(theme_color_4, theme_color_11,Color.White))),
                 thickness = 12.dp
            ),
        )
    }

    LaunchedEffect(values) {
        modelProducer.runTransaction {
            columnModel {
                series(values)
            }
        }
    }

    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(columnProvider),
            startAxis = VerticalAxis.rememberStart(
                label = rememberTextComponent(
                     style = TextStyle(color = grey_4 )
                ),
                line = rememberLineComponent(
                     fill = Fill(grey_4),
                ),
                tick = rememberLineComponent(
                    fill = Fill(grey_4)
                ),

            ),

            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = HorizontalAxis.ItemPlacer.aligned(
                    spacing = {1}
                ),

                valueFormatter = { _, value, _ ->
                    valuesLabels.getOrElse(value.toInt()) {
                        value.toInt().toString()
                    }
                }
            )
        ),
        modelProducer = modelProducer
    )
}