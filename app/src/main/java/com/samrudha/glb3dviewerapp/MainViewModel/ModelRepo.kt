package com.samrudha.glb3dviewerapp.MainViewModel

import android.content.Context
import com.samrudha.glb3dviewerapp.Database.AppDatabase
import com.samrudha.glb3dviewerapp.Database.Entity
import com.samrudha.glb3dviewerapp.Database.ModelEntity
import com.samrudha.glb3dviewerapp.Roles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepo(context: Context) {

    val appDatabase = AppDatabase.getInstance(context)
    val modelDao = appDatabase.modelDao()
    val dao = appDatabase.dao()

    suspend fun login(roles: Roles, email: String, password: String): Result<Entity> =
        withContext(Dispatchers.IO) {
            try {
                val hashedPassword = CryptoManager.hash(password)  // ✅ Hash input password

                val user = dao.login(roles, email, hashedPassword)

                user?.let { Result.success(it) }
                    ?: Result.failure(Exception("Invalid credentials"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }




    suspend fun register(entity: Entity): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dao.registerData(entity)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun addModels(fileName: String, filePath: String) {
        modelDao.insertModelData(
            ModelEntity(
                modelName = fileName,
                modelPath = filePath
            )
        )
    }

    suspend fun getAllModels(): List<ModelEntity> {
        return modelDao.getAllModels()
    }

    suspend fun deleteModel(modelEntity: ModelEntity) {
        modelDao.deleteModelData(modelEntity)
    }

    suspend fun updateModel(modelEntity: ModelEntity) {
        modelDao.updateModelData(modelEntity)
    }

    suspend fun viewModelDetails(modelEntity: ModelEntity): String {
        return "Model Name: ${modelEntity.modelName}\nModel Path: ${modelEntity.modelPath}"
    }

}