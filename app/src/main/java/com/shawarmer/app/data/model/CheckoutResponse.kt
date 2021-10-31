package com.shawarmer.app.data.model
import com.squareup.moshi.Json




data class CheckoutResponse(
    @Json(name = "checkoutId")
    val checkoutId: String?
)
