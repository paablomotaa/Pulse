package com.pmgdev.pulse.ui.settings


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.base.composables.BaseDialog
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.composables.ChangeEmailDialog
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.mediumgreen
import androidx.core.net.toUri

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
    goToLogin: () -> Unit
) {



    BaseScaffold(
        title = "Ajustes",
        navController = navController,
        navIcon = arrowBack(),
        navIconAction = { navController.popBackStack() },
        showBottomBar = false
    ) { paddingValues ->

        if(viewModel.state.showDeleteDialog){
            BaseDialog(
                onConfirm = { viewModel.deleteAccount(goToLogin) },
                onDismiss = { viewModel.hideDeleteDialog() }
            )
        }
        if(viewModel.state.showEmailDialog){
            ChangeEmailDialog(
                onConfirm = { newEmail ->
                    viewModel.changeEmail(newEmail)
                    viewModel.hideEmailDialog()
                },
                onDismiss = { viewModel.hideEmailDialog() }
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(colors = listOf(clairgreen, dark)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Ajustes",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )

                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                        Button(
                            onClick = { viewModel.contactSupport() { url ->
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                navController.context.startActivity(intent)
                            }},
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = mediumgreen)
                        ) {
                            Text("Contactar con nosotros")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tema oscuro",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            /*Switch(
                                checked = isDarkThemeEnabled,
                                onCheckedChange = { viewModel.toggleTheme() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = mediumgreen,
                                    uncheckedThumbColor = Color.Gray
                                )
                            )*/
                        }

                        Button(
                            onClick = { viewModel.showEmailDialog() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = mediumgreen)
                        ) {
                            Text("Cambiar correo")
                        }

                        Button(
                            onClick = { viewModel.changePassword() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = mediumgreen)
                        ) {
                            Text("Cambiar contraseña")
                        }

                        Button(
                            onClick = { viewModel.showDeleteDialog() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Eliminar cuenta")
                        }

                        Button(
                            onClick = { viewModel.logout(goToLogin) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cerrar sesión")
                        }

                    }
                }
            }
        }
    }
}


