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

    @Query("SELECT * FROM entity WHERE role = :roles AND email = :email AND password = :password LIMIT 1")
    suspend fun login(roles: Roles, email: String, password: String): Entity?

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
    suspend fun getAllModels(): List<ModelEntity>
}