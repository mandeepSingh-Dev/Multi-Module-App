package com.app.feature_onboarding.ui

import android.util.SparseIntArray
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.HostScreens
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.strokeGapModifier
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.brush2Vertical
import com.app.core_ui.ui.theme.brush7
import com.app.core_ui.ui.theme.brushGrey
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_5
import com.zodiaq.ui.theme.Shape_12
import com.zodiaq.ui.theme.Shape_20

@Composable
fun UserTypeSelectionScreen(backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
                            userTypeSelectionViewModel: UserTypeSelectionViewModel = hiltViewModel()
) {

    var selectedUserType by rememberSaveable { mutableIntStateOf(UserType.USER.value) }



    BoxCommon(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Column(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_1image1),
                    contentDescription = null,
                    modifier = Modifier
                )
                Text("Talk . Connect Enjoy")

                Text(
                    "How will you use Kahoji?",
                    style = TextStyle(
                        brush = brush7(),
                        fontFamily = HAFFER_TRIAL_BOLD,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.padding(top = 23.dp, bottom = 10.dp)
                )


                Row(modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 20.dp, vertical = 20.dp).padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    UserTypeSelectionCard(
                        modifier = Modifier.weight(1f)
                            .fillMaxHeight(),
                        icon = R.drawable.user_imgbackground_border_shadow,
                        type = "User",
                        desc = "Connect with amazing people, chat, call and make new friends.",
                        isSelected = selectedUserType == UserType.USER.value
                    ) {
                        selectedUserType = UserType.USER.value
                    }
                    UserTypeSelectionCard(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        icon = R.drawable.hostyy_imgmargin,
                        type = "Host",
                        desc = "Create your profile, engage your audience and earn rewards.",
                        isSelected = selectedUserType == UserType.HOST.value
                    ) {
                        selectedUserType = UserType.HOST.value
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 5.dp)
                                .strokeGapModifier()
                       .background(color = Color.White, Shape_12).padding(10.dp)
                ){
                    Image(painter = painterResource(R.drawable.private_iconimage16), contentDescription = null)
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("100% Safe & Private", fontFamily = HAFFER_TRIAL_BOLD, color = theme_color_1)
                        Text("Your privacy and security are our top priority.", color = Color.Black, fontSize = 12.sp)
                    }
                }

            }

            GradientButton(onClick = {
                if(selectedUserType == UserType.USER.value){
                    backStack.add(UserScreens.AuthScreens.AgeConfirmationScreen)
                }else{
                    backStack.add(HostScreens.AuthHostScreens.LoginHostScreen)
                }
            },
                text = "Continue",
                showEndIcon = true,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }

}

@Composable
fun UserTypeSelectionCard(
    modifier: Modifier = Modifier,
    icon: Int,
    type: String,
    desc: String,
    isSelected: Boolean = true,
    onSelect: () -> Unit
) {

    val bgColor by animateColorAsState(if (isSelected) Color.Black else Color.White, animationSpec = tween(durationMillis = 800))
    val textColor by animateColorAsState(if (isSelected) theme_color_5 else Color.Black)
    val textDescColor by animateColorAsState(if (isSelected) grey_1 else Color.Black)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(Shape_20)
            .border(width = 1.dp, brush = if(isSelected) brush2Vertical() else brushGrey(), Shape_20)
            .clickable {
                onSelect()
            }
            .background(color = bgColor, Shape_20)
            .padding(horizontal = 20.dp, vertical = 10.dp)) {

        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
        )
        Text(text = type, color = textColor, fontFamily = HAFFER_TRIAL_BOLD, fontSize = 18.sp)
        Text(
            text = desc,
            color = textDescColor,
            fontFamily = HAFFER_TRIAL_MEDIUM,
            modifier = Modifier.weight(1f).padding(top = 20.dp),
            fontSize = 12.sp,
            textAlign = TextAlign.Center)

        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .width(40.dp)
                .height(3.dp)
                .background(color = theme_color_5, shape = CircleShape)
        )
    }

}