package com.samrudha.glb3dviewerapp.Repo

import android.content.Context
import com.samrudha.glb3dviewerapp.Database.AppDatabase
import com.samrudha.glb3dviewerapp.Database.Entity
import com.samrudha.glb3dviewerapp.Database.ModelEntity
import com.samrudha.glb3dviewerapp.MainViewModel.CryptoManager
import com.samrudha.glb3dviewerapp.MainViewModel.Roles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepo(context: Context) {

    val appDatabase = AppDatabase.getInstance(context)
    val modelDao = appDatabase.modelDao()
    val dao = appDatabase.dao()

    val sharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    fun getLoggedInUserEmail(): String? {
        return sharedPreferences.getString("LOGGED_IN_USER_EMAIL", null)
    }
    fun setLoggedInUserEmail(email: String) {
        sharedPreferences.edit().putString("LOGGED_IN_USER_EMAIL", email).apply()
    }
    fun clearLoggedInUserEmail() {
        sharedPreferences.edit().remove("LOGGED_IN_USER_EMAIL").apply()
    }
    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }
    fun setPass(password: String) {
        sharedPreferences.edit().putString("PASSWORD", password).apply()
    }
    fun getPass(): String? {
        return sharedPreferences.getString("PASSWORD", null)
    }
    fun setRole(role: Roles) {
        sharedPreferences.edit().putString("ROLE", role.name).apply()
    }
    fun getRole(): Roles? {
        val roleName = sharedPreferences.getString("ROLE", null)
        return roleName?.let { Roles.valueOf(it) }
    }

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