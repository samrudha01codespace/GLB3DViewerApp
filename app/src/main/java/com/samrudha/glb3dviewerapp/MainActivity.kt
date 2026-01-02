package com.samrudha.glb3dviewerapp

import android.os.Bundle
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.samrudha.glb3dviewerapp.MainViewModel.GLBModelViewer
import com.samrudha.glb3dviewerapp.ui.theme.GLB3DViewerAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GLB3DViewerAppTheme {
                var lightIntensity by remember { mutableFloatStateOf(100000f) }
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                val glbViewer = remember {
                    GLBModelViewer(this, lifecycleOwner)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        ModelViewerScreen("sample.glb", glbViewer,lightIntensity, modifier = Modifier.wrapContentSize())

                        Text(
                            text = "Light Intensity: ${(lightIntensity / 1000).toInt()}K",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Slider(
                            value = lightIntensity,
                            onValueChange = { newValue ->
                                lightIntensity = newValue
                                glbViewer.setupLighting(
                                    intensity = newValue,
                                    colorTemperature = 6500,
                                    directionX = -0.5f,
                                    directionY = -1f,
                                    directionZ = -0.5f
                                )
                            },
                            valueRange = 10000f..200000f, // Range from dim to very bright
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModelViewerScreen(modelPath: String, glbViewer: GLBModelViewer,lightIntensity: Float,modifier: Modifier = Modifier) {

    AndroidView(
        factory = { ctx ->
            SurfaceView(ctx).also { surfaceView ->
                glbViewer.initialize(surfaceView)
                glbViewer.loadModelFromAssets(modelPath)

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


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GLB3DViewerAppTheme {
        Greeting("Android")
    }
}