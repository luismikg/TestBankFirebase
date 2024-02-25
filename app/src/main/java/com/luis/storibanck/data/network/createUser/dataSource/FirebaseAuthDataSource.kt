package com.luis.storibanck.data.network.createUser.dataSource

import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.data.network.request.RegisterRequest

interface FirebaseAuthDataSource {
    suspend fun createUser(registerRequest: RegisterRequest): Result<FirebaseUser?>
    suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?>
}