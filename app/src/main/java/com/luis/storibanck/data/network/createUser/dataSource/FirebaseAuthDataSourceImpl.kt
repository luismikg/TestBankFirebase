package com.luis.storibanck.data.network.createUser.dataSource

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.luis.storibanck.data.network.request.RegisterRequest
import com.luis.storibanck.domain.model.MovementInfo
import com.luis.storibanck.domain.model.TotalInfo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
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
        return firebaseAuth.uid?.let { uid ->
            val resultImageID = databaseReference.child(uid).child("urlImageID").get().await()
            (resultImageID.value as String).isNotEmpty()
        } ?: false
    }

    override suspend fun saveID(uri: Uri): Result<String> {
        val storageReference = firebaseStorage.getReference("images")
        return firebaseAuth.uid?.let { uid ->
            val resultPutFile = storageReference.child(uid).putFile(uri).await()
            val uriImage = resultPutFile.metadata?.reference?.downloadUrl?.await()

            uriImage?.let {
                Result.success(uriImage.toString())
            } ?: Result.failure(Exception("Error saving ID"))
        } ?: Result.failure(Exception("Error saving ID, UID not available"))
    }

    private suspend fun makeLoginAfterRegister(registerRequest: RegisterRequest): Result<String> {
        val resultLogin = makeLogin(registerRequest.email, registerRequest.password)
        resultLogin.onSuccess { firebaseUser ->
            firebaseUser?.uid?.let { uid ->
                val resultSave = saveDataUser(registerRequest, uid)
                return when {
                    resultSave.isSuccess -> Result.success(uid)
                    else -> resultSave
                }
            } ?: return Result.failure(Exception("login failed, UID not available"))
        }
        resultLogin.onFailure {
            return Result.failure(resultLogin.exceptionOrNull() ?: Exception("login failed"))
        }

        return Result.failure(Exception("login failed"))
    }

    private suspend fun saveDataUser(
        registerRequest: RegisterRequest,
        uid: String
    ): Result<String> {
        val databaseReference = firebaseDatabase.getReference("user")
        databaseReference.child(uid).setValue(registerRequest).await()
        return Result.success(uid)
    }

    override suspend fun updateData(uri: String): Result<Boolean> {
        val databaseReference = firebaseDatabase.getReference("user")

        return firebaseAuth.uid?.let { uid ->
            databaseReference.child(uid).child("urlImageID").setValue(uri).await()
            Result.success(true)
        } ?: Result.failure(Exception("Error updating ID, UID not available"))
    }

    override fun fetchMovement(
        stateMovement: MutableLiveData<List<MovementInfo>>,
        stateHead: MutableLiveData<TotalInfo>
    ) {
        val databaseReferenceMovement = firebaseDatabase.getReference("movement")
        val databaseReferenceHead = firebaseDatabase.getReference("head")

        databaseReferenceMovement.orderByChild("order")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items =
                        snapshot.children.mapNotNull { it.getValue(MovementInfo::class.java) }
                    stateMovement.postValue(items)
                }

                override fun onCancelled(error: DatabaseError) {
                    stateMovement.postValue(emptyList())
                }
            })

        databaseReferenceHead.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(TotalInfo::class.java) }
                stateHead.postValue(items.firstOrNull() ?: TotalInfo())
            }

            override fun onCancelled(error: DatabaseError) {
                stateMovement.postValue(emptyList())
            }
        })
    }
}