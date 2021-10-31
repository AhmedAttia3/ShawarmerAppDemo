package com.shawarmer.app.ui.home.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.repository.CartRepo
import com.shawarmer.app.utils.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject
constructor(
    private val cartRepo: CartRepo
) : ViewModel() {

    // get dishes
    private var getCartItemsJob: Job? = null

    private val _getCartItemsUiState = MutableLiveData<UiStates>()
    val getCartItemsUiState: LiveData<UiStates>
        get() = _getCartItemsUiState

    var cartItemsList: ArrayList<DishModel> = arrayListOf()


    fun getCartItems() {
        getCartItemsJob = CoroutineScope(Dispatchers.Main).launch {
            _getCartItemsUiState.value = UiStates.Loading
            cartItemsList = cartRepo.getCartItemsList() as ArrayList<DishModel>
            if (cartItemsList.isEmpty()) {
                _getCartItemsUiState.value = UiStates.Empty
            } else {
                // this delay for test, show loading
                delay(1000)
                _getCartItemsUiState.value = UiStates.Success(cartItemsList)
            }
        }
    }


    private var _cartItemsLiveData = cartRepo.getCartItemsListLiveData()

    val cartItemsLiveData = Transformations.switchMap(_cartItemsLiveData) { cartList ->
        cartItemsList.forEachIndexed { index, dish ->

            val position = cartList.indexOf(dish)
            if (position > -1) {
                if (dish.quantity != cartList[position].quantity) {
                    dish.quantity = cartList[position].quantity
                    _getCartItemsUiState.value =
                        UiStates.ItemUpdated(dish, position)
                }
            }
        }

        val cartItems: MutableLiveData<List<DishModel>> = MutableLiveData()
        cartItems
    }

    fun minusCartQuantity(dish: DishModel, position: Int) {
        if (dish.quantity > 1) {
            updateItemCartQuantity(dish.copy(quantity = dish.quantity - 1))
        } else {
            deleteItemCart(dish, position)
        }
    }

    fun plusCartQuantity(dish: DishModel) {
        updateItemCartQuantity(dish.copy(quantity = dish.quantity + 1))
    }


    private fun updateItemCartQuantity(dis: DishModel) {
        cartRepo.updateItemCartQuantity(dis)
    }

    private fun deleteItemCart(dish: DishModel, position: Int) {
        cartItemsList.remove(dish)
        _getCartItemsUiState.value = UiStates.ItemDeleted(position)
        cartRepo.deleteItemCart(dish)
        if(cartItemsList.isEmpty()){ _getCartItemsUiState.value = UiStates.Empty
        }
    }

    override fun onCleared() {
        super.onCleared()
        getCartItemsJob?.cancel()
    }

}