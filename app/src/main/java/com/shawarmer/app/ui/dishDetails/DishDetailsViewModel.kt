package com.shawarmer.app.ui.dishDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.repository.CartRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DishDetailsViewModel @Inject
constructor(
    private val cartRepo: CartRepo
) : ViewModel() {


    val dishModel:MutableLiveData<DishModel> = MutableLiveData()
    val ifDishInCart = Transformations.switchMap(dishModel) { dishModel ->
        cartRepo.chickIfDishInCart(dishModel)
    }

    fun updateItemCartQuantity(dis:DishModel){
        cartRepo.updateItemCartQuantity(dis)
    }

}