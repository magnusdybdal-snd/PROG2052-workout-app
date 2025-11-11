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

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val name: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleUseCase: LoginWithGoogleUseCase,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()
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