package com.app.core_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.core_ui.R
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.theme_color_12

@Composable
fun ButtonsDialog(
    showDialog: Boolean = false,
    showLoader: Boolean = false,
    icon : Int =R.drawable.kaho_ji_logoimage1,
    title: String? = null,
    description: String? = null,
    descriptionColor: Color = Color.Unspecified,
    descriptionFontStyle: FontStyle? = null,
    descriptionFontFamily: FontFamily? = null,
    showDescriptionLoader: Boolean = false,
    onDismiss: () -> Unit,
    showBothButtons: Boolean = true,
    positiveBtnText: String? = null,
    negativeBtnText: String? = null,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit = {},
) {

    if (!showDialog) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 26.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ------------------------------------------------
            // Top Icon
            // ------------------------------------------------

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = theme_color_12.copy(alpha = 0.10f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ------------------------------------------------
            // Title
            // ------------------------------------------------

            Text(
                text = title ?: "",
                fontFamily = HAFFER_TRIAL_BOLD,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )

            // ------------------------------------------------
            // Description
            // ------------------------------------------------

            if (showDescriptionLoader) {

                Spacer(modifier = Modifier.height(18.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp,
                    color = theme_color_12
                )

            } else if (!description.isNullOrBlank()) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = if (descriptionColor != Color.Unspecified) {
                        descriptionColor
                    } else {
                        Color(0xFF777777)
                    },
                    fontFamily = descriptionFontFamily,
                    fontStyle = descriptionFontStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ------------------------------------------------
            // Buttons / Loader
            // ------------------------------------------------

            if (showLoader) {

                CircularProgressIndicator(
                    modifier = Modifier.size(34.dp),
                    strokeWidth = 3.dp,
                    color = theme_color_12
                )

            } else {

                if (showBothButtons) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Negative button
                        GradientButton(
                            text = negativeBtnText ?: "No",
                            onClick = {
                                onDismiss()
                                onNegativeClick()
                            },
                            modifier = Modifier.weight(1f)
                        )

                        // Positive button
                        GradientButton(
                            text = positiveBtnText ?: "Yes",
                            onClick = {
//                                onDismiss()
                                onPositiveClick()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                } else {

                    GradientButton(
                        text = positiveBtnText ?: "OK",
                        onClick = {
//                            onDismiss()
                            onPositiveClick()
                                  },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}