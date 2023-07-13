package com.abdulrahman.ecommerce.util

import android.provider.ContactsContract.CommonDataKinds.Email
import com.abdulrahman.ecommerce.data.User

data class RegistrationForm(
    val email: String,
    val password: String
){

    fun validateEmail(): LoginRegisterValidationResult {

        // Validate email
        if (email.isEmpty()) {
            return LoginRegisterValidationResult.EmailError("Email is required")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return LoginRegisterValidationResult.EmailError("Invalid email format")
        }
        return LoginRegisterValidationResult.EmailSuccess
    }

    fun validatePassword(): LoginRegisterValidationResult {
        // Validate password
        if (password.isEmpty()) {
            return LoginRegisterValidationResult.PasswordError("Password is required")
        }
        if (password.length < 6) {
            return LoginRegisterValidationResult.PasswordError("Password should be at least 6 characters long")
        }
        return LoginRegisterValidationResult.PasswordSuccess

    }
}


