package com.pmgdev.pulse.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmgdev.pulse.R
import com.pmgdev.pulse.ui.base.BaseTextField
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.darkgreen
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun LoginScreen(goToRegister: () -> Unit,viewModel: LoginViewModel) {
    Column(
        modifier = Modifier.background(Brush.verticalGradient(colors = listOf(
            darkgreen, dark))).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        Image(painter = painterResource(R.drawable.logo_pulse_transparent), contentDescription = "",modifier = Modifier.size(200.dp) )
        Text("Saca tu potencial.", fontSize = 30.sp, color = Color.White,textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() )
        Text("Cambia tus hábitos.", fontSize = 30.sp, color = Color.White,textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() )
        Spacer(modifier = Modifier.size(50.dp))
        /*BaseTextField(
            value = viewModel.state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = "Email"
        )
        Spacer(modifier = Modifier.size(25.dp))
        BaseTextField(
            value = viewModel.state.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Password"
        )*/
        Spacer(modifier = Modifier.size(25.dp))
        Button(onClick = {viewModel.onLoginClick()}, modifier = Modifier.fillMaxWidth(0.7f), colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)) {
            Text("Login")
        }
        Spacer(modifier = Modifier.size(25.dp))
        Text("¿No tienes una cuenta?", color = Color.White)
        TextButton(onClick = {goToRegister()}) {
            Text("Pulsa aquí")
        }
    }
}