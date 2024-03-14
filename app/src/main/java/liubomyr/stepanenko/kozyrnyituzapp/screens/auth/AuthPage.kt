package liubomyr.stepanenko.kozyrnyituzapp.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
internal fun AuthPage(navController: NavController) {
    AuthScreen(navController = navController)
}

@Composable
private fun AuthScreen(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val state by authViewModel.state.observeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        when (state) {
            AuthState.HasAuth -> {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            }

            AuthState.NoAuth -> {
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { authViewModel.login(email, password) }) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            null -> {
                CircularProgressIndicator()
            }
        }
    }
}
