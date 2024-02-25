package com.luis.storibanck.data.network.createUser.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {
    override suspend fun createUser(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Result.success(result.user)
            } else {
                Result.failure(Exception("login failed"))
            }
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(e)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(e)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun makeLogin(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Result.success(result.user)
            } else {
                Result.failure(Exception("login failed"))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(e)
        }
    }
}