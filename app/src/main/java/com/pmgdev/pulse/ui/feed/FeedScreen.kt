package com.pmgdev.pulse.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmgdev.pulse.R
import com.pmgdev.pulse.repository.model.Post
import com.pmgdev.pulse.ui.base.BaseScaffold
import com.pmgdev.pulse.ui.base.LoadingScreen
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark


@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedScreenViewModel,
    goToImagePost: () -> Unit,
    goToPostPreview:(String) -> Unit
){
    BaseScaffold(
        title = "Feed",
        navController = navController,
        showActionButton = true,
        floatingAction = goToImagePost
    ) { paddingValues ->
        when (viewModel.state) {
            is FeedScreenState.Loading -> LoadingScreen(paddingValues)
            is FeedScreenState.Success -> FeedScreenContent(paddingValues, (viewModel.state as FeedScreenState.Success).post,goToPostPreview)
            is FeedScreenState.NoData -> {
                // NoDataScreen()
            }
        }
    }
}

@Composable
fun FeedScreenContent(paddingValues: PaddingValues, posts: List<Post>,goToPostPreview: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Brush.verticalGradient(colors = listOf(clairgreen, dark)))
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(posts) { post ->
                FeedItem(post,goToPostPreview)
            }
        }
    }
}

@Composable
fun FeedItem(post: Post,goToPostPreview: (String) -> Unit) {
    Card(
        onClick = {
            goToPostPreview(post.uid)
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardColors(containerColor = Color.DarkGray, contentColor = MaterialTheme.colorScheme.surface, disabledContainerColor = MaterialTheme.colorScheme.background, disabledContentColor = MaterialTheme.colorScheme.surface)
    ){
        Column {
            AsyncImage(
                model = post.imagePost,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Text("Subido por " + post.username)
                Spacer(modifier = Modifier.size(30.dp))
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "")
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.MailOutline, contentDescription = "")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(10.dp))
                Text("Le gustan a ${post.likes} personas")
                Spacer(modifier = Modifier.size(30.dp))
                Text("${post.comments} Comentarios")
            }
        }
    }

}
