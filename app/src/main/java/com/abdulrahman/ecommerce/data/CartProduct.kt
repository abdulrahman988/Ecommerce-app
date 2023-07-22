package com.abdulrahman.ecommerce.data

data class CartProduct(
    val product: Product,
    val quantity: Int
){

    constructor():this(Product(),1)

}