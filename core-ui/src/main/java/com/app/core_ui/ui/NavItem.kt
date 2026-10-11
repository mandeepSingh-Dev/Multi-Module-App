@file:OptIn(ExperimentalFoundationApi::class)

package com.app.core_ui.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PremiumNavItem(
    val title: String,
    val icon: ImageVector
)


@Composable
@Preview
fun MainScreen() {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F7F3))
    ) {

        // Your screen content here


        PremiumBottomNavigation(
            selectedIndex = selectedTab,
            onItemSelected = { index ->
                selectedTab = index
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private val GoldStart = Color(0xFFFFD45A)
private val GoldMiddle = Color(0xFFF5B51B)
private val GoldEnd = Color(0xFFE89A00)

private val NavDarkStart = Color(0xFF333333)
private val NavDarkEnd = Color(0xFF858585)

@Composable
fun PremiumBottomNavigation(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val items = remember {
        listOf(
            PremiumNavItem(
                title = "Home",
                icon = Icons.Outlined.Home
            ),
            PremiumNavItem(
                title = "Wallet",
                icon = Icons.Outlined.Home
            ),
            PremiumNavItem(
                title = "Chat",
                icon = Icons.Outlined.Home
            ),
            PremiumNavItem(
                title = "Profile",
                icon = Icons.Outlined.Home
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 100.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = Color.Black.copy(alpha = 0.30f),
                    spotColor = Color.Black.copy(alpha = 0.40f)
                )
                .clip(RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NavDarkStart,
                            Color(0xFF555555),
                            NavDarkEnd
                        )
                    )
                )
                .padding(horizontal = 7.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEachIndexed { index, item ->

                PremiumNavItemView(
                    item = item,
                    selected = selectedIndex == index,
                    onClick = {
                        onItemSelected(index)
                    }
                )
            }
        }
    }
}

@Composable
private fun PremiumNavItemView(
    item: PremiumNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {

    val itemWidth by animateDpAsState(
        targetValue = if (selected) 108.dp else 44.dp,
        animationSpec = spring(
            dampingRatio = 0.72f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "itemWidth"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 0.90f,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconScale"
    )

    val iconColor by animateColorAsState(
        targetValue = if (selected) {
            Color(0xFF4B3500)
        } else {
            Color.White.copy(alpha = 0.48f)
        },
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconColor"
    )

    val labelColor by animateColorAsState(
        targetValue = if (selected) {
            Color(0xFF1F1600)
        } else {
            Color.Transparent
        },
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium
        ),
        label = "labelColor"
    )

    Box(
        modifier = Modifier
            .width(itemWidth)
            .height(48.dp)
            .clip(RoundedCornerShape(28.dp))
            .then(
                if (selected) {
                    Modifier
                        // Premium gold glow/shadow
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = Color(0xFFFFB300).copy(alpha = 0.45f),
                            spotColor = Color(0xFFFFC107).copy(alpha = 0.65f)
                        )
                        // Premium gold gradient
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFD966),
                                    Color(0xFFFFBE26),
                                    Color(0xFFE99A00)
                                )
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = iconColor,
                modifier = Modifier
                    .size(23.dp)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    }
            )

            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(
                    animationSpec = spring(
                        dampingRatio = 0.8f
                    )
                ) + slideInHorizontally(
                    initialOffsetX = { it / 3 },
                    animationSpec = spring(
                        dampingRatio = 0.8f,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
                exit = fadeOut()
            ) {

                Row {

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = item.title,
                        color = labelColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}