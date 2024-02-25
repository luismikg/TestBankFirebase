package com.luis.storibanck.presentation.login.states

sealed class LoginState {
    data object Starting : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()

    data class Error(val error: String) : LoginState()
}
