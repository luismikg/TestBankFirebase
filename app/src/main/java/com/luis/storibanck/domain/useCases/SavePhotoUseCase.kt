package com.luis.storibanck.domain.useCases

import android.net.Uri
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(uri: Uri): Result<Boolean> {
        return firebaseAuthRepository.saveID(uri)
    }
}