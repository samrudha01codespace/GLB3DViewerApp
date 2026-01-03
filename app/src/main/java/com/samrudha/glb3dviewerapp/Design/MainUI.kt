package com.samrudha.glb3dviewerapp.Design

import android.net.Uri
import android.view.SurfaceView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import com.samrudha.glb3dviewerapp.Viewer.GLBModelViewer
import com.samrudha.glb3dviewerapp.MainViewModel.MainViewModel
import com.samrudha.glb3dviewerapp.MainViewModel.Roles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


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
                userRole = userRole,
                modifier = Modifier.padding(top = innerPadding),
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
                glbViewer.setupLighting(
                    intensity = lightIntensity,
                    colorTemperature = 6500,
                    directionX = -0.5f,
                    directionY = -1f,
                    directionZ = -0.5f
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
    )
}


fun loadModelFromPath(glbModelViewer: GLBModelViewer, path: String) {
    glbModelViewer.loadModelFromUri(Uri.fromFile(File(path)))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun manageModelsScreen(
    mainViewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    glbViewer: GLBModelViewer,
    lightIntensity: Float,
    userRole: Roles,
    modifier: Modifier = Modifier
) {


    val context = LocalContext.current
    var selectedModel by remember { mutableStateOf<ModelEntity?>(null) }
    val getModels by mainViewModel.models.collectAsState()
    var sliderValue by remember { mutableFloatStateOf(100000f) }
    var lightDirectionX by remember { mutableFloatStateOf(-0.5f) }
    var lightDirectionY by remember { mutableFloatStateOf(-1f) }
    var lightDirectionZ by remember { mutableFloatStateOf(-0.5f) }

    androidx.activity.compose.BackHandler(enabled = selectedModel != null) {
        selectedModel = null
    }

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

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


    selectedModel?.let { model ->
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
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 60.dp,
            sheetContainerColor = DarkTheme().copy(alpha = 0.5f),
            sheetContentColor = DarkTheme_text(),
            modifier = Modifier,
            sheetContent = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .heightIn(max = 200.dp)
                ) {
                    item {

                        Button(
                            onClick = { selectedModel = null },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Row {
                                Icon(Icons.TwoTone.ArrowBack, contentDescription = "Close Model")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Close Model")
                            }
                        }

                        Text(
                            text = "Lighting Controls",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text("Light Intensity: ${sliderValue.toInt()}")
                        Slider(
                            value = sliderValue,
                            onValueChange = { sliderValue = it },
                            valueRange = 1000f..200000f,
                            steps = 50,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Light Direction X: ${"%.2f".format(lightDirectionX)}")
                        Slider(
                            value = lightDirectionX,
                            onValueChange = { lightDirectionX = it },
                            valueRange = -1f..1f,
                            steps = 40,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Light Direction Y: ${"%.2f".format(lightDirectionY)}")
                        Slider(
                            value = lightDirectionY,
                            onValueChange = { lightDirectionY = it },
                            valueRange = -1f..1f,
                            steps = 40,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Light Direction Z: ${"%.2f".format(lightDirectionZ)}")
                        Slider(
                            value = lightDirectionZ,
                            onValueChange = { lightDirectionZ = it },
                            valueRange = -1f..1f,
                            steps = 40,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        ) {
            Column(modifier = modifier.fillMaxSize()) {

                ModelViewerScreen(
                    modelPath = model.modelPath,
                    glbViewer = glbViewer,
                    lightIntensity = sliderValue,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    } ?: run {
        Box(
            Modifier
                .fillMaxSize()
                .background(DarkTheme())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            mainViewModel.logout()
                        }
                    }
                ) {
                    Text("Logout")
                }
                if (getModels.isEmpty()) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No Models Available")


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

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(getModels.size) { index ->
                            val model = getModels[index]
                            var expanded by remember { mutableStateOf(false) }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                onClick = {
                                    try {
                                        selectedModel = model
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            ) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = model.modelName,
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier.weight(1f)
                                            )

                                            if (userRole == Roles.ADMIN) {
                                                IconButton(onClick = { expanded = true }) {
                                                    Icon(
                                                        Icons.Default.MoreVert,
                                                        contentDescription = "More options"
                                                    )
                                                }
                                            }
                                        }

                                        // Dropdown menu
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            modifier = Modifier.background(DarkTheme())
                                        ) {

                                            if (userRole == Roles.ADMIN) {
                                                DropdownMenuItem(
                                                    text = { Text("Edit") },
                                                    onClick = {
                                                        // Add edit functionality
                                                        expanded = false
                                                    },
                                                    leadingIcon = {
                                                        Icon(Icons.Default.Edit, contentDescription = null)
                                                    }
                                                )

                                                DropdownMenuItem(
                                                    text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                                    onClick = {
                                                        coroutineScope.launch(Dispatchers.IO) {
                                                            mainViewModel.deleteModel(model)
                                                        }
                                                        expanded = false
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            Icons.Default.Delete,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.error
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
    }
}



