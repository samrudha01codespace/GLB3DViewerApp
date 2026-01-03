package com.samrudha.glb3dviewerapp.Design

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.SurfaceView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.samrudha.glb3dviewerapp.Database.ModelDAO
import com.samrudha.glb3dviewerapp.Database.ModelEntity
import com.samrudha.glb3dviewerapp.MainViewModel.GLBModelViewer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun mainUI(glbViewer: GLBModelViewer,lightIntensity: Float,modifier: Modifier = Modifier){
    var lightIntensity by remember { mutableFloatStateOf(10000f) }

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
fun manageModelsScreen(model_dao: ModelDAO,coroutineScope: CoroutineScope){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (
            model_dao.getAllModels().isEmpty()
        ){

        }
    }

}
