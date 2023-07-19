package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProduct = _cartProduct.asStateFlow()


     fun getCartProduct() {
        viewModelScope.launch {
            _cartProduct.emit(Resource.Loading())
        }
        db.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartProduct.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProduct.emit(Resource.Success(cartProducts))
                    }
                }
            }
    }





















}