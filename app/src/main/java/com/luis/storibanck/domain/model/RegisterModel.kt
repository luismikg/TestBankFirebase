package com.luis.storibanck.domain.model

data class RegisterModel(
    var name: String,
    var email: String,
    var password: String,
    var urlImageID: String = String()
)