package com.app.core_ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.core_ui.R
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.theme_color_12


enum class EmptyViewType {
    NOTIFICATION,
    MESSAGE,
    BLOCKED_USERS,
    CALL_HISTORY,
    TRANSACTIONS,
    NONE
}

@Composable
fun emptyView(modifier : Modifier = Modifier,
              isShowImage :Boolean = true,
              emptyViewType: EmptyViewType = EmptyViewType.NONE
) {

    Box(modifier = modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)) {

        if(isShowImage) {

            val image = when(emptyViewType){
                EmptyViewType.NOTIFICATION -> R.drawable.no_notifications
                EmptyViewType.MESSAGE -> R.drawable.no_message
                EmptyViewType.BLOCKED_USERS -> R.drawable.no_blocked_users
                EmptyViewType.CALL_HISTORY -> R.drawable.no_call_history
                EmptyViewType.TRANSACTIONS -> R.drawable.no_transactions
                EmptyViewType.NONE -> R.drawable.kaho_ji_logoimage1
            }

            Image(
                painter = painterResource(image), contentDescription = null,
                modifier = Modifier.sizeIn(maxWidth = 150.dp)
            )
        }
        if(emptyViewType == EmptyViewType.NONE) {
            Text(
                "No data found", color = Color.Black,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = HAFFER_TRIAL_BOLD,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun bottomPagingLoader(
    isVisible : Boolean = false,
    isRetry : Boolean = false,
    onRetry : () -> Unit = {}
){
    if(isVisible) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            if(!isRetry) {
                LoadingIndicator(
                    color = theme_color_12,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 10.dp)
                )
            }
            if (isRetry) {
                Text(text = "Loading Failed", modifier = Modifier.padding(start = 10.dp))
                TextButton(onClick = onRetry,) {
                    Text("RETRY", modifier = Modifier.padding(start = 10.dp))
                }
            }
        }
    }


}


