package com.abdulrahman.ecommerce.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.User
import com.abdulrahman.ecommerce.util.LoginRegisterValidationResult
import com.abdulrahman.ecommerce.util.RegistrationForm
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _login = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val login: Flow<Resource<FirebaseUser>> = _login

    private val _emailValidation = Channel<LoginRegisterValidationResult>()
    val emailValidation = _emailValidation.receiveAsFlow()

    private val _passwordValidation = Channel<LoginRegisterValidationResult>()
    val passwordValidation = _passwordValidation.receiveAsFlow()

    private val _reset = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val reset: Flow<Resource<String>> = _reset

    fun loginAccountWithEmailAndPassword(email: String, password: String) {
        if (validateRegistration(email,password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Success(it.user))
                    }

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message))
                    }
                }
        }else{
            val registerForm  = RegistrationForm(email, password)
            runBlocking {
                _emailValidation.send(registerForm.validateEmail())
                _passwordValidation.send(registerForm.validatePassword())
            }
        }

    }
    fun resetPassword(email: String){
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            viewModelScope.launch {
                _reset.emit(Resource.Success(email))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _reset.emit(Resource.Error(it.message))
            }
        }
    }

    private fun validateRegistration(email: String, password: String): Boolean {
        val registrationForm = RegistrationForm(email, password)
        return registrationForm.validateEmail() is LoginRegisterValidationResult.EmailSuccess
                && registrationForm.validatePassword() is LoginRegisterValidationResult.PasswordSuccess
    }

}