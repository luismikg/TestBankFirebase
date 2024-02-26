package com.luis.storibanck.presentation.idPhoto.states

sealed class IdPhotoState {
    data object Starting : IdPhotoState()
    data object Loading : IdPhotoState()
    data object Success : IdPhotoState()
    data class Error(val error: String) : IdPhotoState()
}
