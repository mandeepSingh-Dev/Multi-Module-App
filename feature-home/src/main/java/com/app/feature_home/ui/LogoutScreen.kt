package com.app.feature_home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.core_ui.R
import com.app.core_ui.components.CoinsCountsCard
import com.app.core_ui.components.FilledCustomButton
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.theme_color_1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LogoutScreen() {


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {},
        dragHandle = {},
        sheetGesturesEnabled = false,
        containerColor = Color.Transparent
    ) {

        Box(modifier = Modifier)
        {
            Image(
                painterResource(R.drawable.shape_1),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Image(painterResource(R.drawable.logout_iconimage79), contentDescription = null)

                GradientText("Logout?")
                Text("You will be signed out from your account on this device",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, horizontal = 20.dp))

                GradientButton(onClick = {},
                    showEndIcon = true,
                    text = "Yes, Logout",
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp))

                FilledCustomButton(onClick = {},
                    text = "Cancel",
                    color = Color.Transparent,
                    textColor = theme_color_1,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 2.dp))

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }

}