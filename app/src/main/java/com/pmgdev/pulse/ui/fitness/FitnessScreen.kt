package com.pmgdev.pulse.ui.fitness

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.base.baseicons.Barefoot
import com.pmgdev.pulse.ui.base.baseicons.FireIcon
import com.pmgdev.pulse.ui.base.composables.BaseButton
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.components.LoadingScreen
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark


/**
 *
 * Contador de pasos y kcal
 *
 * Esta interfaz muestra los pasos y las kcal quemadas en el propio día
 *
 * Falta añadir los gráficos semanales de progreso.
 *
 * Se solicitan al usuario los permisos de recognitionactivity para recoger la actividad diaria del dispositivo.
 * El usuario debe loguearse con Google.
 *
 */

@Composable
fun FitnessScreen(
    viewModel: FitnessScreenViewModel,
    navController: NavController,
    goToMissions: () -> Unit,

    ) {
    BaseScaffold(
        title = "Fitness",
        navController = navController,
        showBottomBar = true,
    ) { paddingValues ->
        val context = LocalContext.current
        val activity = context as Activity

        LaunchedEffect(Unit) {
            Log.d("GoogleFit", "Comprobando permisos...")
            val permission = Manifest.permission.ACTIVITY_RECOGNITION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("GoogleFit", "Solicitando permiso ACTIVITY_RECOGNITION")
                ActivityCompat.requestPermissions(activity, arrayOf(permission), 0)
            }

            if (viewModel.checkGoogleFitPermissions(context)) {
                Log.d("GoogleFit", "Permisos OK, pidiendo datos e iniciando sensor en tiempo real")
                viewModel.initializeFitnessData(context)
            } else {
                Log.d("GoogleFit", "No tiene permisos de Google Fit")
                viewModel.setNotRegistered()
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                Log.d("GoogleFit", "FitnessScreen saliendo de la composición, desregistrando sensores.")
                viewModel.stopRealtimeSteps(context)
            }
        }


        when {
            viewModel.uiState.success -> {
                FitnessScreenUserLogged(viewModel,paddingValues,goToMissions)
            }

            viewModel.uiState.isError -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues).background(Brush.verticalGradient(listOf(clairgreen,dark))),
                    contentAlignment = Alignment.Center,

                ){
                    Text("Error: ${viewModel.uiState.error}", color = Color.Red)
                }

            }

            viewModel.uiState.isNotRegister -> {
                FitnessScreenUserNotLogged(viewModel,paddingValues,context,activity)
            }
            else -> {
                LoadingScreen(paddingValues)
            }
        }
    }
}

@Composable
fun FitnessScreenUserLogged(
    viewModel: FitnessScreenViewModel,
    paddingValues: PaddingValues,
    goToMissions: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Brush.verticalGradient(listOf(clairgreen, dark))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "¡Tu información de hoy!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            FitnessInfoCard(
                icon = Barefoot(),
                title = "Pasos de ayer",
                value = "${viewModel.uiState.stepsYesterday} pasos"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FitnessInfoCard(
                icon = Barefoot(),
                title = "Pasos de hoy",
                value = "${viewModel.uiState.steps} pasos"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FitnessInfoCard(
                icon = FireIcon(),
                title = "Calorías quemadas hoy",
                value = "${viewModel.uiState.calories.toInt()} kcal"
            )

            Spacer(modifier = Modifier.height(24.dp))

            BaseButton(
                onClick = { goToMissions() },
                label = "Ver misiones"
            )
        }
    }
}

@Composable
fun FitnessInfoCard(icon: ImageVector, title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(value, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            }
        }
    }
}

@Composable
fun FitnessScreenUserNotLogged(
    viewModel: FitnessScreenViewModel,
    paddingValues: PaddingValues,
    context: Activity,
    activity: Activity
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Brush.verticalGradient(listOf(clairgreen, dark))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "PulseFit",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ventajas de conectar tu cuenta:",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            val beneficios = listOf(
                "Contador de pasos diarios",
                "Contador de kcal quemadas al día",
                "Gráficos personalizados (próximamente)",
                "Misiones diarias con recompensas"
            )

            beneficios.forEach { beneficio ->
                Text(
                    text = "• $beneficio",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            BaseButton(
                onClick = { viewModel.launchGoogleFitFlow(context, activity) },
                label = "Vincular cuenta de Google"
            )
        }
    }
}
