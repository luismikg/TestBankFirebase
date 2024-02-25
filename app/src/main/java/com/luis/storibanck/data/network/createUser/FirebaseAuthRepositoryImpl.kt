package com.luis.storibanck.data.network.createUser

import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.data.network.createUser.dataSource.FirebaseAuthDataSource
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : FirebaseAuthRepository {
    override suspend fun createUser(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthDataSource.createUser(email, password)
    }

    override suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthDataSource.makeLogin(email, password)
    }
}