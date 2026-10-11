package com.app.feature_cms.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.CMSType
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalSnackBarHostState

@Composable
fun CMSScreen(
    type: Int,
    role: Int,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    cmsViewModel: CmsViewModel = hiltViewModel()
) {


    val uiState by cmsViewModel.uiState.collectAsStateWithLifecycle()
//
    ObserveUIEvents(cmsViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {}
        }
    }

    LaunchedEffect(type) {
        cmsViewModel.getCMS(
            type = type,
            role = role
        )
    }

    var showMainImage by rememberSaveable { mutableStateOf(false) }

    BoxCommon(
        showBackgroundImage = false,
        containerColor = Color.White
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            val title = if(type == CMSType.PRIVACY_POLICY.value){
                "Privacy Policy"
            }else{
                "Terms & Conditions"
            }


            HeaderView2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                title = title,
                showNotificationIcon = false,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .animateContentSize()
            ) {

                val image = when (type) {
                    CMSType.PRIVACY_POLICY.value -> R.drawable.privacy_image
                    CMSType.TERMS_CONDITION.value -> R.drawable.terms_image
                    else -> null
                }
                image?.let {
                    AnimatedVisibility(showMainImage, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Image(
                            painterResource(image), contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 20.dp)
                        )
                    }
                }
                if(uiState?.isLoading ?: false && uiState?.content == null) {
                        CMSContentShimmer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        )
                    } else {
                        HtmlWebView(
                            htmlContent = uiState?.content ?: "",
                            modifier = Modifier.fillMaxSize()
                                .onVisibilityChanged{
                                    if(it) {
                                        showMainImage = true
                                    }
                                }
                        )
                }
            }

        }
    }
}

@Composable
fun CMSContentShimmer(modifier: Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ShimmerBox(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp))
        Spacer(modifier = Modifier.height(10.dp))

        ShimmerBox(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp))
        Spacer(modifier = Modifier.height(10.dp))

        repeat(7) {
            Spacer(modifier = Modifier.height(30.dp))
            ShimmerBox(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))

            repeat(3) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ShimmerBox(modifier = Modifier.size(20.dp), shape = CircleShape)
                    ShimmerBox(modifier = Modifier
                        .weight(1f)
                        .height(30.dp))
                }
            }
        }
    }
}


@Composable
fun HtmlWebView(
    htmlContent: String,
    modifier: Modifier = Modifier
) {
    // Add mobile responsive viewport & default CSS styling to ensure clean rendering
    val styledHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                    line-height: 1.6;
                    color: #333333;
                    padding: 8px;
                }
                h1, h2, h3 {
                    color: #111111;
                }
                a {
                    color: #0066CC;
                }
            </style>
        </head>
        <body>
            $htmlContent
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.apply {
                    javaScriptEnabled = false // Keep disabled if purely static HTML
                    domStorageEnabled = true
                    defaultTextEncodingName = "utf-8"

                }
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                null,
                styledHtml,
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}

