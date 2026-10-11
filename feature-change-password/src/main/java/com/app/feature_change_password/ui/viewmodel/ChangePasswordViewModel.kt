package com.app.feature_change_password.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.requestBody.ChangePasswordRequestBody
import com.app.core_common.base.BaseViewModel
import com.app.feature_change_password.ui.ChangePasswordEvent
import com.app.feature_change_password.ui.state.ChangePasswordUIState
import com.app.feature_change_password.ui.validatePassword
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val authAllUseCases: AuthAllUseCases) :
    BaseViewModel<ChangePasswordUIState>(ChangePasswordUIState()) {
    var isChangePwdClicked = false

    fun changePassword(currentPwd: String?, newPwd: String?, confirmNewPwd: String?) {
        isChangePwdClicked = true

        updateValidationStates(currentPwd, newPwd, confirmNewPwd)

        val isAllValid = validateFields(currentPwd, newPwd, confirmNewPwd)
        if (isAllValid) {
            Log.d("fbknfnbkf", "All Valid")
            val body = ChangePasswordRequestBody(
                currentPassword = currentPwd,
                newPassword = newPwd
            )

            authAllUseCases.changePasswordUseCase(
                changePasswordRequestBody = body
            ).onEach {
                it.getCallbacks(
                    onLoading = {
                        updateState { state -> state.copy(isLoading = true) }
                    },
                    onSuccess = {
                        updateState { state -> state.copy(isLoading = false) }
                        onEvent(ChangePasswordEvent.PwdChangedSuccessfully(it.data?.message))
                    },
                    onError = {
                        updateState { state -> state.copy(isLoading = false) }
                    }
                )
            }.launchIn(viewModelScope)
        }

    }


    fun validateFields(currentPwd: String?, newPwd: String?, confirmNewPwd: String?): Boolean {

        return if (currentPwd.isNullOrEmpty()) {
            updateState { state ->
                state.copy(currentPwdError = "Please enter current password!")
            }
            false
        } else if (newPwd.isNullOrEmpty()) {
            updateState { state ->
                state.copy(newPwdError = "Please enter new password!")
            }
            false
        } else if (!validatePassword(newPwd).isValid) {
            updateState { state ->
                state.copy(passwordValidation = validatePassword(newPwd))
            }
            false
        } else if (confirmNewPwd.isNullOrEmpty()) {
            updateState { state ->
                state.copy(confirmPwdError = "Please enter confirm new password!")
            }
            false
        }else if(newPwd.lowercase() != confirmNewPwd.lowercase()){
            errorEvent("Confirm password and New password do not match")
            false
        }else{
            true
        }

    }

    fun updateValidationStates(currentPwd: String?, newPwd: String?, confirmNewPwd: String?) {

        if (currentPwd.isNullOrEmpty()) {
            updateState { state ->
                state.copy(currentPwdError = "Please enter current password!")
            }
        }
        if (newPwd.isNullOrEmpty()) {
            updateState { state ->
                state.copy(newPwdError = "Please enter new password!")
            }

        }
        newPwd?.let {
            if (!validatePassword(newPwd).isValid) {
                updateState { state ->
                    state.copy(passwordValidation = validatePassword(newPwd))
                }
            }
        }
        if (confirmNewPwd.isNullOrEmpty()) {
            updateState { state ->
                state.copy(confirmPwdError = "Please enter confirm new password!")
            }
        }

    }


}
