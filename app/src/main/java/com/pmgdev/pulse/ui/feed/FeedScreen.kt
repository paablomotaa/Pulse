package com.pmgdev.pulse.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmgdev.pulse.repository.model.Post
import com.pmgdev.pulse.ui.base.composables.Action
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.composables.BaseTextField
import com.pmgdev.pulse.ui.base.components.LoadingScreen
import com.pmgdev.pulse.ui.base.components.NoDataScreen
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark


@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedScreenViewModel,
    goToImagePost: () -> Unit,
    goToPostPreview: (String) -> Unit,
    goToListChats: () -> Unit,
    goToProfile: (String) -> Unit,
){
    LaunchedEffect(Unit) {
        viewModel.getNotices()
        viewModel.observePosts()
    }
    BaseScaffold(
        title = "Feed",
        navController = navController,
        showActionButton = true,
        floatingAction = goToImagePost,
        navIcon = null,
        actions = listOf(
            Action(
                icon = Icons.Default.Refresh,
                contentDescription = "",
                onClick = {viewModel.getNotices()}
            ),
            Action(
                icon = Icons.Default.MailOutline,
                contentDescription = "",
                onClick = {goToListChats()}
            ),
            Action(
                icon = Icons.Default.Search,
                contentDescription = "Buscar usuarios",
                onClick = { viewModel.searchMode = !viewModel.searchMode }
            )
        ),
    ) { paddingValues ->

        if(viewModel.searchMode) {
            SearchScreen(viewModel,paddingValues,goToProfile)
        }
        else {
            when (viewModel.state) {
                is FeedScreenState.Loading -> LoadingScreen(paddingValues)
                is FeedScreenState.Success -> FeedScreenContent(
                    paddingValues,
                    viewModel.posts,
                    goToPostPreview,
                    viewModel = viewModel,
                )

                is FeedScreenState.NoData -> {
                    NoDataScreen(paddingValues)
                }
            }
        }
    }
}

@Composable
fun FeedScreenContent(
    paddingValues: PaddingValues,
    posts: List<Post>,
    goToPostPreview: (String) -> Unit,
    viewModel: FeedScreenViewModel
) {
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
                FeedItem(post,goToPostPreview,viewModel)
            }
        }
    }
}

@Composable
fun FeedItem(post: Post,goToPostPreview: (String) -> Unit,viewModel: FeedScreenViewModel) {
    var isLiked by remember { mutableStateOf(false) }

    LaunchedEffect(post.uid) {
        val liked = viewModel.isPostLikedByUserSuspend(post.uid)
        isLiked = liked
    }
    Card(
        onClick = {
            goToPostPreview(post.uid)
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardColors(containerColor = dark, contentColor = MaterialTheme.colorScheme.surface, disabledContainerColor = MaterialTheme.colorScheme.background, disabledContentColor = MaterialTheme.colorScheme.surface)
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

                IconButton(onClick = {
                    viewModel.toggleLike(post.uid)
                    isLiked = !isLiked
                }) {
                    if(isLiked){
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Like", tint = Color.Red)
                    }
                    else{
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Like")
                    }
                }
                IconButton(onClick = {goToPostPreview(post.uid)}) {
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
@Composable
fun SearchScreen(
    viewModel: FeedScreenViewModel,
    paddingValues: PaddingValues,
    goProfile: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Brush.verticalGradient(listOf(clairgreen, dark)))
    ) {
        if (viewModel.searchMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                BaseTextField(
                    value = viewModel.searchQuery,
                    onValueChange = {
                        viewModel.searchQuery = it
                        viewModel.searchUsers(it)
                    },
                    label = "Buscar usuario...",
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 12.dp)
                )
            }

            LazyColumn {
                items(viewModel.searchResults) { user ->
                    Column {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            colors = CardColors(
                                containerColor = Color.Black,
                                contentColor = Color.White,
                                disabledContainerColor = Color.DarkGray,
                                disabledContentColor = Color.Gray
                            ),
                            shape = MaterialTheme.shapes.medium,
                            onClick = { goProfile(user.uid) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                AsyncImage(
                                    model = user.profileImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                )
                                Spacer(modifier = Modifier.size(12.dp))
                                Column {
                                    Text(user.username, style = MaterialTheme.typography.titleMedium)
                                    Text(user.email, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

