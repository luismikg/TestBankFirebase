package com.luis.storibanck.presentation.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.storibanck.domain.useCases.LoginUserUseCase
import com.luis.storibanck.domain.useCases.ValidAuthExceptionUseCase
import com.luis.storibanck.presentation.login.states.LoginState
import com.luis.storibanck.presentation.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val validAuthExceptionUseCase: ValidAuthExceptionUseCase
) : ViewModel() {

    private lateinit var errors: Errors

    private var _state = MutableStateFlow<LoginState>(LoginState.Starting)
    val state: StateFlow<LoginState> = _state

    fun makeLogin(email: String, password: String) {
        _state.value = LoginState.Loading

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                val result = loginUserUseCase(email, password)
                result.onSuccess {
                    _state.value = LoginState.Success(it)
                }
                result.onFailure {
                    _state.value = LoginState.Error(validException(result))
                }
            }
        } else {
            _state.value = LoginState.Error(errors.completeAllField)
        }
    }

    private fun validException(result: Any): String {
        return validAuthExceptionUseCase(result, errors)
    }

    fun possibleErrors(err: Errors) {
        errors = err
    }
}