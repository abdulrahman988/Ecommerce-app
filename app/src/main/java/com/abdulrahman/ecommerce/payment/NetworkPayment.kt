package com.abdulrahman.ecommerce.payment

import android.content.Context
import android.widget.Toast
import com.abdulrahman.ecommerce.util.Constants
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class NetworkPayment {
    private lateinit var customerId: String
    private lateinit var ephemeralKey: String
    private lateinit var clientSecret: String

    //111
    fun getCustomerId(context: Context) {
        val request: StringRequest = object :
            StringRequest(Request.Method.POST, "https://api.com/v1/customers", { response ->
                try {
                    val jsonObject = JSONObject(response)
                    customerId = jsonObject.getString("id")
                    Toast.makeText(context, customerId, Toast.LENGTH_SHORT).show()
                    getEphemeralKey(context)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val header: HashMap<String, String> = HashMap<String, String>()
                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
                return header
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)

    }


    //222
    fun getEphemeralKey(context: Context) {
        val request: StringRequest = object :
            StringRequest(Request.Method.POST, "https://api.com/v1/ephemeral_keys", { response ->
                try {
                    val jsonObject = JSONObject(response)
                    customerId = jsonObject.getString("id")
//                    Toast.makeText(context, customerId, Toast.LENGTH_SHORT).show()
                    getClientSecret(context,customerId, ephemeralKey)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val header: HashMap<String, String> = HashMap<String, String>()
                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
                header["Stripe-Version"] = "2022-11-15"
                return header
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String>? {
                val params: HashMap<String, String> = HashMap<String, String>()
                params["customer"] = customerId
                return params
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)

    }


    //33
    private fun getClientSecret(context: Context,customerId: String, ephemeralKey: String) {
        val request: StringRequest = object :
            StringRequest(Request.Method.POST, "https://api.com/v1/payment_intents", { response ->
                try {
                    val jsonObject = JSONObject(response)
                    clientSecret = jsonObject.getString("client_secret")
                    Toast.makeText(context, clientSecret, Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }) {


            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val header: HashMap<String, String> = HashMap<String, String>()
                header["Authorization"] = "Bearer ${Constants.SECRET_KEY}"
                return header
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String>? {
                val params: HashMap<String, String> = HashMap<String, String>()
                params["customer"] = customerId
                params["amount"] = "100"+"00 "
                params["currency"] = "USD"
                params["automatic_payment_methods[enabled"] = "true"
                return params
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)

    }

}