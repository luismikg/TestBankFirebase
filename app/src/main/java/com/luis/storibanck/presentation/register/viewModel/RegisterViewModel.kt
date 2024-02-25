package com.luis.storibanck.presentation.register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.luis.storibanck.domain.useCases.CreateUserWithEmailAndPasswordUseCase
import com.luis.storibanck.domain.useCases.ValidAuthExceptionUseCase
import com.luis.storibanck.presentation.utils.Errors
import com.luis.storibanck.presentation.register.states.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val validAuthExceptionUseCase: ValidAuthExceptionUseCase
) : ViewModel() {

    private lateinit var errors: Errors

    private var _state = MutableStateFlow<RegisterState>(RegisterState.Starting)
    val state: StateFlow<RegisterState> = _state

    fun register(name: String, email: String, password: String, passwordConfirmation: String) {
        _state.value = RegisterState.Loading

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {
            if (password == passwordConfirmation) {
                viewModelScope.launch {
                    val result = createUserWithEmailAndPasswordUseCase(email, password)
                    _state.value = when {
                        result.isSuccess -> RegisterState.Success(name)
                        else -> RegisterState.Error(validException(result))
                    }
                }
            } else {
                _state.value = RegisterState.Error(errors.passwordNotEquals)
            }
        } else {
            _state.value = RegisterState.Error(errors.completeAllField)
        }
    }

    private fun validException(result: Result<FirebaseUser?>): String {
        return validAuthExceptionUseCase(result, errors)
    }

    fun possibleErrors(err: Errors) {
        errors = err
    }
}