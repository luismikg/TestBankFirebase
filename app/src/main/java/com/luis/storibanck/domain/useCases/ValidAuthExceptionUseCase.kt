package com.luis.storibanck.domain.useCases

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.presentation.utils.Errors
import javax.inject.Inject

class ValidAuthExceptionUseCase @Inject constructor() {
    operator fun invoke(result: Result<FirebaseUser?>, errors: Errors): String {
        return result.exceptionOrNull()?.let { exception ->
            try {
                throw exception
            } catch (e: FirebaseAuthWeakPasswordException) {
                errors.weakPassword
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                errors.credentialsNotValid
            } catch (e: FirebaseAuthUserCollisionException) {
                errors.userAlreadyExists
            } catch (e: Exception) {
                errors.credentialsNotValid
            }
        } ?: errors.credentialsNotValid
    }
}