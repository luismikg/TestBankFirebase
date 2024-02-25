package com.luis.storibanck.data.network.createUser

import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.luis.storibanck.data.network.createUser.dataSource.FirebaseAuthDataSource
import com.luis.storibanck.data.network.request.RegisterRequest
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import com.luis.storibanck.domain.model.RegisterModel
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : FirebaseAuthRepository {
    override suspend fun createUser(registerModel: RegisterModel): Result<FirebaseUser?> {
        return firebaseAuthDataSource.createUser(mapper(registerModel))
    }

    override suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthDataSource.makeLogin(email, password)
    }

    private fun mapper(registerModel: RegisterModel): RegisterRequest {
        return Gson().fromJson(Gson().toJson(registerModel), RegisterRequest::class.java)
    }
}