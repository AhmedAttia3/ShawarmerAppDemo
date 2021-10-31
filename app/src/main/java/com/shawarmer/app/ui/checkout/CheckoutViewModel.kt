package com.shawarmer.app.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shawarmer.app.MyApp
import com.shawarmer.app.R
import com.shawarmer.app.repository.CartRepo
import com.shawarmer.app.repository.CheckoutRepo
import com.shawarmer.app.utils.Constants
import com.shawarmer.app.utils.DataResource
import com.shawarmer.app.utils.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject
constructor(
    private val myApp: MyApp,
    private val repo: CheckoutRepo,
    private val cartRepo: CartRepo
) : ViewModel() {

    // get checkout id
    private var getCheckoutIdJob: Job? = null
    private val _getCheckoutIdUiState = MutableLiveData<UiStates>()
    val getCheckoutIdUiState: LiveData<UiStates>
        get() = _getCheckoutIdUiState

    var checkoutId:String? =null

    fun requestCheckoutId(amount: String){
        _getCheckoutIdUiState.value = UiStates.Loading
        getCheckoutIdJob = CoroutineScope(Dispatchers.Main).launch {
            // this delay for test, show loading
            delay(1000)
            when (val response = repo.requestCheckoutId(amount,Constants.PaymentConfig.CURRENCY)) {
                is DataResource.Success -> {
                    checkoutId = response.value
                    if (checkoutId!=null) {
                        _getCheckoutIdUiState.value = UiStates.Success(checkoutId)
                    } else
                        _getCheckoutIdUiState.value =
                            UiStates.Error(myApp.getString(R.string.general_error))
                }
                is DataResource.Error -> {
                    _getCheckoutIdUiState.value =
                        UiStates.Error(myApp.getString(R.string.general_error))

                }
            }
        }
    }

    // get payment status id
    private var getPaymentStatusJob: Job? = null
    private val _getPaymentStatusUiState = MutableLiveData<UiStates>()
    val getPaymentStatusUiState: LiveData<UiStates>
        get() = _getPaymentStatusUiState

    var paymentStatus:Boolean =false

    fun requestPaymentStatus(resourcePath: String){
        _getPaymentStatusUiState.value = UiStates.Loading
        getPaymentStatusJob = CoroutineScope(Dispatchers.Main).launch {
            // this delay for test, show loading
            delay(1000)
            when (val response = repo.requestPaymentStatus(resourcePath)) {
                is DataResource.Success -> {
                    paymentStatus = response.value
                    _getPaymentStatusUiState.value = UiStates.Success(paymentStatus)
                    if (paymentStatus)
                        cartRepo.clearCartItems()
                }
                is DataResource.Error -> {
                    _getPaymentStatusUiState.value =
                        UiStates.Error(myApp.getString(R.string.general_error))

                }
            }
        }
    }
}