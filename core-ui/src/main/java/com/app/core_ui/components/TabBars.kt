package com.app.core_ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.theme_color_11
import com.zodiaq.ui.theme.Shape_20


@Composable
private fun TabBars(

) {

    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf(
        "All Gifts",
        "Tier 1",
        "Tier 2",
        "Tier 3",
        "Tier 4",
        "Tier 5",
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {

        list.forEachIndexed { index, item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clip(Shape_20)
                    .clickableDebAnim {
                        selectedIndex = index
                    }
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    item, color =
                        if (selectedIndex == index) theme_color_11 else text_grey_1,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 13.sp
                )
                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .then(
                            if (selectedIndex == index) {
                                Modifier.fillMaxWidth()
                            } else {
                                Modifier
                            }

                        )
                        .height(2.dp)
                        .background(color = theme_color_11, Shape_20)
                )

            }
        }
    }
}

