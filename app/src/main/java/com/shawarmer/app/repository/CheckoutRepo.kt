package com.shawarmer.app.repository

import android.util.Log
import com.shawarmer.app.data.storage.remote.CheckoutRetrofitApiService
import com.shawarmer.app.utils.DataResource
import com.shawarmer.app.utils.safeApiCall
import java.net.URLEncoder
import javax.inject.Inject

class CheckoutRepo
@Inject
constructor(
    private val apiService: CheckoutRetrofitApiService
) {

    suspend fun requestCheckoutId(amount: String, currency: String): DataResource<String?> {
        return safeApiCall {
            val response = apiService.requestCheckoutId(
                amount,
                currency
            )
            response.checkoutId
        }
    }

    suspend fun requestPaymentStatus(resourcePath: String): DataResource<Boolean> {
        return safeApiCall {
            val response = apiService.requestPaymentStatus(
                URLEncoder.encode(resourcePath, "UTF-8")
            )
            response.paymentResult ==  "OK"
        }
    }
}
