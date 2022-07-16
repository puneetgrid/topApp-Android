package com.example.topApp.utils

import java.text.SimpleDateFormat
import java.util.*


class DatePickerClass {

    companion object {
        val utcTimeZone = TimeZone.getTimeZone("UTC")
    }

    var dateValue: Long? = null
    var timeValue: Long? = null

    fun convertLongToDate(timeInMillis: Long, dateFormat: SimpleDateFormat): String {
        dateFormat.timeZone = TimeZone.getDefault()
        return dateFormat.format(Date(timeInMillis))
    }

    fun convertDateToLong(date: String, dateFormat: SimpleDateFormat): Long {
        val timeInMilliseconds: Long
        try {
            dateFormat.timeZone = TimeZone.getDefault()
            val date1: Date = dateFormat.parse(date)!!
            timeInMilliseconds = date1.time
        } catch (e: Exception) {
            return 0
        }
        return timeInMilliseconds
    }


    /**dateFormat-format in which user get from the backend
     * newDateFormat-format in which user wants*/
    fun getParsedDate(
        dateFormat: SimpleDateFormat,
        date: String,
        newDateFormat: SimpleDateFormat
    ): String {
        var parsedDate = ""
        try {
            dateFormat.timeZone = TimeZone.getDefault()
            val serverDate = dateFormat.parse(date)
            val appDateFormatter = newDateFormat
            appDateFormatter.timeZone = TimeZone.getDefault()
            parsedDate = appDateFormatter.format(serverDate)
        } catch (e: Exception) {
            // Exception in parsing date
        }
        return parsedDate
    }

    fun getParsedTime(
        dateFormat: SimpleDateFormat,
        date: String,
        newDateFormat: SimpleDateFormat
    ): String {
        var parsedDate = ""
        try {
            dateFormat.timeZone = TimeZone.getDefault()
            val serverDate = dateFormat.parse(date)
            val appDateFormatter = newDateFormat
            appDateFormatter.timeZone = TimeZone.getDefault()
            parsedDate = appDateFormatter.format(serverDate)
        } catch (e: Exception) {
            // Exception in parsing date
        }
        return parsedDate
    }

    fun formatter(
        newDateFormat: String
    ): SimpleDateFormat {
        return SimpleDateFormat(newDateFormat, Locale.getDefault())
    }


}


