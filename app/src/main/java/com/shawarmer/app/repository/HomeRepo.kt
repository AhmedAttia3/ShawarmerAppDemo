package com.shawarmer.app.repository

import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.data.storage.remote.RetrofitApiService
import com.shawarmer.app.utils.DataResource
import com.shawarmer.app.utils.safeApiCall
import javax.inject.Inject


class HomeRepo
@Inject
constructor(
    private val apiService: RetrofitApiService
) {

    suspend fun getDishesList(): DataResource<List<DishModel>> {
        return safeApiCall {
            apiService.getDishes()
        }
    }

}