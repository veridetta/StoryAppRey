package com.example.storryapp.data.network.retrofit

import com.example.storryapp.data.network.response.AddNewStoriesResponse
import com.example.storryapp.data.network.response.GetAllStoriesResponse
import com.example.storryapp.data.network.response.LoginResponse
import com.example.storryapp.data.network.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @Multipart
    @POST("stories")
    fun addNewStories(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody
    ): Call<AddNewStoriesResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetAllStoriesResponse

    @GET("stories?location=1")
    suspend  fun getAllStoriesMaps(
        @Header("Authorization") token: String
    ): GetAllStoriesResponse
}