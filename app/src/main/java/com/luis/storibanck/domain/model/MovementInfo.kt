package com.luis.storibanck.domain.model

data class MovementInfo(
    var name: String = String(),
    var total: String = String(),
    var uri: String = String(),
    var order: Long = 0,
    var type: Long = 0
)
