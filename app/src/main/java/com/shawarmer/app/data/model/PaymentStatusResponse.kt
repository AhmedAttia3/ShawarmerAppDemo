package com.shawarmer.app.data.model

import com.squareup.moshi.Json

data class PaymentStatusResponse(
    @field:Json(name = "card")
    val card: PaymentCard,
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "paymentBrand")
    val paymentBrand: String,
    @field:Json(name = "paymentResult")
    val paymentResult: String,
    @field:Json(name = "reason")
    val reason: String,
    @field:Json(name = "registrationId")
    val registrationId: String
)

data class PaymentCard(
    @field:Json(name = "bin")
    val bin: String,
    @field:Json(name = "country")
    val country: String,
    @field:Json(name = "expiryMonth")
    val expiryMonth: String,
    @field:Json(name = "expiryYear")
    val expiryYear: String,
    @field:Json(name = "holder")
    val holder: String,
    @field:Json(name = "issuer")
    val issuer: CardIssuer,
    @field:Json(name = "last4Digits")
    val last4Digits: String,
    @field:Json(name = "maxPanLength")
    val maxPanLength: String,
    @field:Json(name = "regulatedFlag")
    val regulatedFlag: String,
    @field:Json(name = "type")
    val type: String
)

data class CardIssuer(
    @field:Json(name = "bank")
    val bank: String,
    @field:Json(name = "phone")
    val phone: String,
    @field:Json(name = "website")
    val website: String
)