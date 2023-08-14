package com.abdulrahman.ecommerce.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    private val _updateImage = MutableStateFlow<Resource<Unit>>(Resource.Unspecified())
    val updateImage: Flow<Resource<Unit>> = _updateImage

    private val _updateFirstName = MutableStateFlow<Resource<Unit>>(Resource.Unspecified())
    val updateFirstName: Flow<Resource<Unit>> = _updateFirstName

    private val _updateLastName = MutableStateFlow<Resource<Unit>>(Resource.Unspecified())
    val updateLastName: Flow<Resource<Unit>> = _updateLastName





    private fun getProfileInfo() {
        db.collection("user").document(auth.uid!!).get().addOnSuccessListener { document ->
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

    private fun updateUserImage(selectedImage: Uri) {
        if (selectedImage != Uri.EMPTY){
            val imageRef = storage.child("users/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(selectedImage)

            uploadTask.addOnSuccessListener {
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
//                Log.d("editProfileVM", "upload task ${it.toString()}")
                    db.collection("user").document(auth.uid!!).update("imagePath", it.toString())
                    viewModelScope.launch {
                        _updateImage.emit(Resource.Success(Unit))
                    }
                }
            }.addOnFailureListener {
//            Log.d("editProfileVM", it.message.toString())
                viewModelScope.launch {
                    _updateImage.emit(Resource.Error(it.message))
                }
            }
        }else{
            viewModelScope.launch {
                _updateImage.emit(Resource.Success(Unit))
//                Log.d("editProfileVM", "else branch occurs")

            }
        }

    }

    private fun updateUserName(firstName: String, lastName: String) {
        db.collection("user").document(auth.uid!!).update("firstName", firstName)
            .addOnSuccessListener {
//                Log.d("editProfileVM", "first name updated successfully ")
                viewModelScope.launch {
                    _updateFirstName.emit(Resource.Success(Unit))
                }

            }.addOnFailureListener {
//                Log.d("editProfileVM", "first name $it")
                viewModelScope.launch {
                    _updateFirstName.emit(Resource.Error(it.message))
                }
            }
        db.collection("user").document(auth.uid!!).update("lastName", lastName)
            .addOnSuccessListener {
//                Log.d("editProfileVM", "lastName updated successfully ")
                viewModelScope.launch {
                    _updateLastName.emit(Resource.Success(Unit))
                }

            }.addOnFailureListener {
//                Log.d("editProfileVM", "lastName $it")
                viewModelScope.launch {
                    _updateLastName.emit(Resource.Error(it.message))
                }
            }
    }



     fun saveUserInfo(firstName: String, lastName: String, selectedImage: Uri) {
        updateUserName(firstName, lastName)
         updateUserImage(selectedImage)
    }
}




