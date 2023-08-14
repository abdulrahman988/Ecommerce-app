package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressSettingViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {
    init {
        getAddresses()
    }

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address = _address.asStateFlow()

    private val _delete = MutableStateFlow<Resource<Void>>(Resource.Unspecified())
    val delete = _delete.asStateFlow()

    private fun getAddresses() {
        db.collection("user").document(auth.uid!!).collection("address")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _address.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    val addresses = value.toObjects(Address::class.java)
                    viewModelScope.launch {
                        _address.emit(Resource.Success(addresses))
                    }
                }

            }
    }

    fun deleteAddress(address: Address) {
        db.collection("user").document(auth.uid!!).collection("address")
            .whereEqualTo("addressTitle", address.addressTitle).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents[0].reference.delete().addOnFailureListener {
                        viewModelScope.launch {
                            _delete.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }

    }


}