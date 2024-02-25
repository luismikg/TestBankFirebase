package com.luis.storibanck.presentation.login.viewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.luis.storibanck.presentation.login.states.Errors
import com.luis.storibanck.presentation.login.states.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private lateinit var errors: Errors

    private var _state = MutableStateFlow<LoginState>(LoginState.Starting)
    val state: StateFlow<LoginState> = _state

    fun makeLogin(email: String, password: String) {
        _state.value = LoginState.Loading

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _state.value = LoginState.Success
                    } else {
                        _state.value = LoginState.Error(validException(task))
                    }
                }
        } else {
            _state.value = LoginState.Error(errors.completeAllField)
        }
    }

    private fun validException(task: Task<AuthResult>): String {
        return task.exception?.let { exception ->
            try {
                throw exception
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                errors.credentialsNotValid
            }
        } ?: errors.credentialsNotValid
    }

    fun possibleErrors(err: Errors) {
        errors = err
    }
}