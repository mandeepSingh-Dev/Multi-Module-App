package com.app.feature_home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.components.GenderCard2
import com.app.core_ui.R
import com.app.core_ui.components.CitySelectionScreen
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.interestCard
import com.app.core_ui.enums.GenderType
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brushWhite
import com.app.core_ui.ui.theme.green_14
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_12
import com.app.feature_home.domain.model.HostFilterState
import com.uae.core_network.data.model.CitiesResponse
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


const val MIN_AGE = 18F
const val MAX_AGE = 45F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun FilterScreen(
    show: Boolean = true,
    filterState: HostFilterState? = null,
    isCitiesLoading: Boolean? = false,
    citiesList: List<CitiesResponse.CityData?>? = null,
    onStateChange: (HostFilterState?) -> Unit = {},
    onDismiss: () -> Unit = {},
    onApply: () -> Unit = {},
    onReset: () -> Unit = {},
) {

    var showCitiesList by rememberSaveable { mutableStateOf(false) }

    if (show) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            dragHandle = null,
            containerColor = Color.White,
            shape = Shape_20,
            modifier = Modifier
                .dropShadowCustom()
                .fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFD9D9D9))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Filters",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Find your perfect host",
                            fontSize = 13.sp,
                            color = grey_4
                        )
                    }
                    // Close button placeholder
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF0F0F0))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(R.drawable.baseline_close_24),
                            contentDescription = "Close",
                            colorFilter = ColorFilter.tint(theme_color_12),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------- Availability ----------
                SectionTitle("Availability")
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PillToggle(
                        text = "Online",
                        selected = filterState?.isOnline == 1,
                        showDot = filterState?.isOnline == 1,
                        onClick = {


                            onStateChange(filterState?.copy(isOnline = if (filterState.isOnline == null || filterState.isOnline == 0) 1 else null))
                        }
                    )
                  /*  PillToggle(
                        text = "Offline",
                        selected = filterState?.isOnline == 0,
                        showDot = filterState?.isOnline == 0,
                        onClick = {

                            onStateChange(filterState?.copy(isOnline = if (filterState.isOnline == null || filterState.isOnline == 1) 0 else null))

                        }
                    )*/
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------- Gender ----------
                SectionTitle("Gender")
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GenderType.entries.forEach { gender ->
                        GenderCard2(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp),
                            genderType = gender.type,
                            isSelected = filterState?.gender == gender.type,
                            onClick = {
                                onStateChange(filterState?.copy(gender = if (gender.type != filterState.gender) gender.type else null))
                            })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------- Age Range ----------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SectionTitle("Age Range")
                    Text(
                        text = "${filterState?.ageRange?.start?.toInt() ?: MIN_AGE.toInt()} - ${filterState?.ageRange?.endInclusive?.toInt() ?: MAX_AGE.toInt()}+",
                        fontSize = 13.sp,
                        color = grey_4
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                RangeSlider(
                    value = filterState?.ageRange ?: MIN_AGE..MAX_AGE,
                    onValueChange = {
                        onStateChange(filterState?.copy(ageRange = it))
                    },
                    valueRange = 18f..45f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = theme_color_12,
                        activeTrackColor = theme_color_12,
                        inactiveTrackColor = Color(0xFFF0D9AE)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "18", fontSize = 11.sp, color = grey_4)
                    Text(text = "25", fontSize = 11.sp, color = grey_4)
                    Text(text = "30", fontSize = 11.sp, color = grey_4)
                    Text(text = "35", fontSize = 11.sp, color = grey_4)
                    Text(text = "40", fontSize = 11.sp, color = grey_4)
                    Text(text = "45+", fontSize = 11.sp, color = grey_4)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------- Location ----------
                SectionTitle("Location")
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(Shape_15)
                        .clickable {
                            showCitiesList = true
                        }
                        .border(1.dp, Color(0xFFE0E0E0), Shape_15)
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier) {
                        Image(painterResource(R.drawable.location_gold), contentDescription = null, modifier = Modifier.size(15.dp) )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = filterState?.location ?: "Select Location", fontSize = 14.sp, color = if(filterState?.location.isNullOrEmpty()) text_grey_5 else Color.Black)
                    }
                    Image(painterResource(R.drawable.baseline_keyboard_arrow_down_24), contentDescription = null, modifier = Modifier.size(12.dp) )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------- Interests ----------
                SectionTitle("Interests")
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3,
                )
                {
                    filterState?.interests?.forEach { interest ->
                        interestCard(
                            interest.name,
                            selected = interest.isSelected,
                            onSelect = {
                                val mList = filterState.interests.toMutableList()
                                val index = mList?.indexOfFirst { it._id == interest._id } ?: -1
                                if (index != -1) {
                                    mList?.set(
                                        index,
                                        interest.copy(isSelected = !interest.isSelected)
                                    )
                                    onStateChange(
                                        filterState.copy(interests = mList)
                                    )
                                }

                            },
                        )

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // ---------- Minimum Rating ----------
                SectionTitle("Minimum Rating")
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("All", "4.0+", "4.5+", "4.8+").forEach { rating ->
                        PillToggle(
                            text = rating,
                            selected = filterState?.minimumRating == rating,
                            leadingPlaceholder = rating != "All",
                            onClick = {
                                onStateChange(filterState?.copy(minimumRating = if (filterState.minimumRating != rating) rating else null))
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- Apply Button ----------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(brush2())
                        .clickable { onApply() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Apply Filter  >>",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Reset",
                    fontSize = 14.sp,
                    color = theme_color_12,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onReset() }
                )
            }
        }
    }


    CitySelectionScreen(
        showShow = showCitiesList,
        cities = citiesList?.filterNot { it?.city.isNullOrEmpty() }
            ?.map { (it?.city ?: "").replaceFirstChar { it.uppercase() } } ?: emptyList(),
        isCitiesLoading = isCitiesLoading,
        selectedCity = filterState?.location,
        onDismiss = {
            showCitiesList = false
        },

        onCitySelected = {
            showCitiesList = false
            onStateChange(
                filterState?.copy(location = it)
            )
        },
    )
}

// ---------- Reusable pieces ----------

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = grey_4
    )
}

@Composable
private fun PillToggle(
    text: String,
    selected: Boolean,
    showDot: Boolean = false,
    leadingPlaceholder: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CircleShape)
            .border(width = 1.dp, color = grey_4, CircleShape)
            .background(if (selected) brush2() else brushWhite())
            .clickable { onClick() }
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        AnimatedVisibility(showDot) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .border(width = 1.dp, color = Color.White, CircleShape)
                    .background(color = green_14, shape = CircleShape)
            )
        }
        if (showDot) {
            Spacer(modifier = Modifier.width(6.dp))
        }
        if (leadingPlaceholder) {
            Image(
                painterResource(R.drawable.starcontainer),
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            fontSize = 13.sp,
            fontFamily = if (selected) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_MEDIUM,
            color = if (selected) Color.Black else grey_4
        )
    }
}


/** Simple circular placeholder — swap this for your own icon/image composable. */
@Composable
private fun PlaceholderIcon(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFFD9D9D9))
    )
}