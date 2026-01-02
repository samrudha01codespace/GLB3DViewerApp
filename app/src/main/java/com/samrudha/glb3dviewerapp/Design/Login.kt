package com.samrudha.glb3dviewerapp.Design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samrudha.glb3dviewerapp.R

//Login UI
@Composable
@Preview
fun Login() {
    val email = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.wallpaper),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .blur(radius = 0.dp)
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .border(1.dp, Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.55f)
            )
        ) {

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = { Text(text = "Email") },
                    leadingIcon = {
                        Icon(
                            Icons.TwoTone.Email, ""
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.65f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.65f)
                    )
                )

                OutlinedTextField(
                    pass.value,
                    onValueChange = { pass.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = { Text(text = "Password") },
                    leadingIcon = {
                        Icon(
                            Icons.TwoTone.Lock, ""
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.65f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.65f)
                    )
                )
                TextButton(
                    onClick = { /*TODO*/ }, modifier = Modifier.padding(5.dp).align(Alignment.Start)
                ) { Text(text = "Forgot Password?") }

                Button(
                    onClick = { /*TODO*/ }, modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Login", modifier = Modifier.padding(5.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Don't have an account?")
                    TextButton(
                        onClick = { /*TODO*/ }, modifier = Modifier.padding(5.dp)
                    ) { Text(text = "Sign Up") }
                }
            }

        }
    }
}

@Composable
@Preview
fun Register() {
    val email = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.wallpaper),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .blur(radius = 0.dp)
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .border(1.dp, Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.55f)
            )
        ) {

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = { Text(text = "Email") },
                    leadingIcon = {
                        Icon(
                            Icons.TwoTone.Email, ""
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.65f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.65f)
                    )
                )

                OutlinedTextField(
                    pass.value,
                    onValueChange = { pass.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = { Text(text = "Password") },
                    leadingIcon = {
                        Icon(
                            Icons.TwoTone.Lock, ""
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.65f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.65f)
                    )
                )

                Button(
                    onClick = { /*TODO*/ }, modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Sign UP", modifier = Modifier.padding(5.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Have an account?")
                    TextButton(
                        onClick = { /*TODO*/ }, modifier = Modifier.padding(5.dp)
                    ) { Text(text = "Login") }
                }
            }

        }
    }
}