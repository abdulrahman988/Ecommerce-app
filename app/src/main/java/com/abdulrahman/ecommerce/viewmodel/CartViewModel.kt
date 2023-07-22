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

    private var total: Float = 0F

    private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProduct = _cartProduct.asStateFlow()

    private val _delete = MutableStateFlow<Resource<Void>>(Resource.Unspecified())
    val delete = _delete.asStateFlow()

    private val _price = MutableStateFlow<Resource<Float>>(Resource.Unspecified())
    val price = _price.asStateFlow()





    fun getCartProduct() {
        db.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartProduct.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    for (item in cartProducts){
                        total = if (item.product?.offerPercentage?.equals(0) == true) {
                            item.quantity * item.product.price
                        } else {
                            val discounted =
                                (item.product.offerPercentage?.times(item.product.price))!! / 100
                            item.quantity * (item.product.price - discounted)
                        }
                    }
                    viewModelScope.launch {
                        _price.emit(Resource.Success(total))
                        _cartProduct.emit(Resource.Success(cartProducts))
                    }
                }
            }
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        db.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    querySnapshot.documents[0].reference.delete()
                        .addOnFailureListener {
                            viewModelScope.launch {
                                _delete.emit(Resource.Error(it.message.toString()))
                            }
                        }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _delete.emit(Resource.Error(it.message.toString()))
                }
            }

    }


}