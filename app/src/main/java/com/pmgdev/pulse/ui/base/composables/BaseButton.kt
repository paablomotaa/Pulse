package com.pmgdev.pulse.ui.base.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun BaseButton(onClick:()->Unit, label:String,modifier:Modifier = Modifier){
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)
    ) {
        Text(label)
    }
}