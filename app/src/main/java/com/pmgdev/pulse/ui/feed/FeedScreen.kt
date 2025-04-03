package com.pmgdev.pulse.ui.feed

import android.widget.Space
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pmgdev.pulse.R
import com.pmgdev.pulse.repository.model.User
import com.pmgdev.pulse.ui.base.BaseNavigationBar
import com.pmgdev.pulse.ui.base.BaseTopAppBar
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark

@Composable
fun FeedScreen(){
    Scaffold(
        modifier = Modifier,
        topBar = { BaseTopAppBar() },
        bottomBar = {
            BaseNavigationBar()
        }
    ) { paddingValues ->
    val users = listOf(
        User(
            username = "paablomotaa",
            image = painterResource(R.drawable.ic_launcher_foreground),
            imagePost = painterResource(R.drawable.logo_pulse_transparent)
        ),
        User(
            username = "pablomota2",
            image = painterResource(R.drawable.logo_pulse_transparent),
            imagePost = painterResource(R.drawable.ic_launcher_background)
        )
    )
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues).background(Brush.verticalGradient(colors = listOf(clairgreen, dark))),
    ){
        LazyColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(users) { user ->
                FeedItem(user)
            }
        }
    }
    }
}
@Composable
fun FeedItem(user: User) {

    Card(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardColors(containerColor = Color.DarkGray, contentColor = MaterialTheme.colorScheme.surface, disabledContainerColor = MaterialTheme.colorScheme.background, disabledContentColor = MaterialTheme.colorScheme.surface)
    ){
        Column {
            Image(painter = user.imagePost, contentDescription = "")
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Text("Subido por " + user.username)
                Spacer(modifier = Modifier.size(30.dp))
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "")
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.MailOutline, contentDescription = "")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(10.dp))
                Text("Le gustan a 10 personas")
                Spacer(modifier = Modifier.size(30.dp))
                Text("7 Comentarios")
            }
        }
    }

}
