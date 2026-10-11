package com.app.core_ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView
import com.app.core_ui.components.HeaderView2


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_4

data class TabItem(
    val title: String,
    val count: Int
)

@Composable
fun PremiumAnimatedTabBar(
    modifier: Modifier,
    tabs: List<TabItem>,
    selectedTabIndex: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val tabWidth = screenWidth / tabs.size

    // Smooth indicator offset animation
    val animatedIndicatorOffset by animateDpAsState(
        targetValue = tabWidth * selectedTabIndex,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "TabIndicatorOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Tab Titles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = index == selectedTabIndex

                    // Smooth color transition for text
                    val animatedTextColor by animateColorAsState(
                        targetValue = if (isSelected) theme_color_11 else text_grey_3,
                        animationSpec = tween(durationMillis = 250),
                        label = "TextColorAnimation"
                    )

                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Remove default ripple for custom clean feel
                            ){
                                onTabSelected(index)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = tab.title,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = animatedTextColor
                            )
                            Spacer(modifier = Modifier.width(6.dp))

                            // Badge with count
                            BadgeCount(
                                count = tab.count,
                                isSelected = isSelected,
                                textColor = animatedTextColor
                            )
                        }
                    }
                }
            }

            // Indicator Container with Base Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                // Background subtle bottom line
                HorizontalDivider(
                    modifier = Modifier.fillMaxSize(),
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )

                // Animated Gold Indicator Line
                Box(
                    modifier = Modifier
                        .offset(x = animatedIndicatorOffset)
                        .width(tabWidth)
                        .fillMaxHeight()
                        .padding(horizontal = 24.dp) // Dynamic padding to keep pill look centered
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = theme_color_4
                        )
                        .clip(CircleShape)
                        .background(brush = brush2())
                )
            }
        }
    }
}

@Composable
private fun BadgeCount(
    count: Int,
    isSelected: Boolean,
    textColor: Color
) {
    val borderColor = if (isSelected) Color(0xFFD97706) else Color(0xFF9CA3AF)

    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFFFFFBEB) else Color(0xFFF3F4F6))
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}
