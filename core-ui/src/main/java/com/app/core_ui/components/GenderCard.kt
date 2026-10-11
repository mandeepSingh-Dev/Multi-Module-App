package com.app.core_ui.components
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.enums.GenderType
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brushWhite
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.theme_color_2
import com.app.core_ui.ui.theme.theme_color_5
import com.zodiaq.ui.theme.Shape_15

@Composable
fun GenderCard(
    modifier: Modifier,
    genderType: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit
)
{

    val data = when (genderType) {
        GenderType.MALE.type -> {
            Triple("Male", "I am male", R.drawable.maleimage26)
        }

        GenderType.FEMALE.type -> {
            Triple("Female", "I am female", R.drawable.femaleimage25)
        }

        else -> {
            Triple("Prefer not to say", "I prefer not to disclose", R.drawable.otherimage27)
        }
    }

    val radiusAnim by animateDpAsState(if (isSelected) 20.dp else 5.dp)
    val borderAnim by animateDpAsState(if (isSelected) 1.dp else 0.dp)
    val shadowAlphaAnim by animateFloatAsState(if (isSelected) 0.7f else 0.2f)
    val bgColor by animateColorAsState(if (isSelected) Color.Black else Color.White)
    val borderColor by animateColorAsState(if (isSelected) theme_color_2 else Color.Transparent)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()

            .dropShadow(
                shadow = Shadow(
                    radius = radiusAnim,
                    color = theme_color_2.copy(shadowAlphaAnim)
                ),
                shape = Shape_15
            )
            .clip(Shape_15)
            .clickable {
                onClick()
            }
            .border(width = borderAnim, color = borderColor, shape = Shape_15)
            .background(color = bgColor, shape = Shape_15)
            .padding(15.dp)) {

        Image(
            painterResource(data.third),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            Text(
                data.first,
                color = if (isSelected) theme_color_5 else Color.Black,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
            Text(
                data.second,
                color = if (isSelected) Color.White else Color.Black,
                fontSize = 12.sp
            )
        }
        AnimatedContent(isSelected) { isSelected ->
            if (isSelected) {
                Image(painterResource(R.drawable.check), contentDescription = null)
            } else {
                Image(
                    painterResource(R.drawable.circle_selectorellipse5),
                    contentDescription = null
                )
            }

        }
    }
}
@Composable
fun GenderCard2(
    modifier: Modifier,
    genderType: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit
)
{

    val data = when (genderType) {
        GenderType.MALE.type -> {
            Triple("Male", "I am male", R.drawable.maleimage26)
        }

        GenderType.FEMALE.type -> {
            Triple("Female", "I am female", R.drawable.femaleimage25)
        }

        else -> {
            Triple("Others", "I prefer not to disclose", R.drawable.otherimage27)
        }
    }

    val radiusAnim by animateDpAsState(if (isSelected) 20.dp else 5.dp)
    val shadowAlphaAnim by animateFloatAsState(if (isSelected) 0.7f else 0.2f)
    val bgColor by animateColorAsState(if (isSelected) Color.Black else Color.White)
    val borderColor by animateColorAsState(if (isSelected) grey_1 else Color.Transparent)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .dropShadow(
                shadow = Shadow(
                    radius = radiusAnim,
                    color = theme_color_2.copy(shadowAlphaAnim)
                ),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .border(width = 1.dp, color = borderColor, shape = CircleShape)
            .background(brush = if(isSelected) brush2() else brushWhite(), shape = CircleShape)
            .padding(horizontal = 10.dp, vertical = 10.dp)) {

        Image(
            painterResource(data.third),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        BasicText(
            data.first,
            style = TextStyle(
                color = Color.Black,
                fontFamily = if (isSelected) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_REGULAR,
                fontSize = 12.sp
            )
        )
    }
}