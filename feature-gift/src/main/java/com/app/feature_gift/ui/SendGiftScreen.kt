package com.app.feature_gift.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.CoinsCountsCard
import com.app.core_ui.components.DragLine
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.giftItem
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP_MEDIUM
import com.app.core_ui.ui.theme.green_10
import com.app.core_ui.ui.theme.green_11
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_12
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.launch
import java.util.SortedMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendGiftScreen(
    show: Boolean = false,
    onDismiss: () -> Unit,
    isGiftsListLoading: Boolean?,
    isGiftSending: Boolean?,
    giftsList: List<GiftListResponse.GiftData?>?,
    onSend: (GiftListResponse.GiftData?) -> Unit,
) {


    if (show) {

        val coroutineScope = rememberCoroutineScope()

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        val groupedList = remember {
            giftsList
                ?.groupBy { it?.tier?.name ?: "" }
                ?.toSortedMap()
        }

        var giftsByTier by remember {
            mutableStateOf(
                groupedList
            )
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            dragHandle = {},
            sheetGesturesEnabled = false,
            containerColor = Color.Transparent,
            sheetState = sheetState,
            modifier = Modifier.fillMaxSize()
        )
        {


            Column(modifier = Modifier.fillMaxSize()) {
                CoinsCountsCard(
                    null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 20.dp, bottom = 40.dp)
                )
                Box(modifier = Modifier.fillMaxSize())
                {
                    Image(
                        painterResource(R.drawable.shape_1),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )

                    Image(
                        painterResource(R.drawable.baseline_close_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            theme_color_12
                        ),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(horizontal = 20.dp)
                            .padding(top = 50.dp)
                            .clip(CircleShape)
                            .clickable {
                                onDismiss()
                            })

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DragLine(modifier = Modifier.width(40.dp))
                        Text(
                            "Send a Gift", fontSize = 17.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        giftsTabBars(giftsByTier, onClick = { name ->
                            giftsByTier = if (name == "All Gifts") {
                                groupedList
                            } else {
                                groupedList?.filterKeys { it == name }?.toSortedMap()
                            }
                        })

                        AnimatedVisibility(isGiftsListLoading ?: false) {
                            CircularProgressIndicator()
                        }

                        AnimatedContent(giftsByTier) { giftsByTier ->

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                            )
                            {
                                giftsByTier?.toMap()?.forEach { (key, list) ->
                                    tierRow(
                                        modifier = Modifier.padding(top = 20.dp),
                                        title = key.replaceFirstChar { it.uppercase() },
                                        coins = list.firstOrNull()?.tier?.coinRange
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                    )
                                    {
                                        list.forEach { gift ->
                                            giftItem(

                                                bgColor = green_10,
                                                borderColor = green_11,
                                                onClick = {
                                                    coroutineScope.launch {
                                                        sheetState.hide()
                                                    }
                                                    onDismiss()
                                                    onSend(gift)
                                                },
                                                image = gift?.image,
                                                name = gift?.name,
                                                coinCost = gift?.coinCost
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun giftsTabBars(
    giftsByTier: SortedMap<String, List<GiftListResponse.GiftData?>>?,
    onClick: (String) -> Unit
) {

    var selectedIndex by remember { mutableStateOf(0) }

    val tierTabs = giftsByTier?.map { it.key } ?: emptyList()

    val tabsList = remember {
        listOf(
            "All Gifts"
        ) + tierTabs
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {

        tabsList.forEachIndexed { index, item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clip(Shape_20)
                    .clickableDebAnim {
                        selectedIndex = index
                        onClick(item)
                    }
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    item, color =
                        if (selectedIndex == index) theme_color_11 else text_grey_1,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 13.sp
                )
                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .then(
                            if (selectedIndex == index) {
                                Modifier.fillMaxWidth()
                            } else {
                                Modifier
                            }

                        )
                        .height(2.dp)
                        .background(color = theme_color_11, Shape_20)
                )

            }
        }
    }
}

@Composable
private fun tierRow(
    modifier: Modifier,
    title: String,
    coins: String?
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            title, fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = HEADING_ROW_SP_MEDIUM,
            modifier = Modifier.weight(1f)
        )
        coins?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.multi_coinsimage46),
                    contentDescription = null
                )
                Text(coins, fontSize = 13.sp)
            }
        }
    }
}
