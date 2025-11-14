package com.example.workoutapp.features.auth

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.core.utils.isTokenExpired
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.domain.models.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Manages authentication state for the whole app
 *
 * Observes token changes and provides a single source of truth
 * for authentication status.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        observeAuthState()
    }

    /**
     * Observes token changes and updates auth state accordingly
     * This runs once when the viewmodel is created, not on every recomposition
     */
    private fun observeAuthState() {
        viewModelScope.launch {
            preferences.token.collect { token ->
                _authState.value = when {
                    token.isNullOrBlank() || isTokenExpired(token) -> AuthState.Unauthenticated
                    else -> AuthState.Authenticated
                }
            }
        }
    }

    /**
     * Logs out the user by clearing auth data
     */
    fun logout() {
        viewModelScope.launch {
            preferences.clearAuthData()
        }
    }
}