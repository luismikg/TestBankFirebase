package com.luis.storibanck.presentation.login.states

sealed class LoginState {
    data object Starting : LoginState()
    data object Loading : LoginState()
    data class Success(val hasPhotoID: Boolean) : LoginState()

    data class Error(val error: String) : LoginState()
}
