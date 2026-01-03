package com.samrudha.glb3dviewerapp.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samrudha.glb3dviewerapp.MainViewModel.Roles

@Entity
data class Entity(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val role: Roles,
    val email: String,
    val password: String
)

@Entity
data class ModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val modelName: String,
    val modelPath: String,
    val thumbnailPath: String? = null
)