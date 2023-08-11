package com.abdulrahman.ecommerce.payment

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.abdulrahman.ecommerce.util.Constants
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONException
import org.json.JSONObject

object NetworkPayment {



    // Define properties
    lateinit var customerId: String
    lateinit var ephemeralKey: String
    lateinit var clientSecret: String

    // Create a MutableStateFlow to hold the payment information
    private var _paymentList = MutableStateFlow(ThirdPartyResponse())
    var paymentList = _paymentList.asStateFlow()

    // Entry point to start the payment process
    fun startPaymentFlow(context: Context, totalCost: Float) {
        getCustomerId(context, totalCost)
    }

    private fun getCustomerId(context: Context, totalCost: Float) {
        // Make a network request to get the customer ID
        val request: StringRequest = object :
            StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", { response ->
                try {
                    val jsonObject = JSONObject(response)
                    customerId = jsonObject.getString("id")
                    getEphemeralKey(customerId, context, totalCost)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                Toast.makeText(context, "get customer id error", Toast.LENGTH_SHORT).show()
            }) {
            // Set headers and parameters
            override fun getHeaders(): MutableMap<String, String> {
                val header: HashMap<String, String> = HashMap()
                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
                return header
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun getEphemeralKey(customerId: String, context: Context, totalCost: Float) {
        // Make a network request to get the ephemeral key
        val request: StringRequest = object : StringRequest(
            Request.Method.POST,
            "https://api.stripe.com/v1/ephemeral_keys",
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    ephemeralKey = jsonObject.getString("id")
                    getClientSecret(context, customerId, totalCost)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, "get Ephemeral Key error", Toast.LENGTH_SHORT).show()
            }) {
            // Set headers and parameters
            override fun getHeaders(): MutableMap<String, String> {
                val header: HashMap<String, String> = HashMap<String, String>()
                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
                header["Stripe-Version"] = "2022-11-15"
                return header
            }

            override fun getParams(): MutableMap<String, String> {
                val params: HashMap<String, String> = HashMap<String, String>()
                params["customer"] = customerId
                return params
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun getClientSecret(context: Context, customerId: String, totalCost: Float) {

        Log.d("clientSecret", "getClientSecret: $totalCost")
        // Make a network request to get the client secret
        val request: StringRequest = object : StringRequest(
            Request.Method.POST,
            "https://api.stripe.com/v1/payment_intents",
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    clientSecret = jsonObject.getString("client_secret")
                    _paymentList.value = ThirdPartyResponse(
                        customerId, ephemeralKey,
                        clientSecret
                    )
                    // Trigger the payment process here
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, "get client secret error", Toast.LENGTH_SHORT).show()
            }) {
            // Set headers and parameters
            override fun getHeaders(): MutableMap<String, String> {
                val header: HashMap<String, String> = HashMap()
                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
                return header
            }

            override fun getParams(): MutableMap<String, String> {
                val params: HashMap<String, String> = HashMap()
                params["customer"] = customerId
                params["amount"] = "${totalCost.toInt() * 100}"
//                params["amount"] = "100000"
                params["currency"] = "USD"
                params["automatic_payment_methods[enabled"] = "true"
                return params
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }
}

//
//    lateinit var customerId: String
//    lateinit var ephemeralKey: String
//    lateinit var clientSecret: String
//
//
//
//    private var _paymentList = MutableStateFlow(ThirdPartyResponse())
//    var paymentList = _paymentList.asStateFlow()
//
//    //111
//    fun getCustomerId(context: Context, totalCost: Float) {
//        val request: StringRequest = object :
//            StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", { response ->
//                try {
//                    val jsonObject = JSONObject(response)
//                    customerId = jsonObject.getString("id")
//                    getEphemeralKey(customerId, context, totalCost)
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, { error ->
//                Toast.makeText(context, "get customer id error", Toast.LENGTH_SHORT).show()
//            }) {
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val header: HashMap<String, String> = HashMap()
//                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
//                return header
//            }
//        }
//        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
//        requestQueue.add(request)
//
//    }
//
//
//    //222
//    fun getEphemeralKey(customerId: String, context: Context, totalCost: Float) {
//        val request: StringRequest = object :
//            StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", { response ->
//                try {
//                    val jsonObject = JSONObject(response)
//                    ephemeralKey = jsonObject.getString("id")
//                    getClientSecret(context, customerId, totalCost)
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, { error ->
//                Toast.makeText(context, "get Ephemeral Key error", Toast.LENGTH_SHORT).show()
//            }) {
//
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): MutableMap<String, String> {
//                val header: HashMap<String, String> = HashMap<String, String>()
//                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
//                header["Stripe-Version"] = "2022-11-15"
//                return header
//            }
//
//            @Throws(AuthFailureError::class)
//            override fun getParams(): MutableMap<String, String> {
//                val params: HashMap<String, String> = HashMap<String, String>()
//                params["customer"] = customerId
//                return params
//            }
//        }
//        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
//        requestQueue.add(request)
//
//    }
//
//
//    //33
//    private fun getClientSecret(context: Context, customerId: String, totalCost: Float) {
//        val request: StringRequest = object :
//            StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", { response ->
//                try {
//                    val jsonObject = JSONObject(response)
//                    clientSecret = jsonObject.getString("client_secret")
//                    _paymentList.value = ThirdPartyResponse(
//                        customerId, ephemeralKey,
//                        clientSecret
//                    )
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, { error ->
//                Toast.makeText(context, "get client secret error", Toast.LENGTH_SHORT).show()
//            }) {
//
//
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): MutableMap<String, String> {
//                val header: HashMap<String, String> = HashMap()
//                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
//                return header
//            }
//
//            @Throws(AuthFailureError::class)
//            override fun getParams(): MutableMap<String, String> {
//                val params: HashMap<String, String> = HashMap()
//                params["customer"] = customerId
//                params["amount"] = "${totalCost}"
//                params["currency"] = "USD"
//                params["automatic_payment_methods[enabled"] = "true"
//                return params
//            }
//        }
//        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
//        requestQueue.add(request)
//
//    }
//
//}