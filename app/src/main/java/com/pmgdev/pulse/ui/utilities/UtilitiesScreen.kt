package com.pmgdev.pulse.ui.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.base.composables.BaseButton
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun UtilitiesScreen(
    viewModel: UtilitiesViewModel,
    navController: NavController,
) {
    val state = viewModel.uiState

    BaseScaffold(
        title = "Utilidades",
        navController = navController,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Brush.verticalGradient(colors = listOf(clairgreen, dark))),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Introduce tus datos y tu objetivo para recibir un consejo personalizado:",
                style = MaterialTheme.typography.bodyLarge
            )

            BaseTextField(
                value = state.altura,
                onValueChange = viewModel::onAlturaChange,
                label = "Tu altura (cm)",
            )
            BaseTextField(
                value = state.peso,
                onValueChange = viewModel::onPesoChange,
                label = "Tu peso (kg)"
            )
            BaseTextField(
                value = state.objetivo,
                onValueChange = viewModel::onObjetivoChange,
                label = "¿Que quieres mejorar?"
            )

            BaseButton(
                onClick = {viewModel.getPersonalizedDietAdvice()},
                label = "Obtener consejo"
            )

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            state.dietAdvice?.let { advice ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Dieta: ",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(advice, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            //He decidido ponerle esta notita ya que siempre suelen poner algo así al utilizar IA
            Text(
                text = "Importante: Este consejo es generado por inteligencia artificial y tiene fines informativos. No sustituye la consulta con un nutricionista, dietista o médico profesional. Consulta siempre a un especialista antes de realizar cambios significativos en tu dieta o si tienes alguna condición médica.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
