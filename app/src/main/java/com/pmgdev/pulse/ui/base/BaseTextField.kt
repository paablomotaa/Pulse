package com.pmgdev.pulse.ui.base

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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