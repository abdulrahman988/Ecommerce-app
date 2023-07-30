package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val error = _error.asStateFlow()

    fun addAddress(address: Address) {
        if (validateInputs(address)) {
            viewModelScope.launch {
                _addNewAddress.emit(Resource.Loading())
            }
            db.collection("user").document(auth.uid!!).collection("address").document().set(address)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Success(address))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Error(it.message.toString()))
                    }
                }
        }else{
            viewModelScope.launch {
                _error.emit(Resource.Error("All fields are required"))
            }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.state.trim().isNotEmpty() &&
                address.fullName.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty()

    }

}