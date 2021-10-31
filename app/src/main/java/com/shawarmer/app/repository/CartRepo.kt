package com.shawarmer.app.repository

import androidx.lifecycle.LiveData
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.data.storage.local.db.AppDao
import javax.inject.Inject

class CartRepo
@Inject
constructor(
    private val db: AppDao
) {
    fun updateItemCartQuantity(dish:DishModel){
        db.insertDish(dish)
    }

    fun deleteItemCart(dis:DishModel){
        db.deleteItemCart(dis.title)
    }

    fun getCartItemsList(): List<DishModel>{
        return db.getCartItemsList()
    }

    fun getCartItemsListLiveData(): LiveData<List<DishModel>>{
        return db.getCartItemsListLiveData()
    }
    fun chickIfDishInCart(dishModel: DishModel):LiveData<Boolean>{
        return db.chickIfDishInCart(dishModel.title)
    }

    fun clearCartItems() {
        db.clearCartItems()
    }
}