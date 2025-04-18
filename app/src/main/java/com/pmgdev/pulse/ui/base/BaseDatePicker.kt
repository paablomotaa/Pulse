

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate

@Composable
fun DateField(
    showDialog: () -> Unit,
    selectedDate: LocalDate?,
    isDateError: Boolean,
    text: String,
    dateFormatError: String?,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier,
        isError = isDateError,
        singleLine = true,
        value = selectedDate.toString(),
        label = { Text(text) },
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = showDialog) {
                Icon(Icons.Default.DateRange, contentDescription = null)
            }

        },
        supportingText = {
            Row {
                Text(
                    text = dateFormatError ?: "",
                    Modifier.clearAndSetSemantics {})
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogDate(
    showDialog: Boolean,
    onShowDialog: () -> Unit,
    onSelectedDate: (LocalDate) -> Unit
) {
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = onShowDialog,
            onDateSelected = { date ->
                onSelectedDate(date)
                onShowDialog()
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val datePickerState = rememberDatePickerState()
                DatePicker(state = datePickerState)

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    TextButton(
                        onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            if (selectedDateMillis != null) {
                                val selectedDate =
                                    LocalDate.ofEpochDay(selectedDateMillis / (24 * 60 * 60 * 1000))
                                onDateSelected(selectedDate)
                            }
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}