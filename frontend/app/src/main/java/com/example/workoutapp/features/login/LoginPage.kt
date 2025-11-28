package com.example.workoutapp.features.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_navigation.Routes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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

    val cs = MaterialTheme.colorScheme
    val isDarkTheme = isSystemInDarkTheme()


    Surface(
        modifier = modifier.fillMaxSize(),
        color = cs.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Draws white logo if in darkmode, black logo if not darkmode.
            Image(
                painter =
                    if (isDarkTheme)painterResource(id = R.drawable.pbb_logo_white)
                    else painterResource(id = R.drawable.pbb_logo_black),
                contentDescription = "PowerLog Logo",
                modifier = Modifier.size(350.dp),
            )

            Text(
                text = "PowerLog",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = cs.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Log in to use PowerLog",
                fontSize = 16.sp,
                color = cs.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            when (loginState) {
                is LoginState.Loading -> CircularProgressIndicator()

                is LoginState.Success -> {} // hides button
                else -> Button(
                    onClick = { launcher.launch(googleSignInClient.signInIntent) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = cs.tertiary
                    )
                ) {
                    Text(
                        text = "Sign in with Google",
                        color = cs.onTertiary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (loginState is LoginState.Error) {
                Text(
                    text = "Login failed: ${(loginState as LoginState.Error).message}",
                    color = cs.error,
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