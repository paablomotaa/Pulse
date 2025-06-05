package com.pmgdev.pulse.ui.base.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ChangeEmailDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
){
    var email by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar correo") },
        text = {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Nuevo correo") }
            )
        },
        confirmButton = {
            BaseButton(
                onClick = { onConfirm(email) },
                label = "Confirmar"
            )
        },
        dismissButton = {
            BaseButton(
                onClick = {onDismiss()},
                label = "Cancelar"
            )
        }
    )
}
@Composable
fun BaseDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminación de cuenta") },
        text = {Text("¿Seguro que deseas borrar tu cuenta? Se perderán todos tus datos y estos no podrán ser recuperados.")},
        confirmButton = {
            BaseButton(
                onClick = { onConfirm() },
                label = "Confirmar"
            )
        },
        dismissButton = {
            BaseButton(
                onClick = {onDismiss()},
                label = "Cancelar"
            )
        }
    )
}
