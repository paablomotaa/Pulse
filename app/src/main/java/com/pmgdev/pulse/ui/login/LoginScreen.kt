package com.pmgdev.pulse.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmgdev.pulse.R
import com.pmgdev.pulse.ui.base.composables.BasePasswordField
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.base.composables.ChangeEmailDialog
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.darkgreen
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun LoginScreen(goToRegister: () -> Unit, viewModel: LoginViewModel, goToHome: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.state.toastMessage) {
        viewModel.state.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    if(viewModel.state.showEmailDialog){
        ChangeEmailDialog(
            onConfirm = { newEmail ->
                viewModel.changeEmail(newEmail)
                viewModel.hideEmailDialog()
            },
            onDismiss = {
                viewModel.hideEmailDialog()
            },
            title = "Contraseña olvidada",
            text = "Tu correo electrónico"
        )
    }
    Column(
        modifier = Modifier.background(Brush.verticalGradient(colors = listOf(
            darkgreen, dark))).fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        Image(painter = painterResource(R.drawable.logo_pulse_transparent), contentDescription = "",modifier = Modifier.size(200.dp) )
        Text("Saca tu potencial.", fontSize = 30.sp, color = Color.White,textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() )
        Text("Cambia tus hábitos.", fontSize = 30.sp, color = Color.White,textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() )
        Spacer(modifier = Modifier.size(50.dp))
        BaseTextField(
            value = viewModel.state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = "Email",
            errorText = viewModel.state.emailErrorText,
            isError = viewModel.state.isEmailError
        )
        Spacer(modifier = Modifier.size(25.dp))
        BasePasswordField(
            password = viewModel.state.password,
            onValueChange = {viewModel.onPasswordChange(it)},
            isError = viewModel.state.isPasswordError
        )
        Spacer(modifier = Modifier.size(25.dp))
        Button(onClick = {viewModel.onLoginClick(goToHome)}, modifier = Modifier.fillMaxWidth(0.7f), colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)) {
            Text("Login")
        }
        TextButton(onClick = {viewModel.showEmailDialog()}) {
            Text("¿Has olvidado tu contraseña?")
        }
        TextButton(onClick = {goToRegister()}) {
            Text("¿No tienes cuenta?")
        }
    }
}