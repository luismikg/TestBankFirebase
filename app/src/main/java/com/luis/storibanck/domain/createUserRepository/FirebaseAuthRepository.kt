package com.luis.storibanck.domain.createUserRepository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.luis.storibanck.domain.model.MovementInfo
import com.luis.storibanck.domain.model.RegisterModel
import com.luis.storibanck.domain.model.TotalInfo

interface FirebaseAuthRepository {
    suspend fun createUser(registerModel: RegisterModel): Result<String>
    suspend fun makeLogin(email: String, password: String): Result<Boolean>
    suspend fun saveID(uri: Uri): Result<Boolean>

    fun fetchMovement(
        stateMovement: MutableLiveData<List<MovementInfo>>,
        stateHead: MutableLiveData<TotalInfo>
    )
}