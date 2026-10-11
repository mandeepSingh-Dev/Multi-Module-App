package com.app.feature_auth_host.ui.screens.signup_steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.theme_color_13
import com.app.feature_auth_host.ui.states.SignUpScreenUIState
import com.app.feature_auth_host.ui.viewmodels.SignUpViewModel
import com.zodiaq.ui.theme.Shape_15

@Composable
fun BankDetails(
    modifier: Modifier = Modifier,
    uiState: SignUpScreenUIState?,
    signUpViewModel: SignUpViewModel,
    onNext: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GradientText(
                text = "Bank Details", fontSize = 20.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            Text(
                "Securely add your bank details to receive payouts.", textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
            )

            TextFieldOuterLabel(
                modifier = Modifier.padding(top = 15.dp),
                label = "Account Holder Name",
                value = uiState?.profileRequestBody?.accountHolderName ?: "",
                onValueChange = {
                    signUpViewModel.updateState { state ->
                        state.copy(profileRequestBody = state.profileRequestBody?.copy(accountHolderName = it))
                    }
                },
                singleLine = true,
                hint = "Account holder name",
                isBorder = true,
                isRequired = true,
                leadingIcon = {
                    Image(painterResource(R.drawable.know_languagname), contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.PersonName,
                    capitalization = KeyboardCapitalization.Words
                ),
            )
            TextFieldOuterLabel(
                modifier = Modifier.padding(top = 15.dp),
                label = "Bank Name",
                value = uiState?.profileRequestBody?.bankName ?: "",
                onValueChange = {
                    signUpViewModel.updateState { state ->
                        state.copy(profileRequestBody = state.profileRequestBody?.copy(bankName = it))
                    }
                },
                singleLine = true,
                hint = "Enter Your bank",
                isBorder = true,
//                enabled = false,
                isRequired = true,
                leadingIcon = {
                    Image(
                        painterResource(R.drawable.know_languagbank_icon),
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )

            TextFieldOuterLabel(
                modifier = Modifier.padding(top = 15.dp),
                label = "Account Number",
                value = uiState?.profileRequestBody?.accountNumber ?: "",
                onValueChange = { value ->

                    val accountNumber = value
                        .filter { it.isDigit() }
                        .take(18)

                    signUpViewModel.updateState { state ->
                        state.copy(profileRequestBody = state.profileRequestBody?.copy(accountNumber = value))
                    }
                },
                singleLine = true,
                hint = "eg: 123456789112",
                isBorder = true,
                isRequired = true,
                leadingIcon = {
                    Image(
                        painterResource(R.drawable.know_languagaccountnumber),
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
            )

            TextFieldOuterLabel(
                modifier = Modifier.padding(top = 15.dp),
                label = "IFSC Code",
                value = uiState?.profileRequestBody?.ifscCode ?: "",
                onValueChange = { value ->

                    val ifsc = value
                        .uppercase()
                        .filter { it.isLetterOrDigit() }
                        .take(11)

                    // Allow only valid IFSC structure while typing
                    if (
                        ifsc.length <= 4 ||
                        (ifsc.length == 5 && ifsc[4] == '0') ||
                        (ifsc.length > 5 && ifsc[4] == '0')
                    ) {
                        signUpViewModel.updateState { state ->
                            state.copy(profileRequestBody = state.profileRequestBody?.copy(ifscCode = ifsc))
                        }
                    }
                },
                singleLine = true,
                hint = "Enter IFSC Code",
                isBorder = true,
                isRequired = true,
                leadingIcon = {
                    Image(painterResource(R.drawable.know_languagifsc), contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )

            DataIsSafe(modifier = Modifier.padding(top = 20.dp))

            Spacer(modifier = Modifier.height(30.dp))

        }
        GradientButton(
            onClick = onNext,
            text = "Continue",
            showEndIcon = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, top = 2.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))
    }


}


@Composable
private fun DataIsSafe(modifier: Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_15, Color.Black.copy(0.1f), radius = 5.dp)
            .background(color = theme_color_13, shape = Shape_15)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Image(painterResource(R.drawable.know_languagdata_safe), contentDescription = null)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("Your data is safe with us", fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp)
            Text(
                "We use bank-level security to keep your information private and secure.",
                fontSize = 12.sp
            )
        }
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
