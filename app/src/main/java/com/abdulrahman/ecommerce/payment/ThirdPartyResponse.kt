package com.abdulrahman.ecommerce.payment

data class ThirdPartyResponse(
    var customerId: String,
    var ephemeralKey: String,
    var clientSecret: String
) {
    constructor():this("","","")
}