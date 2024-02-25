package com.luis.storibanck.presentation.register.viewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.luis.storibanck.presentation.register.states.Errors
import com.luis.storibanck.presentation.register.states.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {

    private lateinit var errors: Errors

    private var _state = MutableStateFlow<RegisterState>(RegisterState.Starting)
    val state: StateFlow<RegisterState> = _state

    fun register(name: String, email: String, password: String, passwordConfirmation: String) {
        _state.value = RegisterState.Loading

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {
            if (password == passwordConfirmation) {
                FirebaseAuth
                    .getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _state.value = RegisterState.Success
                        } else {
                            _state.value = RegisterState.Error(validException(task))
                        }
                    }
            } else {
                _state.value = RegisterState.Error(errors.passwordNotEquals)
            }
        } else {
            _state.value = RegisterState.Error(errors.completeAllField)
        }
    }

    private fun validException(task: Task<AuthResult>): String {
        return task.exception?.let { exception ->
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

    fun possibleErrors(err: Errors) {
        errors = err
    }
}