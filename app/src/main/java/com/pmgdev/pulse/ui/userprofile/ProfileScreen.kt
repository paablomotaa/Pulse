package com.pmgdev.pulse.ui.userprofile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmgdev.pulse.R
import com.pmgdev.pulse.repository.model.User
import com.pmgdev.pulse.ui.base.BaseButton
import com.pmgdev.pulse.ui.base.BaseScaffold
import com.pmgdev.pulse.ui.base.LoadingScreen
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark

@Composable
fun ProfileScreen(navController: NavController,viewModel: ProfileScreenViewModel){
    BaseScaffold(
        title = "Perfil de usuario",
        navController = navController
    ) { paddingValues ->
        when (viewModel.state) {
            ProfileScreenState.Loading -> {
                LoadingScreen(paddingValues)
            }

            ProfileScreenState.NoData -> {
                //NODATA
            }

            is ProfileScreenState.Success -> {
                ProfileScreenContent(
                    paddingValues,
                    user = (viewModel.state as ProfileScreenState.Success).user
                )
            }
        }
    }
}

@Composable
fun ProfileScreenContent(paddingValues: PaddingValues, user: User?){

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues).background(
            Brush.verticalGradient(colors = listOf(clairgreen, dark))),
            contentAlignment = Alignment.TopStart
        ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier.size(150.dp).clip(CircleShape).border(
                            3.dp, clairgreen,
                            CircleShape
                        ).background(dark),
                        painter = painterResource(R.drawable.logo_pulse_transparent),
                        contentDescription = "",
                    )
                    Surface(
                        modifier = Modifier.padding(30.dp).shadow(5.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = dark
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(user?.fullname ?: "NOT FOUND", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = user?.bio ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(22.dp),
                                verticalAlignment = Alignment.CenterVertically  
                            ) {
                                Spacer(Modifier.size(5.dp))
                                Column {
                                    Text("257", color = Color.White)
                                    Text("Posts",color = Color.White)
                                }
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(1.dp)
                                        .background(Color.White)
                                )
                                Column {
                                    Text(user?.followers.toString(),color = Color.White)
                                    Text("Followers",color = Color.White)
                                }
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(1.dp)
                                        .background(Color.White)
                                )
                                Column {
                                    Text(user?.following.toString(),color = Color.White)
                                    Text("Following", color = Color.White)
                                }
                                Spacer(Modifier.size(5.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            BaseButton(onClick = {}, label = "Editar perfil")
                            /*Button(onClick = {}, modifier = Modifier.fillMaxWidth(0.5f), colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)) {
                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text("Follow")
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                                }
                            }*/
                            /*Button(onClick = {}, modifier = Modifier.fillMaxWidth(0.5f), colors = ButtonColors(containerColor = mediumgreen, contentColor = Color.White, disabledContentColor = Color.Red, disabledContainerColor = Color.Red)) {
                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Send message")
                                    Icon(imageVector = Icons.Default.MailOutline, contentDescription = "")
                                }
                            }*/
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
        }
}