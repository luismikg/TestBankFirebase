package com.luis.storibanck.domain.createUserRepository

import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.domain.model.RegisterModel

interface FirebaseAuthRepository {
    suspend fun createUser(registerModel: RegisterModel): Result<FirebaseUser?>
    suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?>
}