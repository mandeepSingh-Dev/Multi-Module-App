package com.app.core_ui.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.core_ui.R


val HAFFER_TRIAL_SEMI_THIN = FontFamily(
    Font(R.font.haffer_trial_thin,FontWeight.Normal),
)
val HAFFER_TRIAL_SEMI_LIGHT = FontFamily(
    Font(R.font.haffer_trial_light,FontWeight.Normal),
)

val HAFFER_TRIAL_REGULAR = FontFamily(
    Font(R.font.haffer_trial_regular,FontWeight.Normal),
)

val HAFFER_TRIAL_BLACK = FontFamily(
    Font(R.font.haffer_trial_black,FontWeight.Normal),
)

val HAFFER_TRIAL_BOLD = FontFamily(
    Font(R.font.haffer_trial_bold,FontWeight.Normal),
)

val HAFFER_TRIAL_SEMI_BOLD = FontFamily(
    Font(R.font.haffer_trial_semibold,FontWeight.Normal),
)

val HAFFER_TRIAL_MEDIUM = FontFamily(
    Font(R.font.haffer_trial_medium,FontWeight.Normal),
)



// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
//        fontFamily = IBM_PLEX_SANS_REGULAR,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = Color.Black,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)