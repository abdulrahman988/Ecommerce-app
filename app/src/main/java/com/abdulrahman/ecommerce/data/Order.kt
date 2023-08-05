package com.abdulrahman.ecommerce.data

import com.abdulrahman.ecommerce.util.PaymentType

data class Order(
    val orderId: String,
    val totalPrice: Float,
    val paymentType: PaymentType
)