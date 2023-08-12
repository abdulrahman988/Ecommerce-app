package com.abdulrahman.ecommerce.fragments

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : ViewModel() {

    init {
        getProfileInfo()
    }

    private val _profileImg = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val profileImg: Flow<Resource<String>> = _profileImg

    private val _firstName = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val firstName: Flow<Resource<String>> = _firstName

    private val _lastName = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val lastName: Flow<Resource<String>> = _lastName

    private val _mail = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val mail: Flow<Resource<String>> = _mail


    private fun getProfileInfo() {
        db.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener { document ->
                val imagePath = document.get("imagePath").toString()
                val firstName = document.get("firstName").toString()
                val lastName = document.get("lastName").toString()
                val email = document.get("email").toString()

                viewModelScope.launch {
                    _profileImg.emit(Resource.Success(imagePath))
                    _firstName.emit(Resource.Success(firstName))
                    _lastName.emit(Resource.Success(lastName))
                    _mail.emit(Resource.Success(email))
                }
            }.addOnFailureListener { e ->
                viewModelScope.launch {
                    _profileImg.emit(Resource.Error(e.message))
                    _firstName.emit(Resource.Error(e.message))
                    _lastName.emit(Resource.Error(e.message))
                    _mail.emit(Resource.Error(e.message))
                }
            }
    }

     fun saveUserInfo(firstName: String, lastName: String, selectedImage: Uri) {
        val imageRef = storage.child("products/images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(selectedImage)

        uploadTask
            .addOnSuccessListener {
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
//                    selectedImagesListString.add(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("asd", it.message.toString())
            }


    }


}





