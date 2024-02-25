package com.luis.storibanck.data.network.createUser.dataSource

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthDataSource {
    suspend fun createUser(email: String, password: String): Result<FirebaseUser?>
    suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?>
}