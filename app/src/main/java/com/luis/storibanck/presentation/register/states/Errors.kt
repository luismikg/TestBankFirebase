package com.luis.storibanck.presentation.register.states

data class Errors(
    var completeAllField: String,
    var passwordNotEquals: String,
    var weakPassword: String,
    var credentialsNotValid: String,
    var userAlreadyExists: String,
)
