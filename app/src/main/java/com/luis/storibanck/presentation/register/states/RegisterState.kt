package com.luis.storibanck.presentation.register.states

sealed class RegisterState {
    data object Starting : RegisterState()
    data object Loading : RegisterState()
    data object Success : RegisterState()

    data class Error(val error: String) : RegisterState()
}
