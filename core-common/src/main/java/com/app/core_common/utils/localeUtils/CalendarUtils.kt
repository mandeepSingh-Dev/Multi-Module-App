package com.app.core_common.utils.localeUtils

import android.annotation.SuppressLint
import android.util.Log
import com.app.core_common.utils.DateFormats
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object CalendarUtil {
    fun getCurrentYear(): Int {
        var calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }
    fun getCurrentMonth(): Int {
        var calendar = Calendar.getInstance()

        return calendar.get(Calendar.MONTH) + 1
    }

    fun getCurrentMonthLabel(): String? {
        var calendar = Calendar.getInstance()
        return calendar.timeInMillis.toMonthLabel()
    }

    fun getCurrentDay(): Int {
        var calendar = Calendar.getInstance()
        return  calendar.get(Calendar.DAY_OF_MONTH) + 1
    }

    fun Long.getYearFromMilliseconds(): Int {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.YEAR)
    }
    fun Long.getMonthFromMilliseconds(): Int {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.MONTH) + 1
    }


    fun getCurrentDate(format : String = DateFormats.DATE_FORMAT_8): String? {
        val sp = SimpleDateFormat(format)
        return sp.format(Date().time)
    }
    fun getTomorrowDate(format : String = DateFormats.DATE_FORMAT_8): String? {
        val sp = SimpleDateFormat(format)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        return sp.format(cal.timeInMillis)
    }

    fun isDateToday(timeMs : Long): Boolean? {
        return try {
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)



            calendar.timeInMillis = timeMs
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            Log.d("Fblmfkbmf","$currentDay$currentMonth$currentYear$day$month$year")

            if (day == currentDay && month == currentMonth && year == currentYear) {
                true
            } else false
        }catch (e:Exception){
            null
        }

    }
    fun isDateTomorrow(timeMs : Long): Boolean? {
        return try {
            val calendar = Calendar.getInstance()
            val tomorrowDay = calendar.get(Calendar.DAY_OF_MONTH).plus(1)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)



            calendar.timeInMillis = timeMs
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            Log.d("Fblmfkbmf","$tomorrowDay$currentMonth$currentYear$day$month$year")

            if (day == tomorrowDay && month == currentMonth && year == currentYear) {
                true
            } else false
        }catch (e:Exception){
            null
        }

    }

    fun isDateYesterday(timeMs : Long) : Boolean?{
        return try {
            val calendar = Calendar.getInstance()
            val yesterday = calendar.get(Calendar.DAY_OF_MONTH) - 1
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            calendar.timeInMillis = timeMs
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            if (day == yesterday && month == currentMonth && year == currentYear) {
                true
            } else false
        }catch (e:Exception){
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun isDateNearThisWeek(timeMs : Long): Boolean? {

        return try {
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            //Decreasing day value by removing yesterday and today.
            val dayExpectTodayYesterday = currentDay - 2

            val dayRange = dayExpectTodayYesterday - 4

            calendar.timeInMillis = timeMs
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)


            if ((day in dayRange..currentDay) && month == currentMonth && year == currentYear) {
                Log.d(
                    "fvlmf34343kvmf",
                    SimpleDateFormat("E").format(Date(timeMs)).toString() + "   If"
                )
                true
            } else {
                Log.d("fvlmf34343kvmf", timeMs.toString() + "   else")
                false
            }
        }catch (e:Exception){
            null
        }
    }


    fun getFormattedTimeDayAccordingly(timeString : String?): String? {
        return try {
            val timeMs = timeString?.getMillisecondsFromUTCTime()
            val formattedTime = timeMs?.let { time ->
                if (isDateToday(time) == true) {
                    Log.d("JGNJFNGJF_Time", timeString.toString())
                    timeString.get_Formatted_UTC_Time(toFormat = DateFormats.TIME_FORMAT_1)
                } else if (isDateYesterday(time) == true) {
                    "Yesterday"
                } else if (isDateNearThisWeek(time) == true) {

                    timeString.get_Formatted_UTC_Time(toFormat = DateFormats.DATE_FORMAT_3)

                } else {

                    timeString.get_Formatted_UTC_Time(toFormat = DateFormats.DATE_FORMAT_2)

                }
            }
            return formattedTime
        }catch (e:Exception){
            null
        }

    }




    fun getCurrentYEAR() = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.YEAR)
    fun getCurrentMONTH() = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.MONTH)
    fun getCurrentDAY() = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.DAY_OF_MONTH)

    fun getCurrentDayInMilliseconds(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.DAY_OF_MONTH, getCurrentDay()-1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)

        return calendar.timeInMillis
    }

    fun getCurrentHourMinuteInMilliseconds(): Long? {
        val currentHrMnString = Date().time.getHourMinuteFromMilliseconds(toFormat = DateFormats.TIME_FORMAT_1)

        Log.d("lbmkmbf",currentHrMnString.toString())
        val currentTimeInMs = currentHrMnString?.getMillisecondsFromHourMinuteWithoutUTC(fromFormat = DateFormats.TIME_FORMAT_1)

        return currentTimeInMs

    }


    fun getTimeInMsFromYearMonthDay(year : Int, month : Int, day: Int) : Long?{

        return try {
            val calendar = android.icu.util.Calendar.getInstance()
            calendar.set(year, month, day)

            calendar.timeInMillis
        }catch (e:Exception){
            null
        }
    }

    fun getTimeInMsFromHourMinute(hour : Int, minute : Int) : Long?{

        return try {
            val calendar = android.icu.util.Calendar.getInstance()
            calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, hour)
            calendar.set(android.icu.util.Calendar.MINUTE, minute)

            calendar.timeInMillis
        }catch (e:Exception){
            null
        }
    }


    /**
     * Calculating total minutes from given/current time by adding days, hours, minutes.*/
    fun getMinutesFromHoursMinutes(hours: Long, minutes: Long): Long? {
        return try {

            val m1 =TimeUnit.HOURS.toMinutes(hours)
            val m2 =TimeUnit.MINUTES.toMinutes(minutes)

            val totalMin =  m1 + m2
            totalMin
        }catch (e:Exception){
            null
        }
    }

    fun compareStartEndTimeModify(startTimeMs : Long, endTimeMs : Long): Long {

        return try {
            if (endTimeMs < startTimeMs) {
                Log.d("fblmfkmvf", "endTime is small from startTime means endTime is of nextDate.")

                val oneDayMilliseconds = TimeUnit.DAYS.toMillis(1)

                Log.d("blgkbogbg", (endTimeMs + oneDayMilliseconds).toString())
                val endTimeExtendedWithOneDay = endTimeMs + oneDayMilliseconds
                endTimeExtendedWithOneDay
            } else {
                Log.d("fblmfkmvf", "startTime is small  means endTime is of current day.")
                endTimeMs
            }
        }catch (e:Exception){
            endTimeMs
        }

    }


    fun getExtendedTime(time : String?, timeFormat : String = DateFormats.TIME_FORMAT_1): Long? {

        return  try {
            Log.d("vmkvmdkvmdfv", time.toString() + " time")

            val hour = time?.getHourFromTimeFormat(fromFormat = timeFormat) ?: 0
            val minute = time?.getMinuteFromTimeFormat(fromFormat = timeFormat) ?: 0

            Log.d("vmkvmdkvmdfv", hour.toString() + " hour")
            Log.d("vmkvmdkvmdfv", minute.toString() + "minute")


            val calr = Calendar.getInstance()
            Log.d("vkfmvfkm", calr.timeInMillis.toString())
            Log.d("vkfmvfkm", minute.toString())

            Log.d("vmkvmdkvmdfv", calr.get(Calendar.HOUR_OF_DAY).toString() + " HOUR")
            Log.d("vmkvmdkvmdfv", calr.get(Calendar.MINUTE).toString() + "MINUTE")

            val currentHour = calr.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calr.get(Calendar.MINUTE)

            Log.d("lfmvkmvfv",currentHour.toString() + " currentHour")
            Log.d("lfmvkmvfv",currentMinute.toString() + " currentMinute")

            //If hour smaller than current-hour means the time is of next day.
            //e.g - hour= 6pm and current-hour= 7pm.
            val dayIncrement = if (hour < currentHour) {
                1
            }
            //If hour == current-hour
            //e.g hour= 6pm and current-hour= 6pm
            else if (hour == currentHour) {
                //If time= 6:10 pm and current-time= 6:15 pm
                //means time is of next day.
                if (minute <= currentMinute) {
                    1
                } else {
                    0
                }
            }
            //when hour= 7pm and current-hour= 6pm
            //then the day will be same.
            else if (hour > currentHour) {
                0
            } else {
                0
            }

            Log.d("dvlmkvmf",dayIncrement.toString() + "dayIncrement")
            Log.d("dvlmkvmf",hour.toString()+ "hour")
            Log.d("dvlmkvmf",minute.toString()+ "minute")

            calr.add(Calendar.DAY_OF_MONTH, dayIncrement)
            calr.set(Calendar.HOUR_OF_DAY, hour)
            calr.set(Calendar.MINUTE, minute)
            calr.set(Calendar.SECOND, 0)
            calr.set(Calendar.MILLISECOND, 0)

            calr.timeInMillis
        }catch (e:Exception){
            null
        }
    }

    //This function used to get relative time in 1 day ago, 20 hours ago etc from milliseconds
    fun getRelativeTimeFromMilliseconds(timeInMS : Long?): String? {

        return try {
            val relativeTime = timeInMS?.let {

                val currentTime = Date().time
                val diffTime = currentTime - timeInMS

                Log.d("flbmkfmv", diffTime.toString())

                val seconds = TimeUnit.MILLISECONDS.toSeconds(diffTime)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffTime)
                val hours = TimeUnit.MILLISECONDS.toHours(diffTime)
                val days = TimeUnit.MILLISECONDS.toDays(diffTime)

                Log.d(
                    "flfbmkfm",
                    "seconds: $seconds, seconds: $minutes, seconds: $hours, seconds: $days, "
                )

                when {
                    seconds < 60 -> "just now"
                    minutes < 60 -> "$minutes minutes ago"
                    hours < 24 -> "$hours hours ago"
                    days < 30 -> "$days days ago"
                    days < 365 -> "${days / 30} months ago"
                    else -> "${days / 365} years ago"
                }

            }
            relativeTime
        }catch (e:Exception){
            null
        }

    }
/*     "start_date":"2024-09-09T00:00:00.000Z","end_date":"2024-09-30T00:00:00.000Z"
    fun isCurrentDateInRange(startDate : String?, endDate : String?){
        try {
            if (!startDate.isNullOrEmpty() && !endDate.isNullOrEmpty()) {

            }
        }catch (e:Exception){

        }
    } */

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeInUTCFormat() : String?{
        val sdf = SimpleDateFormat(DateFormats.DATE_FORMAT_5)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val time = sdf.format(Date().time)
        return time
    }


    fun Long?.getDifferenceDays(startTime : Long?) : Long?{
        try {
            if (this == null) return null
            if (startTime == null) return null
            if (startTime > this) return null

            val diffMillis = this - startTime
            val diffDays = diffMillis / (1000 * 60 * 60 * 24)
            return diffDays + 1
        }catch (e: Exception){
            return null
        }
    }





}

fun convertTo24HourFormat(hh : Int?, mm : Int?, sec : Int?, amPm : String?): String? {

    return runCatching {
        val hours = (hh ?: 0).toString()
        val mins = (mm ?: 0).toString()
        val secs = (sec ?: 0).toString()

        val inputTime = "${hours}:$mins:$secs ${amPm ?: "AM"}"

        val inputFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val date = inputFormat.parse(inputTime)
        outputFormat.format(date)
    }.onSuccess {
        it
    }.onFailure {
            null
        }.getOrNull()
}

fun convertTo24HourFormat(hh : Int?, mm : Int?, amPm : String?): String? {

    return runCatching {
        val hours = (hh ?: 0).toString()
        val mins = (mm ?: 0).toString()

        val inputTime = "${hours}:$mins ${amPm ?: "AM"}"

        val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputTime)
        outputFormat.format(date)
    }.onSuccess {
        it
    }
        .onFailure {
            null
        }.getOrNull()
}

fun getNextDays(limit : Int): List<String> {
    val dateList = mutableListOf<String>()
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(DateFormats.DATE_FORMAT_8, Locale.getDefault())
    repeat(limit) {
        dateList.add(dateFormat.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Move to next day
    }
    return dateList
}

@SuppressLint("SimpleDateFormat")
fun getFieldFromDate(date : String?, field : String = DateFormats.DATE_FORMAT_8) : String? {
    if(date == null) return null
    val inputFormat = SimpleDateFormat(DateFormats.DATE_FORMAT_8, Locale.getDefault())
    val date = inputFormat.parse(date)
    val outputFormat = SimpleDateFormat(field)
    return outputFormat.format(date)
}

fun String?.getFullWeekNameFromShortFrom() : String?{
    if(this.isNullOrEmpty()) return null
    return when{
        this.lowercase().contains("sun") -> "Sunday"
        this.lowercase().contains("mon") -> "Monday"
        this.lowercase().contains("tue") -> "Tuesday"
        this.lowercase().contains("wed") -> "Wednesday"
        this.lowercase().contains("thu") -> "Thursday"
        this.lowercase().contains("fri") -> "Friday"
        this.lowercase().contains("sat") -> "Saturday"
        else -> null
    }
}






//fun getPrevious