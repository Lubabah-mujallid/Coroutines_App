package com.example.coroutines_app

import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("advice")
    fun getAdvice(): Call<Advice>
}