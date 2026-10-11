package com.app.core_common.utils

import jakarta.inject.Inject



interface DeepLinkHandler {

    fun navigateToLoginScreen()

}

var deepLinkHandler : DeepLinkHandler? = null