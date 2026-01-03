package com.samrudha.glb3dviewerapp.MainViewModel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import com.samrudha.glb3dviewerapp.Database.AppDatabase
import com.samrudha.glb3dviewerapp.Database.DAO
import com.samrudha.glb3dviewerapp.Database.Entity
import com.samrudha.glb3dviewerapp.Database.ModelDAO
import com.samrudha.glb3dviewerapp.Database.ModelEntity
import com.samrudha.glb3dviewerapp.Roles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val appRepo: AppRepo,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var _loginStatus = MutableStateFlow<Roles>(Roles.USER)
    var loginStatus = _loginStatus

    fun setLoginStatus(isLoggedIn: Roles): Roles {
        loginStatus.value = isLoggedIn

        return loginStatus.value
    }

    private val _models = MutableStateFlow<List<ModelEntity>>(emptyList())
    val models: StateFlow<List<ModelEntity>> = _models.asStateFlow()

    fun getLoginStatus(isLoggedIn: Roles): Roles {
        return loginStatus.value
    }

    private val _isAutoLoggingIn = MutableStateFlow(false)
    val isAutoLoggingIn: StateFlow<Boolean> = _isAutoLoggingIn.asStateFlow()

    init {
        loadModels()
        checkAutoLogin() // ✅ Check for saved credentials
    }

    private fun loadModels() {
        viewModelScope.launch(Dispatchers.IO) {
            _models.value = appRepo.getAllModels()
        }
    }

    // Helper Functions
    fun getFileName(context: Context, uri: Uri): String {
        var name = "model.glb"
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    fun saveFileToInternalStorage(context: Context, uri: Uri, fileName: String): String {
        val modelsDir = File(context.filesDir, "models")
        if (!modelsDir.exists()) modelsDir.mkdirs()

        val file = File(modelsDir, "${System.currentTimeMillis()}_${fileName}")

        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            _isAutoLoggingIn.value = true

            val savedEmail = appRepo.getLoggedInUserEmail()
            val savedPassword = appRepo.getPass()
            val savedRole = appRepo.getRole()

            if (savedEmail != null && savedPassword != null && savedRole != null) {
                // Auto-login with saved credentials
                _authState.value = AuthState.Loading
                val result = appRepo.login(savedRole, savedEmail, CryptoManager.hash(savedPassword))
                _authState.value = result.fold(
                    onSuccess = { user ->
                        _loginStatus.value = savedRole
                        AuthState.Success(user, savedRole)
                    },
                    onFailure = {
                        // Clear invalid credentials
                        appRepo.clearAllPreferences()
                        AuthState.Idle
                    }
                )
            }

            _isAutoLoggingIn.value = false
        }
    }

    fun login(roles: Roles, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = appRepo.login(roles, email, password)
            _authState.value = result.fold(
                onSuccess = { user ->
                    // ✅ Save credentials for auto-login
                    appRepo.setLoggedInUserEmail(email)
                    appRepo.setPass(CryptoManager.hash(password))
                    appRepo.setRole(roles)
                    _loginStatus.value = roles
                    AuthState.Success(user, roles)
                },
                onFailure = { error -> AuthState.Error(error.message ?: "Unknown Error") }
            )
        }
    }

    fun register(entity: Entity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = appRepo.register(entity)
            _authState.value = result.fold(
                onSuccess = { AuthState.Idle },
                onFailure = { error -> AuthState.Error(error.message ?: "Unknown Error") }
            )
        }
    }

    // ✅ Add logout function
    fun logout() {
        viewModelScope.launch {
            appRepo.clearAllPreferences()
            _authState.value = AuthState.Idle
            _loginStatus.value = Roles.USER
        }
    }

    fun addModels(fileName: String, filePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.addModels(fileName, filePath)
            loadModels()
        }
    }
    suspend fun getAllModels(): List<ModelEntity> {
        return appRepo.getAllModels()
    }
    fun deleteModel(modelEntity: ModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.deleteModel(modelEntity)
            loadModels()
        }
    }
    fun updateModel(modelEntity: ModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.updateModel(modelEntity)
            loadModels()
        }
    }

}