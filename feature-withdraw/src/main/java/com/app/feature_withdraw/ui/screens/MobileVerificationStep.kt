package com.app.feature_withdraw.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.OTPBottomSheetScreen
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.theme_color_13
import com.app.feature_withdraw.ui.events.IdentityVerificationScreenEvents
import com.app.feature_withdraw.ui.states.IdentityVerificationUIState
import com.app.feature_withdraw.ui.viewmodels.IdentityVerificationViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20

@Composable
fun ColumnScope.MobileVerification(uiState: IdentityVerificationUIState?) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GradientText(
            text = "Mobile Verification", fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
        Text(
            "We'll send a verification code to your registered mobile number.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
        )

        RegisteredMobileNumberFields(
            uiState?.phone,
            modifier = Modifier.padding(top = 20.dp)
        )

        OTPDigitInfoCard(
            modifier = Modifier.padding(top = 20.dp)

        )
    }


}

@Composable
private fun RegisteredMobileNumberFields(phone: String?, modifier: Modifier) {


    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .dropShadowCustom(Shape_20)
            .background(color = Color.White, Shape_20)
            .padding(15.dp)
    ) {

        Image(painterResource(R.drawable.phone_with_bgphoneiconbox), contentDescription = null)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            Text("Registered Mobile Number", fontSize = 12.sp, color = text_grey_1)
            Text(phone ?: "")

        }
    }

}

@Composable
fun OTPDigitInfoCard(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_15, Color.Black.copy(0.1f), radius = 5.dp)
            .background(color = theme_color_13, shape = Shape_15)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Image(
            painterResource(com.app.feature_withdraw.R.drawable.otp_iconshieldcheckicon),
            contentDescription = null
        )

        Text(
            "A 6-digit OTP will be sent to your registered mobile number. Please keep your phone handy.",
            fontSize = 12.sp,
            color = text_grey_1,
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(painterResource(R.drawable.star_dectext), contentDescription = null)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.star_dectext), contentDescription = null,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }

}
