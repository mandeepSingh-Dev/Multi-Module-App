package com.app.feature_help_support.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP_MEDIUM
import com.app.core_ui.ui.theme.brush9
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.text_grey_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_2
import com.app.core_ui.ui.theme.theme_color_24
import com.app.feature_help_support.ui.viewmodels.HelpSupportViewModel
import com.uae.core_network.data.model.FaqsResponse
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


@Composable
fun HelpSupportScreen(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    helpSupportViewModel: HelpSupportViewModel = hiltViewModel()
) {


    val uiState by helpSupportViewModel.uiState.collectAsStateWithLifecycle()

    ObserveUIEvents(helpSupportViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {}
        }
    }

    BoxCommon {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            HeaderView2(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                title = "Help & Support",
                secTitle = "Get quick answers and support whenever you need it.",
                showNotificationIcon = false,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .animateContentSize()
            ) {

                WeRHereHelpCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, top = 10.dp)
                )
                FaqsListSection(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    faqs = uiState?.faqs,
                    isLoading = uiState?.isLoading ?: true,
                    onClick = {
                        helpSupportViewModel.toggleFaqExpanded(it)
                    })

                YourConcernCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp,horizontal = 20.dp)
                )

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp))
            }
        }
    }

}

@Composable
private fun WeRHereHelpCard(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(Shape_15)
            .dropShadowCustom(Shape_15, color = theme_color_1)
            .background(brush = brush9(), Shape_15)
            .padding(15.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.faq_image), contentDescription = null,
            modifier = Modifier
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("We're Here to Help!", fontSize = 16.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
            Text(
                "Need assistance? Our support team is always ready to help you.",
                fontSize = 12.sp, color = text_grey_3
            )
        }
    }
}


@Composable
fun FaqsListSection(
    modifier: Modifier, faqs: List<FaqsResponse.FaqData?>?,
    isLoading: Boolean?,
    onClick: (FaqsResponse.FaqData?) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(painter = painterResource(R.drawable.faq_icon), contentDescription = null)
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("FAQ", fontSize = HEADING_ROW_SP_MEDIUM)
            Text("Find answers to common questions", fontSize = 12.sp, color = text_grey_2)

        }
    }

    Card(
        modifier = Modifier
            .padding(top = 10.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        AnimatedContent(isLoading) { isLoading ->
            if(isLoading == true) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                {
                  repeat(5){
                      ShimmerBox(modifier = Modifier.fillMaxWidth().height(80.dp)
                          .padding(horizontal = 20.dp, vertical = 16.dp)
                      )
                  }
                }
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                {
                    faqs?.forEachIndexed { index, item ->

                        ExpandableFaqRow(
                            question = item?.title ?: "",
                            answer = item?.description ?: "",
                            isExpanded = item?.isExpanded ?: false,
                            onToggle = {
                                onClick(item)
//                            expandedItemId = if (isExpanded) null else item.id
                            }
                        )

                        if (index < faqs.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.8.dp,
                                color = Color(0xFFEFEFEF)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableFaqRow(
    question: String,
    answer: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    // Chevron rotation animation (0 deg to 90 deg)
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ArrowRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2C353E),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color(0xFF8A9AAD),
                modifier = Modifier
                    .size(20.dp)
                    .rotate(arrowRotation)
            )
        }


        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(200, delayMillis = 100)) +
                    expandVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)),
            exit = fadeOut(animationSpec = tween(100)) +
                    shrinkVertically(animationSpec = spring(stiffness = Spring.StiffnessLow))
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = answer,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF627183)
                )
            }
        }
    }
}

@Composable
fun YourConcernCard(modifier: Modifier) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(painter = painterResource(R.drawable.faq_chat), contentDescription = null)
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Your Concern", fontSize = HEADING_ROW_SP_MEDIUM)
            Text("Can't find answers?We're here to help!", fontSize = 12.sp, color = text_grey_2)

        }
    }

    Column(
        modifier = Modifier
            .padding(top = 10.dp, horizontal = 20.dp)
            .fillMaxWidth()
            .dropShadowCustom()
            .background(color = Color.White, shape = Shape_20)
            .padding(15.dp),
    )
    {

        Text("Contact pur Support Team", fontSize = HEADING_ROW_SP_MEDIUM, fontFamily = HAFFER_TRIAL_SEMI_BOLD)

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .dropShadowCustom(Shape_15)
                    .background(color = theme_color_24, Shape_15)
                    .padding(5.dp, vertical = 13.dp)

            ) {

                Image(painterResource(R.drawable.faq_iconlive_chat), contentDescription = null)
                Text(
                    "Live Chat",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
                Text(
                    "Chat with us",
                    color = text_grey_3,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .dropShadowCustom(Shape_15)
                    .background(color = theme_color_24, Shape_15)
                    .padding(5.dp, vertical = 13.dp)

            ) {

                Image(painterResource(R.drawable.faq_iconlive_chat), contentDescription = null)
                Text(
                    "Email Us",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
                Text(
                    "Pop@gmail.com",
                    color = text_grey_3,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .dropShadowCustom(Shape_15)
                    .background(color = theme_color_24, Shape_15)
                    .padding(5.dp, vertical = 13.dp)

            ) {
                Image(painterResource(R.drawable.faq_iconlive_chat), contentDescription = null)
                Text(
                    "Call Us",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
                Text(
                    "+91 1234567890",
                    color = text_grey_3,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )

            }

        }
    }

}
























