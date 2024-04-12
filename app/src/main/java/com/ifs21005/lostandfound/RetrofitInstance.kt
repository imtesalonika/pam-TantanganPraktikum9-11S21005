package com.ifs21005.lostandfound

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://public-api.delcom.org/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit ()  : Retrofit {
        return retrofit
    }
}