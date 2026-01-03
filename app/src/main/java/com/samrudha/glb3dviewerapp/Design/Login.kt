package com.samrudha.glb3dviewerapp.Design

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.samrudha.glb3dviewerapp.Database.DAO
import com.samrudha.glb3dviewerapp.Database.Entity
import com.samrudha.glb3dviewerapp.MainViewModel.CryptoManager
import com.samrudha.glb3dviewerapp.MainViewModel.MainViewModel
import com.samrudha.glb3dviewerapp.R
import com.samrudha.glb3dviewerapp.Roles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AuthScreen(mainViewModel: MainViewModel, context: Context) {
    val cryptoManager = CryptoManager
    var isLoginMode by remember { mutableStateOf(true) }
    val email = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }
    val selection = remember { mutableStateOf(Roles.USER) }
    val encrypted = cryptoManager.encrypt(pass.value)
    val decrypted = cryptoManager.decrypt(pass.value)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .border(1.dp, DarkTheme().copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = DarkTheme().copy(alpha = 0.85f)
            )
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    FilterChip(
                        selected = selection.value == Roles.USER,
                        onClick = { selection.value = Roles.USER },
                        modifier = Modifier.padding(10.dp),
                        label = { Text("USER") },
                        leadingIcon = { Icon(Icons.TwoTone.Person, null) },
                    )
                    FilterChip(
                        selected = selection.value == Roles.ADMIN,
                        onClick = { selection.value = Roles.ADMIN },
                        modifier = Modifier.padding(10.dp),
                        label = { Text("ADMIN") },
                        leadingIcon = {Icon(Icons.TwoTone.Lock, null) },
                    )
                }

                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    singleLine = true,
                    label = { Text(text = "Email") },
                    leadingIcon = {
                        Icon(Icons.TwoTone.Email, "")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        unfocusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        focusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        focusedLabelColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedLabelColor = DarkTheme_text().copy(alpha = 0.65f)
                    )
                )

                OutlinedTextField(
                    value = pass.value,
                    onValueChange = { pass.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    label = { Text(text = "Password") },
                    leadingIcon = {
                        Icon(Icons.TwoTone.Lock, "")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        unfocusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        focusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        focusedLabelColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedLabelColor = DarkTheme_text().copy(alpha = 0.65f)
                    )
                )

                if (isLoginMode) {
                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.Start)
                    ) {
                        Text(text = "Forgot Password?", color = DarkTheme_text())
                    }
                }

                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            if (isLoginMode) {
                                val user = dao.loginUser(
                                    selection.value,
                                    email.value,
                                    decrypted
                                )

                                mainViewModel.loginModel()
                                withContext(Dispatchers.Main) {
                                    if (user != null) {
                                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                dao.registerData(
                                    Entity(
                                        email = email.value,
                                        password = encrypted,
                                        role = selection.value
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    isLoginMode = true
                                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = if (isLoginMode) "Login" else "Sign Up",
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isLoginMode) "Don't have an account?" else "Have an account?"
                    )
                    TextButton(
                        onClick = { isLoginMode = !isLoginMode },
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(text = if (isLoginMode) "Sign Up" else "Login")
                    }
                }
            }
        }
    }
}
