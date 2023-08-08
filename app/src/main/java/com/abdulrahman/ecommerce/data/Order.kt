package com.abdulrahman.ecommerce.data

import com.abdulrahman.ecommerce.util.PaymentType

data class Order(
    val orderId: String,
    val totalPrice: Float,
    val contact_email: String,
    val createdAt: String,
    val productList: List<CartProduct>,
    val address: Address,
    val paymentType: PaymentType

)