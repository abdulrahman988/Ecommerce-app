package com.abdulrahman.ecommerce.util


sealed class LoginRegisterValidationResult {
    object EmailSuccess : LoginRegisterValidationResult()
    object PasswordSuccess : LoginRegisterValidationResult()
    data class EmailError(val errorMessage: String) : LoginRegisterValidationResult()
    data class PasswordError(val errorMessage: String) : LoginRegisterValidationResult()
}