package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) : ViewModel() {
    init {
        getAddresses()
        getProducts()
    }

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address = _address.asStateFlow()

    private val _product = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val product = _product.asStateFlow()

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

    private fun getProducts() {
        db.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _product.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    val products = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _product.emit(Resource.Success(products))
                    }
                }

            }

    }

}