package com.pmgdev.pulse.ui.signup

import DateField
import DialogDate
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.darkgreen
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun RegisterScreen(goToLogin: () -> Unit,viewModel:RegisterViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    //Para que salga un toast cada vez que haya algo mal.
    LaunchedEffect(viewModel.state.toastMessage) {
        viewModel.state.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    Column(
        modifier = Modifier.background(
            Brush.verticalGradient(colors = listOf(
            darkgreen, dark
            ))).fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(100.dp))
        Text("¡Bienvenido a Pulse!", fontSize = 30.sp, color = Color.White,textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() )
        Spacer(modifier = Modifier.size(50.dp))
        BaseTextField(
            value = viewModel.state.name,
            label = "Nombre y apellidos",
            onValueChange = {viewModel.onNameChange(it)},
            isError = viewModel.state.isNameError,
            errorText = viewModel.state.nameErrorText
        )
        Spacer(modifier = Modifier.size(25.dp))
        DateField(
            showDialog = { viewModel.showDatePicker() },
            selectedDate = viewModel.state.date,
            isDateError = viewModel.state.isDateError,
            text = "Fecha de Nacimiento",
            dateFormatError = viewModel.state.dateErrorText,
            modifier = Modifier
        )

        DialogDate(
            showDialog = viewModel.state.showDatePickerDialog,
            onShowDialog = { viewModel.hideDatePicker() },
            onSelectedDate = { date -> viewModel.onDateSelected(date) }, // Sigue enviando LocalDate al ViewModel
        )
        Spacer(modifier = Modifier.size(25.dp))
        BaseTextField(
            value = viewModel.state.username,
            onValueChange = {viewModel.onUsernameChange(it)},
            label = "Username",
            errorText = viewModel.state.usernameErrorText,
            isError = viewModel.state.isUsernameError,
        )
        Spacer(modifier = Modifier.size(25.dp))
        BaseTextField(
            value = viewModel.state.email,
            label = "Email",
            onValueChange = {viewModel.onEmailChange(it)},
            isError = viewModel.state.isEmailError,
            errorText = viewModel.state.emailErrorText
        )
        Spacer(modifier = Modifier.size(25.dp))
        BaseTextField(
            value = viewModel.state.password,
            label = "Password",
            onValueChange = {viewModel.onPasswordChange(it)},
            isError = viewModel.state.isPasswordError,
            errorText = viewModel.state.passwordErrorText
            )
        Spacer(modifier = Modifier.size(25.dp))
        Button(onClick = {viewModel.onRegisterClick(goToLogin)}, modifier = Modifier.fillMaxWidth(0.7f), colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.size(25.dp))
        Text("¿Ya tienes una cuenta?", color = Color.White)
        TextButton(onClick = {goToLogin()}) {
            Text("Pulsa aquí")
        }
    }
}