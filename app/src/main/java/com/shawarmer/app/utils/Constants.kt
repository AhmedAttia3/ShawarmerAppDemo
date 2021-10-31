package com.shawarmer.app.utils


object Constants {


    const val CHECKOUT_BASE_URL = "http://52.59.56.185"
    const val MAIN_BASE_URL = "https://run.mocky.io/v3/"
    const val NOTIFICATION_URL = "$CHECKOUT_BASE_URL:80/notification"


    enum class ErrorType {
        NETWORK,
        TIMEOUT,
        UNKNOWN
    }

    object PaymentConfig {

        // In test mode, SAR currency is not accepted
        const val CURRENCY = "EUR"
        val PAYMENT_BRANDS = linkedSetOf("VISA", "MASTER", "GOOGLEPAY")

        const val MERCHANT_ID = "ff80808138516ef4013852936ec200f2"
    }
}