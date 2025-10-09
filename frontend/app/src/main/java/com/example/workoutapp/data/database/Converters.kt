package com.example.workoutapp.data.database

import androidx.room.TypeConverter
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.toDuration

class Converters {

    /**
     * Converts complex data types like LocalDate and Duration into database friendly
     * types for ROOM storage, and back again
     */
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE


    /*
     * LocalDate converters
     */

    // Converts a LocalDate type into a string
    @TypeConverter
    fun fromLocalDate(date: String?): String? {
        return date?.format(dateFormatter)
    }

    // Converts a date as string into a LocalDate type
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, dateFormatter) }
    }

    /*
     * Duration converters
     */

    // Converts a Duration type into seconds (Long)
    @TypeConverter
    fun fromDuration(duration: Duration?): Long? {
        return duration?.seconds
    }

    // Converts seconds (Long) into a duration type
    @TypeConverter
    fun toDuration(seconds: Long?): Duration? {
        return seconds?.let { Duration.ofSeconds(it) }
    }
}