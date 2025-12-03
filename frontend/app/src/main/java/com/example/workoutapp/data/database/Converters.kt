package com.example.workoutapp.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Type converters for Room database.
 *
 * Room cannot persist complex types like LocalDate, LocalDateTime, Duration, or List<String>
 * directly. These converters translate between Kotlin types and database-friendly primitives
 * (String, Long) for storage.
 *
 * Applied to the database via @TypeConverters annotation in AppDatabase.
 */
class Converters {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // ========== LocalDate Converters ==========

    /**
     * Converts a LocalDate to ISO-8601 string for database storage.
     *
     * @param date The date to convert, null if not set
     * @return ISO-8601 date string (e.g., "2025-12-03"), or null
     */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    /**
     * Converts an ISO-8601 date string back to LocalDate.
     *
     * @param dateString ISO date string from database
     * @return Parsed LocalDate, or null if input is null
     */
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, dateFormatter) }
    }

    // ========== LocalDateTime Converters ==========

    /**
     * Converts a LocalDateTime to ISO-8601 string for database storage.
     *
     * @param dateTime The date-time to convert, null if not set
     * @return ISO-8601 date-time string (e.g., "2025-12-03T14:30:00"), or null
     */
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(dateTimeFormatter)
    }

    /**
     * Converts an ISO-8601 date-time string back to LocalDateTime.
     *
     * @param dateTimeString ISO date-time string from database
     * @return Parsed LocalDateTime, or null if input is null
     */
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }

    // ========== Duration Converters ==========

    /**
     * Converts a Duration to total seconds (Long) for database storage.
     *
     * @param duration The duration to convert, null if not set
     * @return Total seconds as Long, or null
     */
    @TypeConverter
    fun fromDuration(duration: Duration?): Long? {
        return duration?.seconds
    }

    /**
     * Converts seconds (Long) back to Duration.
     *
     * @param seconds Total seconds from database
     * @return Duration object, or null if input is null
     */
    @TypeConverter
    fun toDuration(seconds: Long?): Duration? {
        return seconds?.let { Duration.ofSeconds(it) }
    }

    // ========== List<String> Converters ==========

    /**
     * Converts a List<String> to JSON array for database storage.
     *
     * Used for storing lists like exercise target muscles.
     *
     * @param list The string list to convert, null if not set
     * @return JSON array string (e.g., '["chest","triceps"]'), or "[]"
     */
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.let { Json.encodeToString(it) } ?: "[]"
    }

    /**
     * Converts a JSON array string back to List<String>.
     *
     * @param json JSON array from database
     * @return Parsed list of strings, or empty list on parse error
     */
    @TypeConverter
    fun toStringList(json: String): List<String> {
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}