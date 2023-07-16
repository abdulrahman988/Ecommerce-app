package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import com.abdulrahman.ecommerce.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        db.collection("user")
            .document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id)
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        //the product is not in the cart first time added


                    } else {
                        //the product is already in the cart so we increase the quantity
                        val product = it.first().toObject(cartProduct::class.java)
                        if (product == cartProduct){
                            //increase the quantity

                        }else{
                            //add the product

                        }

                    }
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message))
                }
            }
    }


}