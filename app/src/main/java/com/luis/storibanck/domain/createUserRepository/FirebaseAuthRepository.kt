package com.luis.storibanck.domain.createUserRepository

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthRepository {
    suspend fun createUser(email: String, password: String): Result<FirebaseUser?>
    suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?>
}