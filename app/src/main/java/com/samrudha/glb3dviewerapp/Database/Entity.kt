package com.samrudha.glb3dviewerapp.Database

import androidx.room.Entity
import com.samrudha.glb3dviewerapp.Roles

@Entity
data class Entity(
    val role: Roles,
    val email: String,
    val password: String
)
