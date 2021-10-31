package com.shawarmer.app.data.storage.remote

import com.shawarmer.app.data.model.CheckoutResponse
import com.shawarmer.app.data.model.PaymentStatusResponse
import com.shawarmer.app.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface CheckoutRetrofitApiService {

    @GET("token")
    suspend fun requestCheckoutId(
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("paymentType") paymentType: String = "PA",
        @Query("notificationUrl") notificationUrl: String = Constants.NOTIFICATION_URL,
    ): CheckoutResponse


    @GET("status")
    suspend fun requestPaymentStatus(
        @Query("resourcePath") resourcePath: String
    ): PaymentStatusResponse


}