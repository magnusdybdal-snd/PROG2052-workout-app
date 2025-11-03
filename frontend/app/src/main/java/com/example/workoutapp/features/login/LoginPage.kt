package com.example.workoutapp.features.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.workoutapp.R
import com.example.workoutapp.core.core_navigation.Routes
import kotlinx.coroutines.delay


const val welcomeDelay: Long = 2000
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clientId = stringResource(R.string.google_client_id)

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestServerAuthCode(clientId)
        .requestEmail()
        .build()
    // Deprecated function, as backend need a temp oauth code
    @Suppress("DEPRECATION")
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // state from viewmodel
    val loginState by viewModel.loginState.collectAsState()

    val launcher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.serverAuthCode?.let { code ->
                    viewModel.loginWithGoogle(code) {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Workout App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            when (loginState) {
                is LoginState.Loading -> CircularProgressIndicator()
                else -> Button(
                    onClick = { launcher.launch(googleSignInClient.signInIntent) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign in with Google")
                }
            }

            Spacer(Modifier.height(24.dp))

            if (loginState is LoginState.Error) {
                Text(
                    text = "Login failed: ${(loginState as LoginState.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            if (loginState is LoginState.Success) {
                val name = (loginState as LoginState.Success).name
                Text(
                    text = "Welcome, $name",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                // Side effect for delaying hello message
                LaunchedEffect(loginState) {
                    delay(welcomeDelay) // 2 seconds
                    navController.navigate(Routes.WORKOUT) {
                        popUpTo(0)
                    }
                }
            }
        }
    }
}