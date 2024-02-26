package com.luis.storibanck.data.network.createUser.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.luis.storibanck.data.network.request.RegisterRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : FirebaseAuthDataSource {
    override suspend fun createUser(registerRequest: RegisterRequest): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(
                registerRequest.email,
                registerRequest.password
            ).await()
            if (result.user != null) {
                makeLoginAfterRegister(registerRequest)
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

    override suspend fun hasPhotoID(): Boolean {
        val databaseReference = firebaseDatabase.getReference("user")
        var result = false
        firebaseAuth.uid?.let { uid ->
            databaseReference.child(uid).child("urlImageID").get()
                .addOnSuccessListener {
                    result = (it.value as String).isNotEmpty()
                }.addOnFailureListener {
                    result = false
                }.await()
        }

        return result
    }

    private suspend fun makeLoginAfterRegister(registerRequest: RegisterRequest): Result<String> {
        val resultLogin = makeLogin(registerRequest.email, registerRequest.password)
        when {
            resultLogin.isSuccess ->
                resultLogin.onSuccess { firebaseUser ->
                    firebaseUser?.uid?.let { uid ->
                        val resultSave = saveDataUser(registerRequest, uid)
                        return when {
                            resultSave.isSuccess -> Result.success(uid)
                            else -> resultSave
                        }
                    }
                }

            else -> return Result.failure(
                resultLogin.exceptionOrNull() ?: Exception("login failed")
            )
        }
        return Result.failure(Exception("login failed"))
    }

    private suspend fun saveDataUser(
        registerRequest: RegisterRequest,
        uid: String
    ): Result<String> {
        val databaseReference = firebaseDatabase.getReference("user")
        var result = Result.success("")

        databaseReference.child(uid).setValue(registerRequest)
            .addOnSuccessListener {
                result = Result.success("")
            }
            .addOnFailureListener {
                result = Result.failure(Exception("Error in data base"))
            }
            .await()

        return result
    }
}