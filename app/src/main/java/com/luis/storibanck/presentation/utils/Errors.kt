package com.luis.storibanck.presentation.utils

data class Errors(
    var completeAllField: String = String(),
    var passwordNotEquals: String = String(),
    var weakPassword: String = String(),
    var credentialsNotValid: String = String(),
    var userAlreadyExists: String = String(),
)
