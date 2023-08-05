package com.abdulrahman.ecommerce.util

sealed class PaymentType(){
    object Cash: PaymentType()
    object Visa: PaymentType()
}
