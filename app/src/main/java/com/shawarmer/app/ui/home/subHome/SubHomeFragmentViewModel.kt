package com.shawarmer.app.ui.home.subHome

import androidx.lifecycle.*
import com.blankj.utilcode.util.NetworkUtils
import com.shawarmer.app.utils.UiStates
import com.shawarmer.app.MyApp
import com.shawarmer.app.R
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.repository.CartRepo
import com.shawarmer.app.repository.HomeRepo
import com.shawarmer.app.utils.DataResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubHomeFragmentViewModel @Inject
constructor(
    private val myApp: MyApp,
    private val homeRepo: HomeRepo,
    private val cartRepo: CartRepo
) : ViewModel() {

    // get dishes
    private var getDishesJob: Job? = null
    private val _getDishesUiState = MutableLiveData<UiStates>()
    val getDishesUiState: LiveData<UiStates>
        get() = _getDishesUiState
    var dishesList: List<DishModel> = arrayListOf()


    fun getDishes() {
        if (NetworkUtils.isConnected()) {
            if (getDishesJob?.isActive == true)
                return
            getDishesJob = launchGetDishesJob()
        } else {
            _getDishesUiState.value = UiStates.NoConnection
        }
    }

    private fun launchGetDishesJob(): Job {
        _getDishesUiState.value = UiStates.Loading
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = homeRepo.getDishesList()) {
                is DataResource.Success -> {
                    val list = response.value
                    if (list.isNotEmpty()) {
                        val synchronizeList: List<DishModel> = synchronizeListWithCart(list)
                        _getDishesUiState.value = UiStates.Success(synchronizeList)
                    } else
                        _getDishesUiState.value = UiStates.Empty
                }
                is DataResource.Error -> {
                    _getDishesUiState.value =
                        UiStates.Error(myApp.getString(R.string.general_error))

                }
            }
        }
    }

    private fun synchronizeListWithCart(list: List<DishModel>): List<DishModel> {
        val cartList = cartRepo.getCartItemsList()
        cartList.map {
            val n = list.indexOf(it)
            if (n > -1)
                list[n].quantity = it.quantity
        }
        dishesList = list
        return list
    }

    fun addToCart(dishModel: DishModel) {
        dishModel.quantity+=1
        cartRepo.updateItemCartQuantity(dishModel)
    }

    private val cartListLiveData = cartRepo.getCartItemsListLiveData()

    /*
    this liveData created to transform observer
    between current cart list @cartListLiveData and server dishes list @dishesList
    */
    val cartItems = Transformations.switchMap(cartListLiveData) { cartList ->
        cartList?.map {
            val position = dishesList.indexOf(it)
            if (position > -1) {
                dishesList[position].quantity = it.quantity
                _getDishesUiState.value = UiStates.ItemUpdated(dishesList[position],position)
            }
        }
        val cartItems: MutableLiveData<List<DishModel>> = MutableLiveData()
        cartItems
    }


    var currentMotionState:Int = 0
}