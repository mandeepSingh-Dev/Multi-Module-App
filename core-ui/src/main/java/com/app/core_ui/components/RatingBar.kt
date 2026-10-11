package com.app.core_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.core_ui.R

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    starSize : Dp = 35.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(15.dp),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        repeat(maxRating) { index ->

            val icon = if(index < rating) {
                R.drawable.star_2vector
            } else{
                R.drawable.star_2_notselectedvector
            }

            Image(
                painterResource(icon),
                contentDescription = "Rating ${index + 1}",
                modifier = Modifier
                    .size(starSize)
                    .clickable {
                        onRatingChanged(index + 1)
                    }
            )
        }
    }
}