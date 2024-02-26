package com.luis.storibanck.domain.useCases

import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import com.luis.storibanck.domain.model.RegisterModel
import javax.inject.Inject

class CreateUserWithEmailAndPasswordUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(registerModel: RegisterModel): Result<String> {
        return firebaseAuthRepository.createUser(registerModel)
    }
}