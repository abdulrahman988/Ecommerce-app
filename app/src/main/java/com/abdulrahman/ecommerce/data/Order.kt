package com.abdulrahman.ecommerce.data


data class Order(
    val orderId: String,
    val totalPrice: Float,
    val contact_email: String,
    val createdAt: String,
    val productList: List<CartProduct>,
    val address: Address,
    val paymentType: String
){
    constructor():this("",0f,"","", emptyList(),Address(),"")
}