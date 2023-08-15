package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulrahman.ecommerce.data.CartProduct
import com.abdulrahman.ecommerce.data.Order
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    init {
        getOrders()
    }
    private val _orders = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
    val orders = _orders.asStateFlow()


    private fun getOrders() {
        db.collection("user").document(auth.uid!!).collection("order")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _orders.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    val orders = value.toObjects(Order::class.java)
                    viewModelScope.launch {
                        _orders.emit(Resource.Success(orders))
                    }
                }

            }
    }
}