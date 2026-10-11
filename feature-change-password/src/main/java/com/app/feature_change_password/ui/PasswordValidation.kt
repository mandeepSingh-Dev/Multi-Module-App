package com.app.feature_change_password.ui
data class PasswordValidation(
    val minLength: Boolean,
    val hasUppercase: Boolean,
    val hasLowercase: Boolean,
    val hasNumber: Boolean,
    val hasSpecialCharacter: Boolean
) {
    val isValid: Boolean
        get() = minLength &&
                hasUppercase &&
                hasLowercase &&
                hasNumber &&
                hasSpecialCharacter
}

fun validatePassword(password: String): PasswordValidation {
    return PasswordValidation(
        minLength = password.length >= 8,
        hasUppercase = password.any { it.isUpperCase() },
        hasLowercase = password.any { it.isLowerCase() },
        hasNumber = password.any { it.isDigit() },
        hasSpecialCharacter = password.any {
            it in "!@#$%^&*()_+-=[]{}|;:',.<>/?`~\"\\"
        }
    )
}