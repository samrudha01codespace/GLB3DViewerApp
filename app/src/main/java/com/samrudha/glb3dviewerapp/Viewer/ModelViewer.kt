package com.samrudha.glb3dviewerapp.Viewer

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Choreographer
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.Colors
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.Fence
import com.google.android.filament.LightManager
import com.google.android.filament.Material
import com.google.android.filament.View
import com.google.android.filament.utils.KTX1Loader
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3

class GLBModelViewer(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver {

    companion object {
        init { Utils.init() }
        private const val TAG = "GLBModelViewer"
    }

    private var surfaceView: SurfaceView? = null
    private var modelViewer: ModelViewer? = null
    private var choreographer: Choreographer? = null
    private val frameCallback = FrameCallback()
    private var doubleTapDetector: GestureDetector? = null

    private var loadStartTime = 0L
    private var loadStartFence: Fence? = null
    private var currentModelPath: String? = null

    /**
     * Initialize the viewer with a SurfaceView
     */
    fun initialize(surfaceView: SurfaceView) {
        this.surfaceView = surfaceView
        this.choreographer = Choreographer.getInstance()
        this.modelViewer = ModelViewer(surfaceView)

        setupTouchListener()
        setupViewSettings()
        createIndirectLight()

        lifecycleOwner.lifecycle.addObserver(this)
    }

    private fun setupTouchListener() {
        val viewer = modelViewer ?: return
        val sv = surfaceView ?: return

        doubleTapDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // Reset camera on double tap
                viewer.transformToUnitCube()
                return true
            }
        })

        sv.setOnTouchListener { _, event ->
            viewer.onTouchEvent(event)
            doubleTapDetector?.onTouchEvent(event)
            true
        }
    }

    private fun setupViewSettings() {
        val view = modelViewer?.view ?: return
        val viewer = modelViewer ?: return

        // ✅ Set dark gray/black background for the renderer
        viewer.renderer.setClearOptions(
            viewer.renderer.clearOptions.apply {
                clearColor = floatArrayOf(0.0f, 0.0f, 0.0f, 0.5f)
                clear = true
            }
        )

        // Optimize for mobile quality
        view.renderQuality = view.renderQuality.apply {
            hdrColorBuffer = View.QualityLevel.ULTRA
        }

        // Enable dynamic resolution for better performance
        view.dynamicResolutionOptions = view.dynamicResolutionOptions.apply {
            enabled = true
            quality = View.QualityLevel.ULTRA
        }

        // Enable MSAA
        view.multiSampleAntiAliasingOptions = view.multiSampleAntiAliasingOptions.apply {
            enabled = true
        }

        // Enable FXAA
        view.antiAliasing = View.AntiAliasing.FXAA

        // Enable ambient occlusion
        view.ambientOcclusionOptions = view.ambientOcclusionOptions.apply {
            enabled = true
        }

        // Enable bloom for realism
        view.bloomOptions = view.bloomOptions.apply {
            enabled = true
        }
    }

    private fun createIndirectLight() {
        val viewer = modelViewer ?: return
        val engine = viewer.engine
        val scene = viewer.scene

        try {
            val ibl = "default_env"

            // Load IBL (Image-Based Lighting)
            readAsset("envs/$ibl/${ibl}_ibl.ktx")?.let { buffer ->
                val bundle = KTX1Loader.createIndirectLight(engine, buffer)
                scene.indirectLight = bundle.indirectLight
                viewer.indirectLightCubemap = bundle.cubemap
                scene.indirectLight?.intensity = 30_000.0f
            }

            // Load Skybox
            readAsset("envs/$ibl/${ibl}_skybox.ktx")?.let { buffer ->
                val bundle = KTX1Loader.createSkybox(engine, buffer)
                scene.skybox = bundle.skybox
                viewer.skyboxCubemap = bundle.cubemap
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not load default environment: ${e.message}")
        }
    }

    /**
     * Load a GLB model from the assets folder
     * @param assetPath Path to the GLB file in assets (e.g., "models/model.glb")
     */
    fun loadModelFromAssets(assetPath: String) {
        val viewer = modelViewer ?: run {
            Log.e(TAG, "ModelViewer not initialized")
            return
        }

        try {
            val buffer = readAsset(assetPath)
            if (buffer != null) {
                viewer.destroyModel()
                viewer.loadModelGlb(buffer)
                viewer.transformToUnitCube()
                currentModelPath = assetPath
                loadStartTime = System.nanoTime()
                loadStartFence = viewer.engine.createFence()
                Log.i(TAG, "Loaded model from assets: $assetPath")
            } else {
                Log.e(TAG, "Failed to read asset: $assetPath")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model from assets: ${e.message}")
        }
    }

    /**
     * Load a GLB model from a URI (e.g., from file picker)
     * @param uri URI to the GLB file
     */
    fun loadModelFromUri(uri: Uri) {
        val viewer = modelViewer ?: run {
            Log.e(TAG, "ModelViewer not initialized")
            return
        }

        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                val buffer = ByteBuffer.wrap(bytes)

                viewer.destroyModel()
                viewer.loadModelGlb(buffer)
                viewer.transformToUnitCube()
                currentModelPath = uri.toString()
                loadStartTime = System.nanoTime()
                loadStartFence = viewer.engine.createFence()
                Log.i(TAG, "Loaded model from URI: $uri")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model from URI: ${e.message}")
        }
    }

    // Add these properties to the class
    private var sunlightEntity: Int = 0

    // Add this method to setup lighting
    fun setupLighting(
        intensity: Float = 100000f,
        colorTemperature: Int = 6500,
        directionX: Float = -0.5f,
        directionY: Float = -1f,
        directionZ: Float = -0.5f
    ) {
        val engine: Engine? = modelViewer?.engine

        // Remove existing light if any
        if (sunlightEntity != 0) {
            modelViewer?.scene?.removeEntity(sunlightEntity)
            engine?.destroyEntity(sunlightEntity)
        }

        // Create a directional light
        sunlightEntity = EntityManager.get().create()
        val (r, g, b) = Colors.cct(colorTemperature.toFloat())

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(r, g, b)
            .intensity(intensity)
            .direction(directionX, directionY, directionZ)
            .castShadows(true)
            .build(engine!!, sunlightEntity)

        modelViewer?.scene?.addEntity(sunlightEntity)
    }

    /**
     * Load a GLB model from a ByteBuffer
     * @param buffer ByteBuffer containing the GLB data
     * @param name Optional name for logging
     */
    fun loadModelFromBuffer(buffer: ByteBuffer, name: String = "buffer") {
        val viewer = modelViewer ?: run {
            Log.e(TAG, "ModelViewer not initialized")
            return
        }

        try {
            viewer.destroyModel()
            viewer.loadModelGlb(buffer)
            viewer.transformToUnitCube()
            currentModelPath = name
            loadStartTime = System.nanoTime()
            loadStartFence = viewer.engine.createFence()
            Log.i(TAG, "Loaded model from buffer: $name")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model from buffer: ${e.message}")
        }
    }

    private fun readAsset(assetName: String): ByteBuffer? {
        return try {
            context.assets.open(assetName).use { input ->
                val bytes = ByteArray(input.available())
                input.read(bytes)
                ByteBuffer.wrap(bytes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading asset $assetName: ${e.message}")
            null
        }
    }

    /**
     * Clear the current model
     */
    fun clearModel() {
        modelViewer?.destroyModel()
        currentModelPath = null
    }

    /**
     * Reset camera to default position
     */
    fun resetCamera() {
        modelViewer?.transformToUnitCube()
    }

    // Lifecycle callbacks
    override fun onResume(owner: LifecycleOwner) {
        choreographer?.postFrameCallback(frameCallback)
    }

    override fun onPause(owner: LifecycleOwner) {
        choreographer?.removeFrameCallback(frameCallback)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        choreographer?.removeFrameCallback(frameCallback)
        modelViewer?.destroyModel()
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()

        override fun doFrame(frameTimeNanos: Long) {
            choreographer?.postFrameCallback(this)

            val viewer = modelViewer ?: return

            // Check if model loading is complete
            loadStartFence?.let { fence ->
                if (fence.wait(Fence.Mode.FLUSH, 0) == Fence.FenceStatus.CONDITION_SATISFIED) {
                    val end = System.nanoTime()
                    val total = (end - loadStartTime) / 1_000_000
                    Log.i(TAG, "Model loaded in $total ms")
                    viewer.engine.destroyFence(fence)
                    loadStartFence = null

                    // Compile materials for better performance
                    compileMaterials()
                }
            }

            // Animate the model if it has animations
            viewer.animator?.apply {
                if (animationCount > 0) {
                    val elapsedTimeSeconds = (frameTimeNanos - startTime).toDouble() / 1_000_000_000
                    applyAnimation(0, elapsedTimeSeconds.toFloat())
                }
                updateBoneMatrices()
            }

            // Render the frame
            viewer.render(frameTimeNanos)
        }
    }

    private fun compileMaterials() {
        val viewer = modelViewer ?: return
        val materials = mutableSetOf<Material>()
        val rcm = viewer.engine.renderableManager

        viewer.scene.forEach { entity ->
            if (rcm.hasComponent(entity)) {
                val ri = rcm.getInstance(entity)
                val count = rcm.getPrimitiveCount(ri)
                for (i in 0 until count) {
                    val mi = rcm.getMaterialInstanceAt(ri, i)
                    materials.add(mi.material)
                }
            }
        }

        materials.forEach { material ->
            material.compile(
                Material.CompilerPriorityQueue.HIGH,
                Material.UserVariantFilterBit.DIRECTIONAL_LIGHTING or
                Material.UserVariantFilterBit.DYNAMIC_LIGHTING or
                Material.UserVariantFilterBit.SHADOW_RECEIVER,
                null, null
            )
            material.compile(
                Material.CompilerPriorityQueue.LOW,
                Material.UserVariantFilterBit.FOG or
                Material.UserVariantFilterBit.SKINNING or
                Material.UserVariantFilterBit.SSR or
                Material.UserVariantFilterBit.VSM,
                null, null
            )
        }
    }
}
