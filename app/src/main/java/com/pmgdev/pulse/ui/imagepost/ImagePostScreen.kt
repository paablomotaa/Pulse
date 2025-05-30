package com.pmgdev.pulse.ui.imagepost

import BasePickerImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pmgdev.pulse.ui.base.composables.BaseButton
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark

@Composable
fun ImagePostScreen(navController: NavController,viewModel: ImagePostViewModel,onBack:() -> Unit){
    BaseScaffold(
        title = "Nueva publicación",
        navController = navController,
        showBottomBar = false,
        navIcon = arrowBack(),
        navIconAction = onBack
    ) { paddingValues ->
        ImagePostContent(paddingValues, viewModel,onBack)
    }
}

@Composable
fun ImagePostContent(
    paddingValues: PaddingValues,
    viewModel: ImagePostViewModel,
    onBack: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(clairgreen, dark)))
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            BasePickerImage(
                image = viewModel.state.image,
                onImageChange = { viewModel.onImageChange(it)}
            )
            Spacer(modifier = Modifier.size(36.dp))
            BaseTextField(
                value = viewModel.state.description,
                onValueChange = {viewModel.onDescriptionChange(it)},
                label = "Description",
                errorText = "si",
                isError = false
            )
            Spacer(modifier = Modifier.size(26.dp))
            BaseButton(onClick = {viewModel.onPostClick(onBack)}, label = "Post")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePostContentPreview() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<ImagePostViewModel>()
    ImagePostScreen(navController,viewModel,{})
}