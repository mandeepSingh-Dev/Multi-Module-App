package com.app.core_ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// --- PREMIUM PALETTE DEFINITION ---
object DatePickerTheme {
    val DeepBackground = Color(0xFF0F172A)
    val SurfaceCard = Color(0xFF1E293B)
    val PrimaryGold = Color(0xFFFFD700)
    val AmberGlow = Color(0xFFF59E0B)
    val GradientStart = Color(0xFFD97706)
    val RangeHighlight = Color(0x33F59E0B)
    val TextMuted = Color(0xFF94A3B8)
    val TextLight = Color(0xFFF8FAFC)
}

data class DateRangeSelection(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

@Composable
@Preview
fun DateRangePicker(
    show : Boolean = true,
    onDismiss : () -> Unit = {},
    modifier: Modifier = Modifier,
    initialMonth: YearMonth = YearMonth.now(),
    onRangeSelected: (DateRangeSelection) -> Unit = {}
) {

    if(show) {
        var selectedRange by remember { mutableStateOf(DateRangeSelection()) }
        var currentMonth by remember { mutableStateOf(initialMonth) }

        Dialog(
            onDismissRequest = onDismiss,
        )
        {
            Column(
                modifier = Modifier
                    .clip(Shape_20)
                    .background(
                        shape = Shape_20,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DatePickerTheme.SurfaceCard,
                                DatePickerTheme.DeepBackground
                            )
                        )
                    )
                    .padding(20.dp)
            )
            {
                // Display Selected Date Range Bar
                SelectedRangeHeader(selectedRange = selectedRange)

                Spacer(modifier = Modifier.height(20.dp))

                // Month Navigation Controls
                MonthHeaderControls(
                    currentMonth = currentMonth,
                    onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                    onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Days of Week Labels
                DaysOfWeekHeader()

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar Grid
                CalendarGrid(
                    currentMonth = currentMonth,
                    selectedRange = selectedRange,
                    onDateSelected = { clickedDate ->
                        val newRange = when {
                            selectedRange.startDate == null || selectedRange.endDate != null -> {
                                DateRangeSelection(startDate = clickedDate, endDate = null)
                            }

                            clickedDate.isBefore(selectedRange.startDate) -> {
                                DateRangeSelection(startDate = clickedDate, endDate = null)
                            }

                            else -> {
                                DateRangeSelection(
                                    startDate = selectedRange.startDate,
                                    endDate = clickedDate
                                )
                            }
                        }
                        selectedRange = newRange
//                        onRangeSelected(newRange)
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                ) {

                    FilledCustomButton(
                        onClick = {
                            onDismiss()
                        },
                        text = "Cancel",
                        color = Color.Transparent,
                        vertical = BUTTON_VERTICAL_PADDING,
                        shape = Shape_15
                    )
                    GradientButton(
                        onClick = {
                            onRangeSelected(selectedRange)
                        },
                        text = "Done",
                        horizontalPadding = 35.dp,
                        shape = Shape_15,
                        verticalPadding = 10.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedRangeHeader(selectedRange: DateRangeSelection) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DatePickerTheme.DeepBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateHeaderChip(
            label = "From",
            dateText = selectedRange.startDate?.format(formatter) ?: "Select Date",
            isActive = selectedRange.startDate != null
        )

        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = DatePickerTheme.AmberGlow,
            modifier = Modifier.size(24.dp)
        )

        DateHeaderChip(
            label = "To",
            dateText = selectedRange.endDate?.format(formatter) ?: "Select Date",
            isActive = selectedRange.endDate != null
        )
    }
}

@Composable
private fun DateHeaderChip(label: String, dateText: String, isActive: Boolean) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = DatePickerTheme.TextMuted,
            letterSpacing = 1.sp
        )
        Text(
            text = dateText,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isActive) DatePickerTheme.PrimaryGold else DatePickerTheme.TextMuted
        )
    }
}

@Composable
private fun MonthHeaderControls(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${
                currentMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${currentMonth.year}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DatePickerTheme.TextLight
        )

        Row {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Month",
                    tint = DatePickerTheme.TextLight
                )
            }
            IconButton(onClick = onNextMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Month",
                    tint = DatePickerTheme.TextLight
                )
            }
        }
    }
}

@Composable
private fun DaysOfWeekHeader() {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(modifier = Modifier.fillMaxWidth()) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = DatePickerTheme.TextMuted
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: YearMonth,
    selectedRange: DateRangeSelection,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    val totalSlots = firstDayOfWeek + daysInMonth

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        userScrollEnabled = false
    ) {
        items(totalSlots) { index ->
            if (index >= firstDayOfWeek) {
                val dayOfMonth = index - firstDayOfWeek + 1
                val date = currentMonth.atDay(dayOfMonth)

                val isStart = date == selectedRange.startDate
                val isEnd = date == selectedRange.endDate
                val isInRange = selectedRange.startDate != null &&
                        selectedRange.endDate != null &&
                        date.isAfter(selectedRange.startDate) &&
                        date.isBefore(selectedRange.endDate)

                DateCell(
                    date = date,
                    isSelectedStart = isStart,
                    isSelectedEnd = isEnd,
                    isInRange = isInRange,
                    onClick = { onDateSelected(date) }
                )
            } else {
                Spacer(modifier = Modifier.aspectRatio(1f))
            }
        }
    }
}

@Composable
private fun DateCell(
    date: LocalDate,
    isSelectedStart: Boolean,
    isSelectedEnd: Boolean,
    isInRange: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Press feedback scale
    val clickScale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "clickScale"
    )

    // Selection entrance scale animation
    val selectionScale = remember { Animatable(1f) }
    LaunchedEffect(isSelectedStart, isSelectedEnd) {
        if (isSelectedStart || isSelectedEnd) {
            selectionScale.animateTo(
                targetValue = 1.25f,
                animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
            )
            selectionScale.animateTo(
                targetValue = 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    val isEndpoint = isSelectedStart || isSelectedEnd

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        // Range Highlight Capsule Background
        if (isInRange || isSelectedStart || isSelectedEnd) {
            val shape = when {
                isSelectedStart && isSelectedEnd -> CircleShape
                isSelectedStart -> RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50)
                isSelectedEnd -> RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50)
                else -> RoundedCornerShape(0)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(shape)
                    .background(
                        if (isInRange) DatePickerTheme.RangeHighlight
                        else Color.Transparent
                    )
            )
        }

        // Date Circle Indicator
        Box(
            modifier = Modifier
                .size(36.dp)
                .graphicsLayer {
                    scaleX = clickScale * if (isEndpoint) selectionScale.value else 1f
                    scaleY = clickScale * if (isEndpoint) selectionScale.value else 1f
                }
                .then(
                    if (isEndpoint) {
                        Modifier.shadow(
                            elevation = 10.dp,
                            shape = CircleShape,
                            spotColor = DatePickerTheme.AmberGlow
                        )
                    } else Modifier
                )
                .clip(CircleShape)
                .background(
                    brush = when {
                        isEndpoint -> Brush.linearGradient(
                            colors = listOf(
                                DatePickerTheme.GradientStart,
                                DatePickerTheme.AmberGlow
                            )
                        )

                        else -> Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.Transparent)
                        )
                    }
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 14.sp,
                fontWeight = if (isEndpoint) FontWeight.ExtraBold else FontWeight.Normal,
                color = when {
                    isEndpoint -> DatePickerTheme.DeepBackground
                    isInRange -> DatePickerTheme.PrimaryGold
                    else -> DatePickerTheme.TextLight
                }
            )
        }
    }
}