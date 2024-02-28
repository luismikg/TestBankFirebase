package com.luis.storibanck.data.network.createUser.dataSource

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.data.network.request.RegisterRequest
import com.luis.storibanck.domain.model.MovementInfo
import com.luis.storibanck.domain.model.TotalInfo

interface FirebaseAuthDataSource {
    suspend fun createUser(registerRequest: RegisterRequest): Result<String>
    suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?>
    suspend fun hasPhotoID(): Boolean
    suspend fun saveID(uri: Uri): Result<String>
    suspend fun updateData(uri: String): Result<Boolean>

    fun fetchMovement(
        stateMovement: MutableLiveData<List<MovementInfo>>,
        stateHead: MutableLiveData<TotalInfo>
    )
}