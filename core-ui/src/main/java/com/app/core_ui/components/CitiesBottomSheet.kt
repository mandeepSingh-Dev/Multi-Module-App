package com.app.core_ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_22
import com.app.core_ui.ui.theme.theme_color_23
import com.app.core_ui.ui.theme.theme_color_24
import com.zodiaq.ui.theme.Shape_15


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionScreen(
    showShow: Boolean = false,
    onDismiss : () -> Unit,
    cities: List<String>,
    selectedCity: String?,
    onCitySelected: (String) -> Unit,
    isCitiesLoading: Boolean?
) {

    if (showShow) {

        val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            sheetState = sheet,
            dragHandle = null,
            onDismissRequest = onDismiss
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                GradientText(
                    text = "Select City",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Choose your preferred city",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,

                    )

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        cities,
                        key = { it }
                    ) { city ->

                        CitySelectionItem(
                            city = city,
                            selected = city == selectedCity,
                            onClick = {
                                onCitySelected(city)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CitySelectionItem(
    city: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.01f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cityScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            theme_color_23.copy(alpha = 0.8f)
        } else {
            theme_color_22
        },
        animationSpec = tween(250),
        label = "background"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            theme_color_1
        } else {
            theme_color_22
        },
        animationSpec = tween(250),
        label = "border"
    )
    val shadowColor by animateColorAsState(
        targetValue = if (selected) {
            theme_color_1.copy(alpha = 0.7f)
        } else {
            Color.Transparent
        },
        animationSpec = tween(250),
        label = "border"
    )
    val shapeDp by animateDpAsState(
        targetValue = if (selected) {
            22.dp
        } else {
            15.dp
        },
        animationSpec = tween(250),
        label = "border"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .dropShadowCustom(color = shadowColor, shape = Shape_15)
            .clip(RoundedCornerShape(shapeDp))
            .clickable(
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .background(backgroundColor, RoundedCornerShape(shapeDp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(shapeDp)),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 17.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = city,
                modifier = Modifier.weight(1f),
                fontFamily = if (selected) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_REGULAR,
                color = MaterialTheme.colorScheme.onSurface
            )

            AnimatedCityIndicator(
                selected = selected
            )
        }
    }
}

@Composable
private fun AnimatedCityIndicator(
    selected: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "indicatorScale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            theme_color_1
        } else {
            theme_color_24
        },
        animationSpec = tween(250),
        label = "indicatorBorder"
    )

    Box(
        modifier = Modifier
            .size(26.dp)
            .scale(scale)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = selected,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.10f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}