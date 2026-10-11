package com.app.core_ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.grey_4
@Composable
    fun StoryScreenSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String?) -> Unit,
    hint: String?,
    borderColor: Color = grey_4,
    bgColor: Color = androidx.compose.ui.graphics.Color.Black.copy(0.4f),
    startIcon: Int? = null,
    startIconTint: Color = androidx.compose.ui.graphics.Color.Unspecified,
    startIconSize: Dp = 40.dp,
    endIcon: Int? = null,
    endIconSize: Dp = 40.dp,
    verticalPadding: Dp = 5.dp,
    horizontalPadding: Dp = 5.dp,
    onShowEmojis: () -> Unit,
    fontColor: Color = Color.Black
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(IntrinsicSize.Max)
                .clip(CircleShape)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .background(color = bgColor, shape = CircleShape)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        )
        {
            startIcon?.let {
                Image(
                    painterResource(startIcon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = startIconTint),
                    modifier = Modifier
                        .clickable {
                            onShowEmojis()
                        },
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = fontColor),
                decorationBox = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 2.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint ?: "",
                                color = grey_10
                            )
                        }
                        it()
                    }

                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            )
            endIcon?.let {
                Image(
                    painterResource(endIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(endIconSize)
                        .clickable {
                            onShowEmojis()
                        },
                )
            }
        }
    }
@Composable
    fun ChatTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String?) -> Unit,
    hint: String?,
    borderColor: Color = grey_4,
    bgColor: Color = androidx.compose.ui.graphics.Color.Black.copy(0.4f),
    startIcon: Int? = null,
    startIconTint: Color = androidx.compose.ui.graphics.Color.Unspecified,
    startIconSize: Dp = 25.dp,
    endIcon: Int? = null,
    endIconSize: Dp = 25.dp,
    verticalPadding: Dp = 5.dp,
    horizontalPadding: Dp = 10.dp,
    onShowEmojis: () -> Unit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(IntrinsicSize.Max)
                .clip(CircleShape)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .background(color = bgColor, shape = CircleShape)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        )
        {
            startIcon?.let {
                Image(
                    painterResource(startIcon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = startIconTint),
                    modifier = Modifier
                        .size(startIconSize)
                        .clickable {
                            onShowEmojis()
                        },
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = Color.Black),
                decorationBox = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 2.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint ?: "",
                                color = grey_10
                            )
                        }
                        it()
                    }

                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            )
            endIcon?.let {
                Image(
                    painterResource(endIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(endIconSize)
                        .clickable {
                            onShowEmojis()
                        },
                )
            }
        }
    }