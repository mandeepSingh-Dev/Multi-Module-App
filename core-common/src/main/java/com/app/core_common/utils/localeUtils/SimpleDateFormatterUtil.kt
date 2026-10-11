package com.app.core_common.utils.localeUtils

import android.annotation.SuppressLint
import android.util.Log
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.DateFormats.DATE_FORMAT_5
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
fun String.convertToDateFormat(
    fromFormat: String? = DateFormats.DATE_FORMAT_5,
    toFormat: String? = DateFormats.DATE_FORMAT_6,
    fallBackFromFormat: String? = DateFormats.DATE_FORMAT_2
): String? {
    return try {

        val inputDateFormat = SimpleDateFormat(fromFormat)
        val outputDateFormat = SimpleDateFormat(toFormat)

        val date = inputDateFormat.parse(this)
        outputDateFormat.format(date)
    } catch (e: Exception) {
        try {
            val inputDateFormat = SimpleDateFormat(fallBackFromFormat)
            val outputDateFormat = SimpleDateFormat(toFormat)

            val date = inputDateFormat.parse(this)
            outputDateFormat.format(date)
        } catch (e: Exception) {
            null
        }
    }

}

fun String.normalizeHindiMonths(): String {
    return this
        .replace("जनवरी", "जनवरी")
        .replace("फ़रवरी", "फरवरी")
        .replace("फ़रवरी", "फरवरी")
        .replace("मार्च", "मार्च")
        .replace("अप्रैल", "अप्रैल")
        .replace("मई", "मई")
        .replace("जून", "जून")
        .replace("जुलाई", "जुलाई")
        .replace("अगस्त", "अगस्त")
        .replace("सितंबर", "सितम्बर")
        .replace("अक्टूबर", "अक्टूबर")
        .replace("नवंबर", "नवम्बर")
        .replace("दिसंबर", "दिसम्बर")
}

@SuppressLint("SimpleDateFormat")
fun Long.convertToDateFormat(
    toFormat: String? = DateFormats.DATE_FORMAT_6,
    isUtc: Boolean = false
): String? {
    return try {
        val outputDateFormat = SimpleDateFormat(toFormat)
        if (isUtc) {
            outputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        }
        outputDateFormat.format(this)

    } catch (e: Exception) {
        Log.d("dlvmkmv", e.message.toString())
        null // Handle the error appropriately
    }

}


fun String.get_Formatted_UTC_Time(
    fromFormat: String = DATE_FORMAT_5,
    toFormat: String = "HH:mm:ss"
): String? {

    val isoFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */

        val date = isoFormat.parse(this)
        val timeFormat = SimpleDateFormat(toFormat, Locale.getDefault())
        timeFormat.timeZone = TimeZone.getDefault() // Use local timezone
        timeFormat.format(date)
    } catch (e: Exception) {
        Log.d("fvmkfbmnkgbg", e.message.toString())
        e.printStackTrace()
        null // Handle parsing error
    }
}


fun String.getMillisecondsFromUTCTime(fromFormat: String = DateFormats.DATE_FORMAT_5): Long? {

    val isoFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */

        val date = isoFormat.parse(this)
        date.time

    } catch (e: Exception) {
        Log.d("fvmkfbmnkgbg", e.message.toString())
        e.printStackTrace()
        null // Handle parsing error
    }
}

fun String.getMillisecondsFromHourMinute(
    fromFormat: String = "hh:mm",
    toFormat: String = "HH:mm:ss"
): Long? {

    val isoFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */

        val date = isoFormat.parse(this)
        date.time

    } catch (e: Exception) {
        Log.d("fvmkfbmnkgbg", e.message.toString())
        e.printStackTrace()
        null // Handle parsing error
    }
}

fun String.getMillisecondsFromHourMinuteWithoutUTC(fromFormat: String = "hh:mm"): Long? {

    val isoFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
//    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */

        val date = isoFormat.parse(this)
        date.time

    } catch (e: Exception) {
        Log.d("fvmkfbmnkgbg", e.message.toString())
        e.printStackTrace()
        null // Handle parsing error
    }
}

fun String?.convertTimeToMilliseconds(): Long? {

    return if (!this.isNullOrEmpty()) {
        try {
            // Define the date format
            val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            // Parse the time string into a Date object
            val date = dateFormat.parse(this)

            // Use a Calendar to get the hours, minutes, and seconds
            val calendar = Calendar.getInstance().apply {
                this.time = date
            }

            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val seconds = calendar.get(Calendar.SECOND)

            // Calculate milliseconds since the start of the day
            val milliseconds = (hours * 3600000) + (minutes * 60000) + (seconds * 1000)

            milliseconds.toLong()
        } catch (e: Exception) {
            null
        }
    } else {
        null
    }
}


fun String.getMillisecondsFromDateWithoutUTC(
    fromFormat: String = DateFormats.DATE_FORMAT_2,
    locale: Locale = Locale.getDefault()
): Long? {

    val isoFormat = SimpleDateFormat(fromFormat, locale)
//    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */
        val date = isoFormat.parse(this)
        date.time
    } catch (e: Exception) {
//        Log.d("fkbnwe24232kfnb", "getMillisecondsFromDateWithoutUTC ${e.message}" )
        println("getMillisecondsFromDateWithoutUTC Error" + e.message)
        null // Handle parsing error
    }
}

fun String.getMillisecondsFromDateWithUTC(
    fromFormat: String = DateFormats.DATE_FORMAT_12,
    locale: Locale = Locale.getDefault()
): Long? {

    val isoFormat = SimpleDateFormat(fromFormat, locale)
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */
        val date = isoFormat.parse(this)
        date.time
    } catch (e: Exception) {
//        Log.d("fkbnwe24232kfnb", "getMillisecondsFromDateWithoutUTC ${e.message}" )
        println("getMillisecondsFromDateWithoutUTC Error" + e.message)
        null // Handle parsing error
    }
}

fun Long.getHourMinuteFromMilliseconds(toFormat: String = DateFormats.TIME_FORMAT_1): String? {

    return try {
        val simpleDateFormat = SimpleDateFormat(toFormat)
        val date = simpleDateFormat.format(Date(this))
        date
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getHourFromTimeFormat(fromFormat: String? = DateFormats.TIME_FORMAT_1): Int? {
    return try {
        val inputSimpleDateFormat = SimpleDateFormat(fromFormat)
        val date = inputSimpleDateFormat.parse(this)
        val outputSimpleDateFormat = SimpleDateFormat("hh")
        val hour = outputSimpleDateFormat.format(date)
        hour.toInt()
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getMinuteFromTimeFormat(fromFormat: String? = DateFormats.TIME_FORMAT_1): Int? {
    return try {
        val inputSimpleDateFormat = SimpleDateFormat(fromFormat)
        val date = inputSimpleDateFormat.parse(this)
        val outputSimpleDateFormat = SimpleDateFormat("mm")
        val minute = outputSimpleDateFormat.format(date)
        minute.toInt()
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getSecondsFromTimeFormat(fromFormat: String? = DateFormats.TIME_FORMAT_1): Int? {
    return try {
        val inputSimpleDateFormat = SimpleDateFormat(fromFormat)
        val date = inputSimpleDateFormat.parse(this)
        val outputSimpleDateFormat = SimpleDateFormat("ss")
        val minute = outputSimpleDateFormat.format(date)
        minute.toInt()
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getAMPMFromTimeFormat(fromFormat: String? = DateFormats.TIME_FORMAT_1): String? {
    return try {
        val inputSimpleDateFormat = SimpleDateFormat(fromFormat)
        val date = inputSimpleDateFormat.parse(this)
        val outputSimpleDateFormat = SimpleDateFormat("a") // returns "AM" or "PM"
        outputSimpleDateFormat.format(date).uppercase()
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getDayFromDateFormat(fromFormat: String? = DateFormats.DATE_FORMAT_5): Int? {
    return try {
        val inputSimpleDateFormat = SimpleDateFormat(fromFormat)
        val date = inputSimpleDateFormat.parse(this)
        val outputSimpleDateFormat = SimpleDateFormat("dd")
        val day = outputSimpleDateFormat.format(date)
        day.toInt()
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getAmPmFromTimeFormat(fromFormat: String? = DateFormats.TIME_FORMAT_1): Int? {
    return try {
        val inputSimpleDateFormat = SimpleDateFormat(fromFormat)
        val date = inputSimpleDateFormat.parse(this)
        val outputSimpleDateFormat = SimpleDateFormat("mm")
        val minute = outputSimpleDateFormat.format(date)
        minute.toInt()
    } catch (e: Exception) {
        null
    }
}


fun Long.getHourFromMilliseconds(toFormat: String = "HH"): String? {

    return try {
        val simpleDateFormat = SimpleDateFormat(toFormat)
        val date = simpleDateFormat.format(Date(this))
        date
    } catch (e: Exception) {
        null
    }
}


fun Long.toMonthLabel(): String? {
    return try {
        SimpleDateFormat("MMM").format(Date(this))
    } catch (e: Exception) {
        null
    }
}

fun String.getYearFromUTCTime(
    fromFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
    toFormat: String = "YYYY"
): String? {

    val isoFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        /* source : "2024-02-19T17:10:27.492343+05:30"  EXAMPLE */

        val date = isoFormat.parse(this)
        val timeFormat = SimpleDateFormat(toFormat, Locale.getDefault())
        timeFormat.timeZone = TimeZone.getDefault() // Use local timezone
        timeFormat.format(date)
    } catch (e: Exception) {
        Log.d("fvmkfbmnkgbg", e.message.toString())
        e.printStackTrace()
        null // Handle parsing error
    }
}


fun String.splitDate() = this.split("T").first()

fun String?.getDMYFieldsFromDate(fromFormat: String = DateFormats.DATE_FORMAT_8): Triple<Int, Int, Int> {

    val date = this?.getMillisecondsFromDateWithoutUTC(fromFormat = fromFormat)
        ?: Calendar.getInstance().timeInMillis

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH).plus(1)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    return Triple(first = dayOfMonth, second = month, third = year)
}

fun String?.getDMYFieldsFromDateFromUTC(fromFormat: String = DateFormats.DATE_FORMAT_12): Triple<Int, Int, Int> {

    val date = this?.getMillisecondsFromDateWithUTC(fromFormat = fromFormat)
        ?: Calendar.getInstance().timeInMillis

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH).plus(1)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    return Triple(first = dayOfMonth, second = month, third = year)
}

fun String?.extractTimeIn12HourFormat(): String? {

    if (this.isNullOrEmpty()) return null

    var period = "AM"


    // Check for Hindi time indicators
    if (this.contains("शाम") || this.contains("दोपहर")) {
        period = "PM"
    } else if (this.contains("सुबह")) {
        period = "AM"
    }

    // Define the regex pattern for HH:mm
    val regex = """(\d{1,2}):(\d{2})""".toRegex()
    val matchResult = regex.find(this)

    if (matchResult != null) {
        val timeString = matchResult.value // e.g., "7:39"
        val components = timeString.split(":")

        if (components.size == 2) {
            val hour = components[0].toIntOrNull() ?: 0
            val minute = components[1]

            // Format hour to 2 digits (e.g., 7 becomes "07")
            val formattedHour = String.format("%02d", hour)

            return "$formattedHour:$minute $period"
        }
    }

    return null
}

fun String?.calculateAge(): Int {
    if (isNullOrEmpty()) return 0
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dob = LocalDate.parse(this, formatter)
        Period.between(dob, LocalDate.now()).years
    } catch (e: Exception) {
        0
    }
}

/*
* For normalizing गुरु सितंबर 27 1984 types of date
* For normalizing सोम जून 22 2026 दोपहर 1:34 बजे types of date
* */
fun String?.normalizeHindiDate(): String? {
    return this?.substringAfter(" ")?.split(" ")
        ?.mapIndexed { index, it ->
            if (index == 0) {
                it.normalizeHindiMonths().trim()
            } else {
                it.trim()
            }
        }?.joinToString(" ")?.trim()
}

/*
* For normalizing गुरु सितंबर 27 1984 types of date
* For normalizing सोम जून 22 2026 दोपहर 1:34 बजे types of date
* */
fun String?.normalizeHindiDate2(): String? {

    val str = this
        ?.replace("बहुत सवेरे", "AM")
        ?.replace("सुबह", "AM")
        ?.replace("दोपहर", "PM")
        ?.replace("शाम", "PM")
        ?.replace("रात", "PM")
        ?.replace(" बजे", "") ?: ""

    return str.substringAfter(" ").split(" ")
        .mapIndexed { index, string ->
            if (index == 0) {
                string.normalizeHindiMonths()
            } else {
                string
            }
        }.joinToString(" ")


}

fun getSupportedMonthsList(locale: Locale): List<String?> {
    val isoFormat = SimpleDateFormat(DateFormats.DATE_FORMAT_25, locale)
    return isoFormat.dateFormatSymbols.months.toList()

}


@SuppressLint("SimpleDateFormat")
fun String?.getTodayYesterdayElseData(): String? {
    if (isNullOrEmpty()) return null
    val sdf = SimpleDateFormat(DateFormats.DATE_FORMAT_12, Locale.getDefault())
    val outputFormat = SimpleDateFormat(DateFormats.DATE_FORMAT_4, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val date = sdf.parse(this)


    val today = Calendar.getInstance()

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val transactionCal = Calendar.getInstance().apply {
        time = date
    }

    return when {
        isSameDay(transactionCal, today) -> "Today"

        isSameDay(transactionCal, yesterday) -> "Yesterday"

        else -> outputFormat.format(date)
    }
}

@SuppressLint("SimpleDateFormat")
fun String?.getTodayYesterdayElseDate2(): String? {

    if (isNullOrEmpty()) return null

    val inputFormat = SimpleDateFormat(
        DateFormats.DATE_FORMAT_12,
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val outputTimeFormat = SimpleDateFormat(
        "h:mm a",
        Locale.getDefault()
    )

    val outputDateTimeFormat = SimpleDateFormat(
        "d MMM, yyyy, h:mm a",
        Locale.getDefault()
    )

    val date = inputFormat.parse(this) ?: return null

    val today = Calendar.getInstance()

    val yesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }

    val transactionCal = Calendar.getInstance().apply {
        time = date
    }

    return when {
        isSameDay(transactionCal, today) -> {
            outputTimeFormat.format(date)
        }

        isSameDay(transactionCal, yesterday) -> {
            "Yesterday ${outputTimeFormat.format(date)}"
        }

        else -> {
            outputDateTimeFormat.format(date)
        }
    }
}

private fun isSameDay(
    date1: Calendar,
    date2: Calendar
): Boolean {
    return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
            date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)
}

@SuppressLint("SimpleDateFormat")
fun String?.toViewedAgo(): String {
    if (this.isNullOrEmpty()) return ""

    return try {
        val formatter = SimpleDateFormat(
            DateFormats.DATE_FORMAT_5,
            Locale.getDefault()
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val date = formatter.parse(this) ?: return ""

        val diffMillis = System.currentTimeMillis() - date.time

        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            seconds < 60 -> "Viewed just now"
            minutes < 60 -> "Viewed $minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            hours < 24 -> "Viewed $hours ${if (hours == 1L) "hour" else "hours"} ago"
            days < 30 -> "Viewed $days ${if (days == 1L) "day" else "days"} ago"
            days < 365 -> {
                val months = days / 30
                "Viewed $months ${if (months == 1L) "month" else "months"} ago"
            }

            else -> {
                val years = days / 365
                "Viewed $years ${if (years == 1L) "year" else "years"} ago"
            }
        }
    } catch (e: Exception) {
        ""
    }
}

fun String?.getTimeRemaining(): String? {
    if(this.isNullOrEmpty()) return null

    try {
        val format = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )
        format.timeZone = TimeZone.getTimeZone("UTC")

        val targetDate = format.parse(this) ?: return "00d : 00h : 00m"
        val remainingMillis = targetDate.time - System.currentTimeMillis()

        if (remainingMillis <= 0) {
            return "00d : 00h : 00m"
        }

        val days = TimeUnit.MILLISECONDS.toDays(remainingMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60

        return String.format(
            Locale.getDefault(),
            "%02dd : %02dh : %02dm",
            days,
            hours,
            minutes
        )
    }catch (e: Exception){
        return null
    }
}

fun String?.toLocalDateOrNull(): LocalDate? {
    return runCatching {
        this?.let {
            Instant.parse(it)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
        }
    }.getOrNull()
}

