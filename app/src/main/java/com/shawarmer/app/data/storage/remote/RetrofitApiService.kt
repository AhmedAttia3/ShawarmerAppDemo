package com.shawarmer.app.data.storage.remote

import com.shawarmer.app.data.model.DishModel
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApiService {

    @GET("3433cbad-2606-4b85-8c30-008aaa462c55")
    suspend fun getDishes(): List<DishModel>


}