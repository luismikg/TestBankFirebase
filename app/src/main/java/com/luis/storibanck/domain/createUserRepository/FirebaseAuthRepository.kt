package com.luis.storibanck.domain.createUserRepository

import android.net.Uri
import com.luis.storibanck.domain.model.RegisterModel

interface FirebaseAuthRepository {
    suspend fun createUser(registerModel: RegisterModel): Result<String>
    suspend fun makeLogin(email: String, password: String): Result<Boolean>
    suspend fun saveID(uri: Uri): Result<Boolean>
}