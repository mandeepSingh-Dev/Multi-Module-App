package com.app.core_ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import com.app.core_ui.R
import com.zodiaq.ui.theme.Shape_15

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BlockedUserBottomHeader(
    isBlocked: Boolean,
    onUnblockClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean?
) {

    val headerShape = RoundedCornerShape(
        topStart = 26.dp,
        topEnd = 26.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    AnimatedVisibility(
        visible = isBlocked,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                // Ambient red glow
                .dropShadow(
                    shape = headerShape,
                    shadow = androidx.compose.ui.graphics.shadow.Shadow(
                        radius = 24.dp,
                        color = Color(0x26D62839),
                        offset = DpOffset(0.dp, (-5).dp)
                    )
                )
                // Deeper outer shadow
                .dropShadow(
                    shape = headerShape,
                    shadow = androidx.compose.ui.graphics.shadow.Shadow(
                        radius = 12.dp,
                        color = Color(0x18000000),
                        offset = DpOffset(0.dp, (-3).dp)
                    )
                )
                .clip(headerShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFFFFAFA),
                            Color(0xFFFFF4F4)
                        )
                    )
                )
                // Subtle gradient outline
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFDADA),
                            Color(0xFFFFFFFF),
                            Color(0xFFF3C6CB)
                        )
                    ),
                    shape = headerShape
                )
                // Inner top highlight
                .drawBehind {
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFFFD6D9),
                                Color(0xFFFFFFFF),
                                Color.Transparent
                            )
                        ),
                        start = Offset(0f, 1f),
                        end = Offset(size.width, 1f),
                        strokeWidth = 2.dp.toPx()
                    )
                },

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFCE7E7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(R.drawable.outline_block_24),
                            contentDescription = "Blocked user",
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                Color(
                                    0xFFD92D35
                                )
                            ),
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "You blocked this user",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Messages and calls are disabled.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .background(shape = Shape_15, color = Color(0xFFFCE7E7)),
                    ) {

                        Text(
                            text = "User blocked",
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 13.dp
                            ),
                            color = Color(0xFFC62828),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    AnimatedContent(isLoading ?: false) { isLoading ->
                        if(isLoading) {
                            CircularWavyProgressIndicator(modifier = Modifier.size(35.dp))
                        }else{
                            Button(
                                onClick = onUnblockClick,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFC62828),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Unblock",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
