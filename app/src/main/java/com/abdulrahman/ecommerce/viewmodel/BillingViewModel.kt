package com.abdulrahman.ecommerce.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.Address
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.data.Order
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.payment.NetworkPayment
import com.abdulrahman.ecommerce.payment.ThirdPartyResponse
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

    private val _orderUser = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val orderUser = _orderUser.asStateFlow()

    private val _orderAdmin = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val orderAdmin = _orderAdmin.asStateFlow()

    private var price = 0f


    private var discounted = 0f
    val productPrice = product.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }

            else -> null
        }
    }


    private fun calculatePrice(cartProducts: List<CartProduct>): Float {

        return cartProducts.sumByDouble { cartProduct ->
            getProductPrice(cartProduct).toDouble()
        }.toFloat()
    }

    private fun getProductPrice(cartProduct: CartProduct): Float {
        if (cartProduct.product.offerPercentage?.equals(0) == true) {
            price = cartProduct.quantity.toFloat() * cartProduct.product.price
        } else {
            discounted =
                (cartProduct.product.offerPercentage?.times(cartProduct.product.price))!! / 100
            price = cartProduct.quantity * (cartProduct.product.price - discounted)
        }
        return price
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

    //payment

    fun createOrder(order: Order) {
        //create order collection to add orders to it where in admin app can see all orders added
        db.collection("order")
            .document(order.orderId)
            .set(order)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _orderAdmin.value = Resource.Success(order)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _orderAdmin.value = Resource.Error(it.message.toString())
                }
            }

        //add order to user
        db.collection("user").document(auth.uid!!).collection("order").document().set(order)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _orderUser.value = Resource.Success(order)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _orderUser.value = Resource.Error(it.message.toString())
                }
            }
    }

    fun deleteCartProduct() {
        db.collection("user").document(auth.uid!!).collection("cart").get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    document.reference.delete()
                }
            }
    }


    fun getContactEmail(): String? {
        return auth.currentUser?.email
    }

}