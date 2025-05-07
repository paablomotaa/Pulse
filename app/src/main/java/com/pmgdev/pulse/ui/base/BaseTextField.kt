package com.pmgdev.pulse.ui.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.darkgray

@Composable
fun BaseTextField(
    value:String,
    onValueChange:(String) -> Unit,
    label:String,
    errorText:String,
    isError:Boolean
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = clairgreen) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = darkgray,
            unfocusedContainerColor = dark,
            focusedLabelColor = clairgreen,
            unfocusedLabelColor = darkgray,
            focusedIndicatorColor = clairgreen,
            unfocusedIndicatorColor = darkgray,
            cursorColor = clairgreen,
            focusedTextColor = clairgreen,
            unfocusedTextColor = clairgreen
        ),
        isError = isError,
        supportingText = { Text(errorText) }
    )
}
@Composable
fun BasePasswordField(
    password:String,
    onValueChange:(String) -> Unit,
    isError: Boolean
){
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text("Contraseña", color = clairgreen) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = darkgray,
            unfocusedContainerColor = dark,
            focusedLabelColor = clairgreen,
            unfocusedLabelColor = darkgray,
            focusedIndicatorColor = clairgreen,
            unfocusedIndicatorColor = darkgray,
            cursorColor = clairgreen,
            focusedTextColor = clairgreen,
            unfocusedTextColor = clairgreen
        ),
        isError = isError,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.CheckCircle
            else Icons.Filled.Close

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
            }
        },
    )
    if(isError){
        Text("ERROR en el formato de la contraseña")
    }
}