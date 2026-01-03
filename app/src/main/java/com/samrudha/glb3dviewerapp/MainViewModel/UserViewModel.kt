package com.samrudha.glb3dviewerapp.MainViewModel

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

class MainViewModel(
    private val appRepo: AppRepo,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var _loginStatus = Roles.USER
    var loginStatus = _loginStatus

    fun setLoginStatus(isLoggedIn: Roles): Roles {
        loginStatus = isLoggedIn

        return loginStatus
    }

    fun getLoginStatus(isLoggedIn: Roles): Roles {
        return loginStatus
    }

    fun login(roles: Roles, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = appRepo.login(roles, email, password)
            _authState.value = result.fold(
                onSuccess = { user ->
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

    fun addModels(fileName: String, filePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.addModels(fileName, filePath)
        }
    }
    fun getAllModels(): List<ModelEntity> {
        return appRepo.getAllModels()
    }
    fun deleteModel(modelEntity: ModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.deleteModel(modelEntity)
        }
    }
    fun updateModel(modelEntity: ModelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.updateModel(modelEntity)
        }
    }


}