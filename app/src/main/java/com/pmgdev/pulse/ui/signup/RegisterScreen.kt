package com.pmgdev.pulse.ui.signup

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmgdev.pulse.ui.base.BaseTextField
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.darkgreen
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun RegisterScreen(goToLogin: () -> Unit) {
    Column(
        modifier = Modifier.background(
            Brush.verticalGradient(colors = listOf(
            darkgreen, dark
            ))).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(100.dp))
        Text("¡Bienvenido a Pulse!", fontSize = 30.sp, color = Color.White,textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() )
        Spacer(modifier = Modifier.size(50.dp))
        //BaseTextField("Nombre y apellidos")
        Spacer(modifier = Modifier.size(25.dp))
        //BaseTextField("Fecha de nacimiento")
        Spacer(modifier = Modifier.size(25.dp))
        //BaseTextField("Email")
        Spacer(modifier = Modifier.size(25.dp))
        //BaseTextField("Password")
        Spacer(modifier = Modifier.size(25.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth(0.7f), colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.size(25.dp))
        Text("¿Ya tienes una cuenta?", color = Color.White)
        TextButton(onClick = {goToLogin()}) {
            Text("Pulsa aquí")
        }
    }
}