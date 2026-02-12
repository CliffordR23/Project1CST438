package com.example.project_1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class NumVerifyResponse(
    val valid: Boolean,
    val number: String,
    val local_format: String,
    val international_format: String,
    val country_prefix: String,
    val country_code: String,
    val country_name: String,
    val location: String,
    val carrier: String,
    val line_type: String
)

interface NumVerifyService {
    @GET("api/validate")
    suspend fun verifyPhoneNumber(
        @Query("access_key") accessKey: String,
        @Query("number") phoneNumber: String
    ): NumVerifyResponse
}

object NumVerifyClient {
    private const val BASE_URL = "http://apilayer.net/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: NumVerifyService = retrofit.create(NumVerifyService::class.java)
}