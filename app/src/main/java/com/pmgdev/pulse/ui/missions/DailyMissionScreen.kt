package com.pmgdev.pulse.ui.missions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
 * dailyMissions
 *
 * Interfaz para las misiones diarias que el usuario podrá realizar
 *
 * Faltan las recompensas.
 *
 */

@Composable
fun DailyMissionsScreen(
    viewModel: DailyMissionsViewModel,
    navController: NavController,
    steps:Int,
    kcal: Float
) {
    LaunchedEffect(Unit) {
        viewModel.loadMissions(steps,kcal)
    }

    BaseScaffold(
        title = "Misiones",
        navController = navController,
        navIcon = arrowBack(),
        navIconAction = {navController.popBackStack()},
    ) { paddingValues ->
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues).background(Brush.verticalGradient(listOf(clairgreen,dark)))
    ) {
        items(viewModel.missions) { mission ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(mission.title, fontWeight = FontWeight.Bold)
                    Text(mission.description)
                    LinearProgressIndicator(
                    progress = { (mission.progress.toFloat() / mission.goal).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    )
                    Text("${mission.progress} / ${mission.goal}", modifier = Modifier.padding(top = 4.dp))
                    if (mission.isCompleted) {
                        Text("¡Completada!", color = Color.Green, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
    }
}
