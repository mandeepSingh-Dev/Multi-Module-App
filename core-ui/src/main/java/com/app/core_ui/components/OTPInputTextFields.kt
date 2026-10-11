package com.app.core_ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_LIGHT
import com.app.core_ui.ui.theme.gray_light_3
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.light_blue
import com.app.core_ui.ui.theme.red
import com.app.core_ui.ui.theme.theme_color_1
import com.zodiaq.ui.theme.Shape_15
import kotlin.collections.all
import kotlin.collections.first
import kotlin.collections.forEachIndexed
import kotlin.collections.indices
import kotlin.collections.joinToString
import kotlin.text.isDigit
import kotlin.text.isEmpty
import kotlin.text.isNotEmpty

/**
 * [OTPInputTextFields] is a custom OTP input fields for the OTP verification.
 * In this composable function, [onOtpInputComplete] callback is invoked when all OTP input fields are filled,
 * or when user presses the [ImeAction.Done] ime button. (If you are controlling it (i.e., invoking validations and navigation)
 * from other [Composable] like [Button] then you can simply pass empty lambda `{}` as callback).
 *
 *  See end of this file for `example of usage`.
 *
 * @param otpLength length of OTP code (this determines the number of [OutlinedTextField] for OTP code input)
 * @param otpValues list of [String] OTP values entered by the user. The length of this list is determined by otpLength.
 * Pass this `List(otpLength) { "" }` as default for future reference (To use, it should be passed from
 * the viewModel for value update).
 * @param onUpdateOtpValuesByIndex callback is invoked when OTP value is updated by the user. For each respective
 * input field it returns `index` of the list item where it is updating, and `value` of the OTP for that specific index.
 * @param onOtpInputComplete callback to be invoked when OTP input is complete. (This is where we validate the
 * OTP code, navigate to next screen, or call any function as per requirement) - pass all these as argument.
 * In the below function [OTPInputTextFields], it is invoked when all OTP input fields are filled (till last),
 * or when user presses the [ImeAction.Done] ime button.
 * @param isError is for dynamic feedback to User for OTP input after validation. Changes the color of text fields as error red.
 * Ignore this if not needed (i.e., default to `false`).
 *
 * - Just for more understanding and reference, `List(otpLength) { "" }` creates a list of `otpLength` size i.e, if `otpLength` is 4, then it will create
 * list of size 4 with all elements as empty string, which we will update using [onUpdateOtpValuesByIndex] callback.
 */
@Composable
fun OTPInputTextFields(
    otpLength: Int,
    onUpdateOtpValuesByIndex: (Int, String) -> Unit,
    onOtpInputComplete: () -> Unit,
    modifier: Modifier = Modifier,
    otpValues: List<String> = List(otpLength) { "" },
    isError: Boolean = false,
) {
    // 1. Remember requesters so they are stable across recompositions
    val focusRequesters = remember(otpLength) { List(otpLength) { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var nextFocusIndex by remember {
        mutableIntStateOf(-1)
    }

    LaunchedEffect(nextFocusIndex) {
        if (nextFocusIndex in 0..<otpLength) {
            focusRequesters[nextFocusIndex].requestFocus()
            nextFocusIndex = -1
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            otpValues.forEachIndexed { index, value ->
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(horizontal = 2.dp)
                        .border(
                            width = 1.dp,
                            color = if (!isError) grey_1 else red,
                            shape = Shape_15
                        )
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown &&
                                keyEvent.key == Key.Backspace
                            ) {
                                if (otpValues[index].isEmpty() && index > 0) {
                                    onUpdateOtpValuesByIndex(index - 1, "")
                                    focusRequesters[index - 1].requestFocus()
                                } else {
                                    onUpdateOtpValuesByIndex(index, "")
                                }
                                true
                            } else {
                                false
                            }
                        },
                    value = value,
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }

                        when {
                            // Pasted / autofilled full code
                            digits.length >= otpLength -> {
                                for (i in 0 until otpLength) {
                                    onUpdateOtpValuesByIndex(i, digits[i].toString())
                                }
                                focusRequesters[otpLength - 1].requestFocus()
                                onOtpInputComplete()
                            }

                            // Normal single digit typed
                            digits.length == 1 -> {
                                onUpdateOtpValuesByIndex(index, digits)

                                if (index < otpLength - 1) {
                                    nextFocusIndex = index + 1
                                } else {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    onOtpInputComplete()
                                }
                            }

                            // Deleted
                            digits.isEmpty() -> onUpdateOtpValuesByIndex(index, "")

                            // Two chars typed into a filled box: take the newest digit
                            else -> {
                                onUpdateOtpValuesByIndex(index, digits.last().toString())
                                if (index < otpLength - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (index < otpLength - 1) focusRequesters[index + 1].requestFocus()
                        },
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onOtpInputComplete()
                        }
                    ),
                    singleLine = true,
                    shape = Shape_15,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme_color_1,
                        unfocusedBorderColor = light_blue,
                    ).copy(
                        focusedContainerColor = if (value.isEmpty()) Color.White else gray_light_3,
                        unfocusedContainerColor = if (value.isEmpty()) Color.White else gray_light_3,
                        disabledContainerColor = if (value.isEmpty()) Color.White else gray_light_3,
                    ),
                    isError = isError,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontFamily = HAFFER_TRIAL_MEDIUM,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
        }
        AnimatedVisibility(isError) {
            Text(
                "Wrong code, Please try again",
                color = red,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


// Reference for using OTP input fields

/*
// In Screen, inside Composable function,

OTPInputTextFields(
    otpLength = signUpUiState.otpLength,
    otpValues = signUpUiState.otpValues,
    isError = signUpUiState.isOtpError,
    onUpdateOtpValuesByIndex = { index, value ->
        screenModel.updateOtpValue(index, value)
    },
    onOtpInputComplete = {

        // Check for the otp validation
        if (screenModel.isOtpInputValuesAreValid()) {
            showLoadingDialog = true

            // Call the API for OTP verification and perform any necessary actions

            coroutineScope.launch {
                if ( /*If API response is success*/ ) {
                    showLoadingDialog = false

                    showSuccessDialogWithSuccessMessage = true

                    // After success, navigate to next screen

                } else {
                    showErrorMessage() // Response with error message
                }
            }
        }
    }
)


// In ViewModel,

data class SignUpUiState(
    val otpLength: Int = 6,
    val isOtpError: Boolean = false,
    val otpValues: List<String> = List(otpLength) { "" }
}


fun updateOtpValue(index: Int, value: String) {
    val newOtpValues = _signUpUiState.value.otpValues.toMutableList() // Making list mutable to update value

    newOtpValues[index] = value // Update value at the specified index

    _signUpUiState.update { currentState ->
        currentState.copy(
            otpValues = newOtpValues,
            isOtpError = false
        )
    }
}
*/


// Separate from above (RECOMMENDED ONLY FOR TESTING)

@Composable
fun OtpInputTestFunction(modifier: Modifier = Modifier) {
    Column(modifier) {
        // Inside composition, use this only for testing.
        // I recommend to do it through viewModel like above.
        val otpValues = remember { mutableStateListOf<String>("", "", "", "", "", "") }

        OTPInputTextFields(
            otpValues = otpValues,
            otpLength = 6,
            onOtpInputComplete = { /* TODO: Make api calls or anything after validation */ },
            onUpdateOtpValuesByIndex = { index, value ->
                otpValues[index] = value
            }
        )

        // To print or check
        Text(otpValues.joinToString())
    }
}