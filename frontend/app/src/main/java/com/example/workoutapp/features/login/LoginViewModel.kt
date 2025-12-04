package com.example.workoutapp.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.domain.usecases.LoginWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed class representing the state of the login process.
 */
sealed class LoginState {
    /** Initial state before any login attempt */
    object Idle : LoginState()

    /** Login in progress */
    object Loading : LoginState()

    /** Login succeeded with user name */
    data class Success(val name: String) : LoginState()

    /** Login failed with error message */
    data class Error(val message: String) : LoginState()
}

/**
 * ViewModel for the login screen, handling Google OAuth authentication.
 *
 * Manages the login flow including exchanging the authorization code for credentials,
 * persisting user data, and exposing login state to the UI.
 *
 * @property googleUseCase Use case for Google OAuth login
 * @property preferences DataStore for persisting JWT token and user data
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleUseCase: LoginWithGoogleUseCase,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    /**
     * Observable login state for the UI.
     */
    val loginState = _loginState.asStateFlow()

    /**
     * Authenticates the user via Google OAuth.
     *
     * Exchanges the authorization code for user credentials, saves them to DataStore,
     * and updates the login state. Calls the callback with the user's name on success.
     *
     * @param code Authorization code from Google Sign-In
     * @param onResult Callback invoked with user name on successful login
     */
    fun loginWithGoogle(code: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = googleUseCase.invoke(code)

                preferences.saveAuthData(response.userId, response.token, response.name)
                _loginState.value = LoginState.Success(response.name)
                onResult(response.name)
            } catch (e: Exception) {
                e.printStackTrace()
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }
}