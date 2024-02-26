package com.luis.storibanck.data.network.createUser

import android.net.Uri
import com.google.gson.Gson
import com.luis.storibanck.data.network.createUser.dataSource.FirebaseAuthDataSource
import com.luis.storibanck.data.network.request.RegisterRequest
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import com.luis.storibanck.domain.model.RegisterModel
import javax.inject.Inject
import kotlin.Exception

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : FirebaseAuthRepository {
    override suspend fun createUser(registerModel: RegisterModel): Result<String> {
        return firebaseAuthDataSource.createUser(mapper(registerModel))
    }

    override suspend fun makeLogin(email: String, password: String): Result<Boolean> {

        val result = firebaseAuthDataSource.makeLogin(email, password)
        result.onSuccess {
            return Result.success(firebaseAuthDataSource.hasPhotoID())
        }
        result.onFailure {
            return Result.failure(result.exceptionOrNull() ?: Exception("error register"))
        }

        return Result.failure(result.exceptionOrNull() ?: Exception("error register"))
    }

    override suspend fun saveID(uri: Uri): Result<Boolean> {
        val result = firebaseAuthDataSource.saveID(uri)
        result.onSuccess { uriServer ->
            return firebaseAuthDataSource.updateData(uriServer)
        }
        result.onFailure {
            return Result.failure(result.exceptionOrNull() ?: Exception("error register"))
        }

        return Result.failure(result.exceptionOrNull() ?: Exception("error register"))
    }

    private fun mapper(registerModel: RegisterModel): RegisterRequest {
        return Gson().fromJson(Gson().toJson(registerModel), RegisterRequest::class.java)
    }
}