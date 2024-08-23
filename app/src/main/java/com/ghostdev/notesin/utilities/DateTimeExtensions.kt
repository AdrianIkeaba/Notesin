package com.ghostdev.notesin.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.toFormattedDateTime(): String {
    // Define the desired date-time format
    val formatter = SimpleDateFormat("HH:mm, dd/MM/yy", Locale.getDefault())
    // Convert the Long (milliseconds) to a Date object
    val date = Date(this)
    // Format the date and return the string
    return formatter.format(date)
}