package com.example.lbprate

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("api/v1/dollarRate")
    fun getRates():Call<BSRate>
}