package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    val auth: FirebaseAuth, val db: FirebaseFirestore
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }
        db.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get().addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        //the cart is empty there is no products in the cart
                        db.collection("user").document(auth.uid!!).collection("cart").document()
                            .set(cartProduct).addOnSuccessListener {
                                viewModelScope.launch {
                                    _addToCart.emit(Resource.Success(cartProduct))
                                }
                            }.addOnFailureListener {
                                viewModelScope.launch {
                                    _addToCart.emit(Resource.Error(it.message.toString()))
                                }
                            }
                    } else {
                        //the cart is not empty and there is one product or more in the cart
                        val cartProductStored = it.first().toObject(cartProduct::class.java)
                        if (cartProductStored?.product?.id == cartProduct.product.id) {
                            //increase the quantity
                            db.runTransaction { transition ->
                                val documentId = it.first().id
                                val documentRef =
                                    db.collection("user")
                                        .document(auth.uid!!)
                                        .collection("cart")
                                        .document(documentId)

                                val newQuantity = cartProduct.quantity + 1
                                transition.update(documentRef,"quantity",newQuantity)
//                                productObject?.let { cartProduct ->
//                                    val newQuantity = cartProduct.quantity + 1
//                                    val newProductObject = cartProduct.copy(quantity = newQuantity)
//                                    transition.set(documentRef, newProductObject)
//                                }
                            }.addOnFailureListener {
                                viewModelScope.launch {
                                    _addToCart.emit(Resource.Error(it.message.toString()))
                                }
                            }

                        } else {
                            //the cart is not empty and there is no product
                            db.collection("user").document(auth.uid!!).collection("cart").document()
                                .set(cartProduct).addOnSuccessListener {
                                    viewModelScope.launch {
                                        _addToCart.emit(Resource.Success(cartProduct))
                                    }
                                }.addOnFailureListener {
                                    viewModelScope.launch {
                                        _addToCart.emit(Resource.Error(it.message.toString()))
                                    }
                                }

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