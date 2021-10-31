package com.shawarmer.app.utils

sealed class UiStates {

    object Loading : UiStates()
    data class Success<out T : Any>(val data: T?) : UiStates()
    data class ItemUpdated<out T : Any>(val data: T?, val position:Int) : UiStates()
    data class ItemDeleted(val position:Int) : UiStates()
    data class Error(val message: String) : UiStates()
    object NoConnection : UiStates()
    object Empty : UiStates()

}