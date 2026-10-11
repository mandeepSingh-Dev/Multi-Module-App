package com.app.core_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_ui.R
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD


@Composable
fun HeaderView(
    onBack : () -> Unit,
    onSkip : () -> Unit,
) {


    Row(modifier = Modifier.fillMaxWidth().systemBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        Image(painterResource(R.drawable.back), contentDescription = null,
            modifier = Modifier.padding(top = 2.dp).clickableDebAnim{
                onBack()
            })
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Profile Setup", fontFamily = HAFFER_TRIAL_BOLD, fontSize = 20.sp)
            Text("Create you profile and tell us a little about yourself.")
        }
        OutlineButton2(onClick = onSkip,
            text = "Skip",
            isBorder = true,
            shape = CircleShape,
            horizontalPadding = 20.dp,
            verticalPadding = 10.dp
        )
    }

}
@Composable
fun HeaderView2(
    modifier: Modifier = Modifier,
    title : String,
    secTitle : String? = null,
    showNotificationIcon : Boolean = true,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    onNotification : (() -> Unit)? = null,
    onBack : (() -> Unit)? = null,
) {

    Row(modifier = modifier.fillMaxWidth().statusBarsPadding()
        .padding(top = 10.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        Image(painterResource(R.drawable.back), contentDescription = null,
            modifier = Modifier.padding(top = 2.dp)
                .clip(CircleShape)
                .clickable{
                    onBack?.invoke() ?: backStack?.removeLastOrNull()
                }.padding(2.dp))
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, fontFamily = HAFFER_TRIAL_BOLD, fontSize = 20.sp)
            secTitle?.let {
                Text(it, fontSize = 13.sp, lineHeight = 13.sp)
            }
        }
        if(showNotificationIcon) {
            Image(painterResource(R.drawable.notificationcontainer), contentDescription = null,
                modifier = Modifier.clickableDebAnim{
                    onNotification?.invoke()
                })
        }
    }

}
@Composable
fun HeaderView3(
    modifier: Modifier = Modifier,
    title : String,
    secTitle : String? = null,
    showNotificationIcon : Boolean = true,
    onBack : (() -> Unit)? = null,
    onNotification : (() -> Unit)? = null,
    ) {


    Row(modifier = modifier.fillMaxWidth().statusBarsPadding()
        .padding(top = 10.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        Image(painterResource(R.drawable.back), contentDescription = null,
            modifier = Modifier.padding(top = 2.dp)
                .clickable{
                    onBack?.invoke()
                })
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, fontFamily = HAFFER_TRIAL_BOLD, fontSize = 20.sp)
            secTitle?.let {
                Text(it, fontSize = 13.sp, lineHeight = 13.sp)
            }
        }
        if(showNotificationIcon) {
            Image(painterResource(R.drawable.notificationcontainer), contentDescription = null,
                modifier = Modifier.clickableDebAnim{
                    onNotification?.invoke()
                })
        }
    }

}