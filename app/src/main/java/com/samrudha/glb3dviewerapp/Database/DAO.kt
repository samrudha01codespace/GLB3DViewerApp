package com.samrudha.glb3dviewerapp.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.samrudha.glb3dviewerapp.Roles

@Dao
interface DAO {
    @Insert
    fun registerData(user: Entity)

    @Query("SELECT * FROM Entity WHERE email = :email AND password = :password AND role = :roles LIMIT 1")
    fun loginUser(roles: Roles,email: String, password: String): Entity?
}

@Dao
interface ModelDAO {
    @Insert
    suspend fun insertModelData(modelEntity: ModelEntity)

    @Delete
    suspend fun deleteModelData(modelEntity: ModelEntity)

    @Update
    suspend fun updateModelData(modelEntity: ModelEntity)

    @Query("SELECT * FROM ModelEntity")
    fun getAllModels(): List<ModelEntity>
}