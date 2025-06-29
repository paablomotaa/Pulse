package com.pmgdev.pulse.ui.previewpost

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmgdev.pulse.repository.model.Comment
import com.pmgdev.pulse.repository.model.Post
import com.pmgdev.pulse.ui.base.composables.Action
import com.pmgdev.pulse.ui.base.composables.BaseButton
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.components.LoadingScreen
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.base.components.NoDataScreen
import com.pmgdev.pulse.ui.base.composables.BaseDialog
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark


/**
 *
 * PostDetails
 *
 * Interfaz para ver los posts y añadir comentarios.
 *
 *
 */

@Composable
fun PostDetailScreen(
    post: String,
    onBack: () -> Unit,
    navController: NavController,
    viewModel: PreviewPostViewModel,
    goToProfile: (String) -> Unit,
){
    LaunchedEffect(Unit) {
        viewModel.uploadImage(uid = post)
        viewModel.observeComments(post)
    }

    if(viewModel.showDialogFine){
        BaseDialog(
            title = "¿Seguro que deseas reportar a este usuario?",
            text = "Crearemos un registro y nos pondremos a revisar esta publicación",
            onConfirm = {viewModel.createFine(post)},
            onDismiss = {viewModel.hideDialogFine()}
        )
    }

    BaseScaffold(
        title = "Detalles de la publicación",
        navController = navController,
        showBottomBar = false,
        navIcon = arrowBack(),
        navIconAction = onBack,
        actions = listOf(
            Action(
                icon = Icons.Default.Refresh,
                contentDescription = "",
                onClick = {viewModel.uploadImage(uid = post)}
            ),
            Action(
                icon = Icons.Default.Warning,
                contentDescription = "",
                onClick = {viewModel.showDialogFine()}
            ),
        )
    ) { paddingValues ->



    when(viewModel.state){
        PreviewPostState.Loading -> {
            LoadingScreen(paddingValues)
        }
        PreviewPostState.NoData ->{
            NoDataScreen(paddingValues)
        }
        is PreviewPostState.Success -> {
            PostDetailContent(
                post = (viewModel.state as PreviewPostState.Success).post,
                comments = viewModel.comments,
                paddingValues,
                viewModel,
                goToProfile
            )
        }
    }
    }
}

@Composable
fun PostDetailContent(
    post: Post,
    comments: List<Comment>,
    paddingValues: PaddingValues,
    viewModel: PreviewPostViewModel,
    goToProfile: (String) -> Unit
) {

        if(viewModel.showDialogDelete){
            BaseDialog(
                title = "¿Seguro que deseas borrar la publicación?",
                text = "El cambio sera irreversible y no podrás recuperar los datos.",
                onConfirm = {viewModel.deletePost(post.uid)},
                onDismiss = {viewModel.hideDialogDelete()}
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(colors = listOf(clairgreen, dark))),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = post.imagePost,
                    contentDescription = "Imagen del post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = dark,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Subido por ${post.username}", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Descripcion: ${post.description}", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${post.likes} Me gusta • ${post.comments} Comentarios", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Comentarios:", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (comments.isEmpty()) {
                            Text("No hay comentarios aún", color = Color.LightGray)
                        } else {
                            comments.forEach { comment ->
                                Text("• ${comment.username}: ${comment.content}", color = Color.White)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                        Spacer(Modifier.size(16.dp))
                        if(!viewModel.isCurrentUserPost){
                        if(viewModel.statecomment.sended){
                            Text("Comentario enviado", color = Color.LightGray)
                        }
                        else {
                            BaseTextField(
                                value = viewModel.statecomment.comment,
                                onValueChange = { viewModel.onContentChange(it) },
                                label = "Escribe un comentario",
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                    BaseButton(
                                        onClick = {viewModel.postComment(post.uid)},
                                        label = "Enviar"
                                    )
                                    BaseButton(
                                        onClick = {goToProfile(post.uiduser)},
                                        label = "Ver perfil"
                                    )
                                }
                            }

                        }
                        else{
                            BaseButton(
                                //Funcion para eliminar
                                onClick = {viewModel.showDialogDelete()},
                                label = "Eliminar"
                            )
                        }
                    }
                }
            }
        }
}
