package com.example.workoutapp.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    /**
     * Converts complex data types like LocalDate and Duration into database friendly
     * types for ROOM storage, and back again
     */
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME


    /*
     * LocalDate converters
     */

    // Converts a LocalDate type into a string
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    // Converts a date as string into a LocalDate type
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, dateFormatter) }
    }

    /*
     * LocalDateTime converters
     */

    // Converts a LocalDateTime type into a string
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(dateTimeFormatter)
    }

    // Converts a date and time as string into a LocalDateType type
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, dateTimeFormatter) }
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

    /*
     * List converters
     */

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.let { Json.encodeToString(it) } ?: "[]"
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}