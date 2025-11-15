package com.example.workoutapp.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workoutapp.data.database.AppDatabase
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.entities.history.HistoryWorkoutEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class HistoryWorkoutDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: HistoryWorkoutDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Create an in-memory database (clears after tests)
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        dao = database.historyWorkoutDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveWorkout() = runTest {
        // Given
        val workout = HistoryWorkoutEntity(
            id = "1",
            name = "Test Workout",
            duration = Duration.ofMinutes(60), // 1 hour in millis
            date = LocalDate.now(),
            note = "Test note",
            isDeleted = false,
            isSynced = false
        )

        // When
        dao.insert(workout)
        val result = dao.getAllHistoryWorkoutsSnapshot()

        // Then
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Test Workout", result[0].name)
    }

    @Test
    fun getAllHistoryWorkouts_excludesDeletedWorkouts() = runTest {
        // Given
        val workout1 = HistoryWorkoutEntity(
            id = "1",
            name = "Active Workout",
            duration = Duration.ofMinutes(60),
            date = LocalDate.now(),
            note = "",
            isDeleted = false,
            isSynced = false
        )
        val workout2 = HistoryWorkoutEntity(
            id = "2",
            name = "Deleted Workout",
            duration = Duration.ofMinutes(60),
            date = LocalDate.now(),
            note = "",
            isDeleted = true,
            isSynced = false
        )

        // When
        dao.insert(workout1)
        dao.insert(workout2)
        val result = dao.getAllHistoryWorkouts().first()

        // Then
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Active Workout", result[0].name)
    }

    @Test
    fun markAsDeleted_setsDeletedFlag() = runTest {
        // Given
        val workout = HistoryWorkoutEntity(
            id = "1",
            name = "Workout",
            duration = Duration.ofMinutes(60),
            date = LocalDate.now(),
            note = "",
            isDeleted = false,
            isSynced = false
        )
        dao.insert(workout)

        // When
        dao.markAsDeleted("1")
        val result = dao.getAllHistoryWorkouts().first()

        // Then
        Assert.assertEquals(0, result.size) // Deleted workouts are filtered out
    }

    @Test
    fun flowEmitsUpdatesWhenDataChanges() = runTest {
        // Given
        val workout = HistoryWorkoutEntity(
            id = "1",
            name = "Workout",
            duration = Duration.ofMinutes(60),
            date = LocalDate.now(),
            note = "",
            isDeleted = false,
            isSynced = false
        )

        // When - first emission (empty)
        val initialResult = dao.getAllHistoryWorkouts().first()
        Assert.assertEquals(0, initialResult.size)

        // When - insert and get second emission
        dao.insert(workout)
        val updatedResult = dao.getAllHistoryWorkouts().first()

        // Then
        Assert.assertEquals(1, updatedResult.size)
    }
}