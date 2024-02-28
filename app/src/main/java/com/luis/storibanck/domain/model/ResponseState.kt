package com.luis.storibanck.domain.model

sealed class ResponseState {
    data object Loading : ResponseState()
    data class Success(val movementList: List<MovementInfo>) : ResponseState()
}