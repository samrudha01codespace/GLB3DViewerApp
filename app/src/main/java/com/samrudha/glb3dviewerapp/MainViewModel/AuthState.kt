package com.samrudha.glb3dviewerapp.MainViewModel

import com.samrudha.glb3dviewerapp.Database.Entity
import com.samrudha.glb3dviewerapp.Roles

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: Entity, val role: Roles) : AuthState()
    data class Error(val message: String) : AuthState()
}