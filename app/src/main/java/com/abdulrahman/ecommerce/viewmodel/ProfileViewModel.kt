package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.User
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    init {
        getProfileInfo()
    }

    private val _profileImg = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val profileImg: Flow<Resource<String>> = _profileImg

    private val _name = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val name: Flow<Resource<String>> = _name



    private val _mail = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val mail: Flow<Resource<String>> = _mail



    fun logout() {
        auth.signOut()
    }

    private fun getProfileInfo() {
        db.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {document ->
                val imagePath = document.get("imagePath").toString()
                val firstName = document.get("firstName").toString()
                val lastName = document.get("lastName").toString()
                val email = document.get("email").toString()

                viewModelScope.launch {
                    _profileImg.emit(Resource.Success(imagePath))
                    _name.emit(Resource.Success("$firstName $lastName"))
                    _mail.emit(Resource.Success(email))
                }
            }.addOnFailureListener {e ->
                viewModelScope.launch {
                    _profileImg.emit(Resource.Error(e.message))
                    _name.emit(Resource.Error(e.message))
                    _mail.emit(Resource.Error(e.message))
                }
            }
    }
}





