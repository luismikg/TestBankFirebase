package com.luis.storibanck.presentation.register.states

sealed class RegisterState {
    data object Starting : RegisterState()
    data object Loading : RegisterState()
    data class Success(val user: String) : RegisterState()

    data class Error(val error: String) : RegisterState()
}
