package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.abdulrahman.ecommerce.data.User
import com.abdulrahman.ecommerce.util.Constants.USER_COLLECTION
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.util.RegistrationForm
import com.abdulrahman.ecommerce.util.LoginRegisterValidationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _emailValidation = Channel<LoginRegisterValidationResult>()
    val emailValidation = _emailValidation.receiveAsFlow()

    private val _passwordValidation = Channel<LoginRegisterValidationResult>()
    val passwordValidation = _passwordValidation.receiveAsFlow()


    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (validateRegistration(user,password)){
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user.let {
                        if (it != null) {
                            saveUserInfo(it.uid,user)
                        }
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }else{
            val registerForm  = RegistrationForm(user.email, password)
            runBlocking {
                _emailValidation.send(registerForm.validateEmail())
                _passwordValidation.send(registerForm.validatePassword())
            }

        }
    }

    private fun saveUserInfo(userUid: String ,user: User) {
        firestore.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun validateRegistration(user: User, password: String): Boolean {
        val registrationForm = RegistrationForm(user.email, password)
        return registrationForm.validateEmail() is LoginRegisterValidationResult.EmailSuccess
                && registrationForm.validatePassword() is LoginRegisterValidationResult.PasswordSuccess
    }

}

