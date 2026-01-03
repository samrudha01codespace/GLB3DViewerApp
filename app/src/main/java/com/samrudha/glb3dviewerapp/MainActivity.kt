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
import com.samrudha.glb3dviewerapp.Database.AppDatabase
import com.samrudha.glb3dviewerapp.Database.DAO
import com.samrudha.glb3dviewerapp.Design.AuthScreen
import com.samrudha.glb3dviewerapp.Design.mainUI

import com.samrudha.glb3dviewerapp.MainViewModel.GLBModelViewer
import com.samrudha.glb3dviewerapp.ui.theme.GLB3DViewerAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val dao: DAO = database.dao()
        setContent {
            GLB3DViewerAppTheme {
                var lightIntensity by remember { mutableFloatStateOf(100000f) }
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                val glbViewer = remember {
                    GLBModelViewer(this, lifecycleOwner)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainUI(
                        glbViewer = glbViewer,
                        lightIntensity = lightIntensity,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
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