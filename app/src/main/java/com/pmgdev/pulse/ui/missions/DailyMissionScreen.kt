package com.pmgdev.pulse.ui.missions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark


/**
 * DailyMissionsScreen
 *
 * Interfaz para la misión diaria de 5000 pasos con títulos progresivos.
 *
 */

@Composable
fun DailyMissionsScreen(
    viewModel: DailyMissionsViewModel,
    navController: NavController,
    steps: Int,
) {
    LaunchedEffect(steps) {
        viewModel.loadMissions(steps)
    }

    BaseScaffold(
        title = "Misiones",
        navController = navController,
        navIcon = arrowBack(),
        navIconAction = { navController.popBackStack() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(clairgreen, dark)))
        ) {
            Text(
                text = "Tu Título: ${viewModel.userCurrentTitle}",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val mission = viewModel.missions.firstOrNull()

                if (mission != null) {
                    item {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(mission.title, fontWeight = FontWeight.Bold)
                                Text(mission.description)
                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { (mission.progress.toFloat() / mission.goal).coerceIn(0f, 1f) },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${mission.progress} / ${mission.goal} pasos", color = Color.Gray)

                                Spacer(modifier = Modifier.height(8.dp))

                                val isCurrentlyAtGoal = steps >= mission.goal

                                if (isCurrentlyAtGoal && !mission.isCompleted) {
                                    Text("¡Misión completada! Reclama tu nuevo título.", color = Color.Green, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.claimMissionReward() }) {
                                        Text("Reclamar Título")
                                    }
                                } else if (mission.isCompleted) {
                                    Text("¡Título reclamado hoy!", color = Color.Blue, fontWeight = FontWeight.Bold)
                                } else {
                                    Text("Sigue andando para completar esta misión.", color = Color.Black)
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text("Cargando misión...", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}