package com.pmgdev.pulse.ui.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pmgdev.pulse.R
import com.pmgdev.pulse.ui.base.BaseNavigationBar
import com.pmgdev.pulse.ui.base.BaseScaffold
import com.pmgdev.pulse.ui.base.BaseTopAppBar
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark

@Composable
fun UtilitiesScreen(navController: NavController){
    BaseScaffold(
        title = "Utilidades",
        navController = navController
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues).background(
            Brush.verticalGradient(colors = listOf(clairgreen, dark))),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Gráfico Semanal")
                Image(painter = painterResource(R.drawable.graficoejemplo), contentDescription = "")
            }
        }
    }
}