package com.shawarmer.app.data.storage.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shawarmer.app.data.model.DishModel


@Dao
interface AppDao {

    // ============== cart ==============
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDish(dish: DishModel):Long

    @Query("SELECT * FROM cart ORDER BY onAddedTimestamp ASC")
    fun getCartItemsList(): List<DishModel>

    @Query("SELECT * FROM cart ORDER BY onAddedTimestamp ASC")
    fun getCartItemsListLiveData(): LiveData<List<DishModel>>

    @Query("SELECT EXISTS(SELECT * FROM cart WHERE title = :title)")
    fun chickIfDishInCart(title:String) : LiveData<Boolean>

    @Query("DELETE FROM cart WHERE title = :title")
    fun deleteItemCart(title:String):Int

    @Query("DELETE FROM cart")
    fun clearCartItems()


}