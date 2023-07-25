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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {


    private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProduct = _cartProduct.asStateFlow()

    private var price = 0f
    private var discounted = 0f


    private val _delete = MutableStateFlow<Resource<Void>>(Resource.Unspecified())
    val delete = _delete.asStateFlow()

     val productPrice = cartProduct.map {
        when (it){
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }


    private fun getProductPrice(cartProduct: CartProduct): Float {
         if (cartProduct.product.offerPercentage?.equals(0) == true){
            price = cartProduct.quantity.toFloat() * cartProduct.product.price
        }else{
            discounted = (cartProduct.product.offerPercentage?.times(cartProduct.product.price))!! / 100
            price = cartProduct.quantity * (cartProduct.product.price - discounted)
        }
        return price
    }

    private fun calculatePrice(cartProducts: List<CartProduct>): Float{

         return cartProducts.sumByDouble { cartProduct ->
             getProductPrice(cartProduct).toDouble()
         }.toFloat()
    }


    fun getCartProduct() {
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