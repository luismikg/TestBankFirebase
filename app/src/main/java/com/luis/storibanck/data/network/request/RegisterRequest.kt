package com.luis.storibanck.data.network.request

data class RegisterRequest(
    var name: String,
    var email: String,
    var password: String,
    var urlImageID: String = String()
)