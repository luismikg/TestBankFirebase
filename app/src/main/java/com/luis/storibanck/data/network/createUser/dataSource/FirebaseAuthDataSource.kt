package com.luis.storibanck.data.network.createUser.dataSource

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.data.network.request.RegisterRequest

interface FirebaseAuthDataSource {
    suspend fun createUser(registerRequest: RegisterRequest): Result<String>
    suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?>
    suspend fun hasPhotoID(): Boolean
    suspend fun saveID(uri: Uri): Result<String>
    suspend fun updateData(uri: String): Result<Boolean>
}