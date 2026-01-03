package com.samrudha.glb3dviewerapp.Design

import android.content.Context
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Visibility
import androidx.compose.material.icons.twotone.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.samrudha.glb3dviewerapp.Database.DAO
import com.samrudha.glb3dviewerapp.Database.Entity
import com.samrudha.glb3dviewerapp.MainViewModel.AuthState
import com.samrudha.glb3dviewerapp.MainViewModel.CryptoManager
import com.samrudha.glb3dviewerapp.MainViewModel.MainViewModel
import com.samrudha.glb3dviewerapp.R
import com.samrudha.glb3dviewerapp.MainViewModel.Roles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AuthScreen(
    viewModel: MainViewModel,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(Roles.USER) }

    val authState by viewModel.authState.collectAsState()

    val cryptoManager = CryptoManager
    val encryptedPassword = cryptoManager.encrypt(password)

    val mask = remember { mutableStateOf(true) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.wallpaper),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),  // Dark at bottom
                            Color.Transparent                 // Transparent at top
                        ),
                        startY = Float.POSITIVE_INFINITY,
                        endY = 0f
                    )
                )
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
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Role Selection
                Row {
                    FilterChip(
                        selected = selectedRole == Roles.USER,
                        onClick = { selectedRole = Roles.USER },
                        modifier = Modifier.padding(10.dp),
                        label = { Text("USER") },
                        leadingIcon = { Icon(Icons.TwoTone.Person, null) }
                    )
                    FilterChip(
                        selected = selectedRole == Roles.ADMIN,
                        onClick = { selectedRole = Roles.ADMIN },
                        modifier = Modifier.padding(10.dp),
                        label = { Text("ADMIN") },
                        leadingIcon = { Icon(Icons.TwoTone.Lock, null) }
                    )
                }

                // Name field (only for registration)
                if (!isLoginMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        singleLine = true,
                        label = { Text("Name") },
                        leadingIcon = { Icon(Icons.TwoTone.Person, "") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                            unfocusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                            focusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                            unfocusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                            focusedLabelColor = DarkTheme_text().copy(alpha = 0.65f),
                            unfocusedLabelColor = DarkTheme_text().copy(alpha = 0.65f)
                        )
                    )
                }

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    singleLine = true,
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.TwoTone.Email, "") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        unfocusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        focusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        focusedLabelColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedLabelColor = DarkTheme_text().copy(alpha = 0.65f)
                    )
                )

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    singleLine = true,
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.TwoTone.Lock, "") },
                    trailingIcon = {
                        androidx.compose.material3.IconButton(
                            onClick = { mask.value = !mask.value },
                            enabled = authState !is AuthState.Loading &&
                                    password.isNotEmpty()
                        ) {
                             if (mask.value) Icon(Icons.TwoTone.Visibility,"") else Icon(Icons.TwoTone.VisibilityOff,"")
                        }
                    },
                    visualTransformation = if(mask.value) PasswordVisualTransformation() else VisualTransformation.None,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        unfocusedContainerColor = DarkTheme().copy(alpha = 0.65f),
                        focusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedBorderColor = DarkTheme_text().copy(alpha = 0.65f),
                        focusedLabelColor = DarkTheme_text().copy(alpha = 0.65f),
                        unfocusedLabelColor = DarkTheme_text().copy(alpha = 0.65f)
                    )
                )

                // Error message
                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                if (error != null) {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Login/Register Button
                Button(
                    onClick = {
                        if (isLoginMode) {

                            // Login
                            viewModel.login(selectedRole, email, password)

                        } else {
                            // Register
                            viewModel.register(
                                Entity(
                                    email = email,
                                    password = cryptoManager.hash(password),
                                    role = selectedRole
                                )
                            )

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    enabled = authState !is AuthState.Loading &&
                            email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            (!isLoginMode.not() || name.isNotEmpty())
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = if (isLoginMode) "Login" else "Sign Up",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }

                // Toggle between Login/Register
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = if (isLoginMode) "Don't have an account?" else "Have an account?",
                        color = DarkTheme_text()
                    )
                    TextButton(
                        onClick = {
                            isLoginMode = !isLoginMode
                            // Clear fields when switching
                            email = ""
                            password = ""
                            name = ""
                        },
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(if (isLoginMode) "Sign Up" else "Login")
                    }
                }
            }
        }
    }
}

