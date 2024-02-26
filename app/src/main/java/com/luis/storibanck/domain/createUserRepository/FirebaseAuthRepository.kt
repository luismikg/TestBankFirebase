package com.luis.storibanck.domain.createUserRepository

import com.luis.storibanck.domain.model.RegisterModel

interface FirebaseAuthRepository {
    suspend fun createUser(registerModel: RegisterModel): Result<String>
    suspend fun makeLogin(email: String, password: String): Result<Boolean>
}