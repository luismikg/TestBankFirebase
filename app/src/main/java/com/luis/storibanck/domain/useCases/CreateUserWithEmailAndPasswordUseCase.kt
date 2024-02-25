package com.luis.storibanck.domain.useCases

import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import javax.inject.Inject

class CreateUserWithEmailAndPasswordUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthRepository.createUser(email, password)
    }
}