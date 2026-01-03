package com.samrudha.glb3dviewerapp.Design

import android.net.Uri
import android.view.SurfaceView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.samrudha.glb3dviewerapp.Database.ModelEntity
import com.samrudha.glb3dviewerapp.MainViewModel.AuthState
import com.samrudha.glb3dviewerapp.MainViewModel.GLBModelViewer
import com.samrudha.glb3dviewerapp.MainViewModel.MainViewModel
import com.samrudha.glb3dviewerapp.Roles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.text.toInt


@Composable
fun mainUI(
    glbViewer: GLBModelViewer,
    modifier: Modifier = Modifier,
    lightIntensity: Float,
    authState: AuthState,
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel,
    innerPadding: Dp
) {
    when (authState) {
        is AuthState.Success -> {
            val userRole = authState.role

            manageModelsScreen(
                mainViewModel = viewModel,
                glbViewer = glbViewer,
                userRole = userRole,  // ✅ Pass the extracted role
                modifier = Modifier.padding(innerPadding),
                coroutineScope = coroutineScope,
                lightIntensity = lightIntensity,
            )
        }

        is AuthState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is AuthState.Error -> {
            AuthScreen(
                viewModel = viewModel,
                error = authState.message,
                modifier = Modifier
            )

        }

        AuthState.Idle -> {
            AuthScreen(
                viewModel = viewModel,
                modifier = Modifier
            )

        }
    }
}

@Composable
fun ModelViewerScreen(
    modelPath: String,
    glbViewer: GLBModelViewer,
    lightIntensity: Float,
    modifier: Modifier = Modifier
) {

    AndroidView(
        factory = { ctx ->
            SurfaceView(ctx).also { surfaceView ->
                glbViewer.initialize(surfaceView)

                loadModelFromPath(glbViewer, modelPath)

                // Setup custom lighting
                glbViewer.setupLighting(
                    intensity = lightIntensity,     // Brightness (higher = brighter)
                    colorTemperature = 6500, // 2700=warm, 6500=daylight, 9000=cool
                    directionX = -0.5f,
                    directionY = -1f,
                    directionZ = -0.5f
                )
            }

        },
        modifier = modifier
    )
}

fun loadModelFromPath(glbModelViewer: GLBModelViewer, path: String) {
    glbModelViewer.loadModelFromUri(Uri.fromFile(File(path)))
}

@Composable
fun manageModelsScreen(
    mainViewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    glbViewer: GLBModelViewer,
    lightIntensity: Float,
    userRole: Roles, // ✅ Add role parameter
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedModel by remember { mutableStateOf<ModelEntity?>(null) }
    val getModels by mainViewModel.models.collectAsState()
    var lightIntensity by remember { mutableFloatStateOf(100000f) }
    var sliderValue by remember { mutableFloatStateOf(lightIntensity) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = mainViewModel.getFileName(context, it)
            val filePath = mainViewModel.saveFileToInternalStorage(context, it, fileName)

            coroutineScope.launch(Dispatchers.IO) {
                mainViewModel.addModels(
                    fileName = fileName.substringBeforeLast('.'),
                    filePath = filePath
                )
            }
        }
    }

    // Show model viewer if a model is selected
    selectedModel?.let { model ->
        Column(modifier = modifier.fillMaxSize()) {
            Button(
                onClick = { selectedModel = null },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("← Back to List")
            }
            ModelViewerScreen(
                modelPath = model.modelPath,
                glbViewer = glbViewer,
                lightIntensity = lightIntensity,
                modifier = Modifier.weight(1f)
            )

            var lightDirectionX by remember { mutableFloatStateOf(-0.5f) }
            var lightDirectionY by remember { mutableFloatStateOf(-1f) }
            var lightDirectionZ by remember { mutableFloatStateOf(-0.5f) }

            LaunchedEffect(sliderValue, lightDirectionX, lightDirectionY, lightDirectionZ) {
                kotlinx.coroutines.delay(100)
                glbViewer.setupLighting(
                    intensity = sliderValue,
                    colorTemperature = 6500,
                    directionX = lightDirectionX,
                    directionY = lightDirectionY,
                    directionZ = lightDirectionZ
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Intensity Slider
                Text("Light Intensity: ${sliderValue.toInt()}")
                Slider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        sliderValue = newValue
                    },
                    valueRange = 1000f..200000f,
                    steps = 50,
                    modifier = Modifier.fillMaxWidth()
                )

                // X Direction Slider
                Text("Light Direction X: ${"%.2f".format(lightDirectionX)}")
                Slider(
                    value = lightDirectionX,
                    onValueChange = { newValue ->
                        lightDirectionX = newValue
                    },
                    valueRange = -1f..1f,
                    steps = 40,
                    modifier = Modifier.fillMaxWidth()
                )

                // Y Direction Slider
                Text("Light Direction Y: ${"%.2f".format(lightDirectionY)}")
                Slider(
                    value = lightDirectionY,
                    onValueChange = { newValue ->
                        lightDirectionY = newValue
                    },
                    valueRange = -1f..1f,
                    steps = 40,
                    modifier = Modifier.fillMaxWidth()
                )

                // Z Direction Slider
                Text("Light Direction Z: ${"%.2f".format(lightDirectionZ)}")
                Slider(
                    value = lightDirectionZ,
                    onValueChange = { newValue ->
                        lightDirectionZ = newValue
                    },
                    valueRange = -1f..1f,
                    steps = 40,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } ?: run {
        // Show model list when no model is selected
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            if (getModels.isEmpty()) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No Models Available")

                    // ✅ Only show Add button for ADMIN
                    if (userRole == Roles.ADMIN) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { filePicker.launch("*/*") }) {
                            Icon(Icons.TwoTone.AddCircle, contentDescription = "Add Model")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add Model")
                        }
                    }
                }
            } else {
                // ✅ Only show Add button for ADMIN
                if (userRole == Roles.ADMIN) {
                    Button(
                        onClick = { filePicker.launch("*/*") },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.TwoTone.AddCircle, contentDescription = "Add Model")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Add Model")
                    }
                }

                getModels.forEach { model ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = model.modelName,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // ✅ View button available for both USER and ADMIN
                        Button(onClick = {
                            selectedModel = model
                        }) {
                            Text(text = "View")
                        }

                        // ✅ Delete button only for ADMIN
                        if (userRole == Roles.ADMIN) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    mainViewModel.deleteModel(model)
                                }
                            }) {
                                Text(text = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}



