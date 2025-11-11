package com.example.workoutapp.core.core_navigation

object Routes {
    const val SPLASH = "splash" // Loading state page
    const val HISTORY = "history"
    const val LOGIN = "login"
    const val WORKOUT = "workout"
    const val EXERCISES = "exercises"
    const val NEWTEMP = "newTemp"
    const val EDITTEMP = "editTemp/{tempId}"
    const val WORKTEMP = "workTemp"

    // HistoryDetail
    const val HISTORY_DETAIL = "historyDetail/{workoutId}"
    fun historyDetailPage(workoutId: String) = "historyDetail/$workoutId"
}
