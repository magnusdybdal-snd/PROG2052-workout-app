package com.example.workoutapp.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.usecases.LoginWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleUseCase: LoginWithGoogleUseCase
) : ViewModel() {
    fun loginWithGoogle(code: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val response = googleUseCase.invoke(code)


            onResult(response.userId)
        }
    }
}