package com.pmgdev.pulse.ui.editprofile

import BasePickerImageProfile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.base.composables.BaseButton
import com.pmgdev.pulse.ui.base.composables.BaseNumberField
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark


//OBSERVER?
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel,
    goBack: () -> Boolean
) {
    LaunchedEffect(Unit) {
        viewModel.ChargeUserData()
    }
    BaseScaffold(
        title = "Editar perfil",
        navController = navController,
        navIcon = arrowBack(),
        navIconAction = {goBack()}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).background(Brush.verticalGradient(colors = listOf(
                    clairgreen, dark
                ))),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BasePickerImageProfile(
                image = viewModel.state.image,
                onImageChange = { viewModel.onImageChange(it)}
            )
            Spacer(modifier = Modifier.size(15.dp))
            BaseTextField(
                value = viewModel.state.username,
                onValueChange = { viewModel.onUsernameChange(it)  },
                label = "Nombre de usuario",
                isError = viewModel.state.errorUsername,
                errorText = viewModel.state.errorUsernameText
            )

            BaseTextField(
                value = viewModel.state.fullname,
                onValueChange = { viewModel.onNameChange(it) },
                label = "Nombre completo",
                isError = viewModel.state.fullnameError,
                errorText = viewModel.state.errorFullnameText
            )

            BaseTextField(
                value = viewModel.state.bio,
                onValueChange = { viewModel.onBioChange(it)  },
                label = "Biografía",
                isError = viewModel.state.bioError,
                errorText = viewModel.state.errorBioText
            )

            BaseNumberField(
                value = viewModel.state.peso.toString(),
                onValueChange = {
                    viewModel.onPesoChange(it)
                },
                label = "Peso (kg)",

            )

            BaseNumberField(
                value = viewModel.state.altura.toString(),
                onValueChange = {
                    viewModel.onAlturaChange(it)
                },
                label = "Altura (cm)",
            )

            Spacer(modifier = Modifier.height(24.dp))

            BaseButton(
                onClick = { viewModel.onEditClick(goBack) },
                label = "Guardar cambios"
            )
        }
    }

}
