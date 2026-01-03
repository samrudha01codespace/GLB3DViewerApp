package com.samrudha.glb3dviewerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.samrudha.glb3dviewerapp.Design.mainUI
import com.samrudha.glb3dviewerapp.MainViewModel.AppRepo
import com.samrudha.glb3dviewerapp.MainViewModel.GLBModelViewer
import com.samrudha.glb3dviewerapp.MainViewModel.MainViewModel
import com.samrudha.glb3dviewerapp.ui.theme.GLB3DViewerAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    private var appRepo: AppRepo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appRepo = AppRepo(this)
        viewModel = MainViewModel(appRepo!!)
        setContent {
            GLB3DViewerAppTheme {
                val authState by viewModel.authState.collectAsState()
                var lightIntensity by remember { mutableFloatStateOf(100000f) }
                val lifecycleOwner = LocalLifecycleOwner.current
                val glbViewer = remember {
                    GLBModelViewer(this, lifecycleOwner)
                }
                val coroutineScope = rememberCoroutineScope()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainUI(
                        glbViewer = glbViewer,
                        modifier = Modifier.padding(innerPadding),
                        authState = authState,
                        coroutineScope = coroutineScope,
                        viewModel = viewModel,
                        lightIntensity = lightIntensity,
                        innerPadding = innerPadding.calculateTopPadding()
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