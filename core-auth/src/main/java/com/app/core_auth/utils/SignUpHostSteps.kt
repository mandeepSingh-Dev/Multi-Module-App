package com.app.core_auth.utils

enum class SignUpHostSteps(val value: Int) {
    BECOME_HOST(1),
    BASIC_INFORMATION(2),
    SELECT_LANGUAGE(3),
    SELECT_YOUR_INTERESTS(4),
    BANK_DETAILS(5);

    companion object {
        fun getLastStep() = entries.last()
    }
}