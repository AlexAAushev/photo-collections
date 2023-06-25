package com.practicum.practice_work_1_1.data.api


import com.practicum.practice_work_1_1.accessTokenApi
import com.practicum.practice_work_1_1.data.db.*
import com.practicum.practice_work_1_1.presentations.searchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

var BASE_URL = "https://api.unsplash.com"

var retrofit = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(Api::class.java)

interface Api {
    @GET("/users/{id}/photos")
    suspend fun loadUsersPhoto(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String,
        @Query("page") page: Int,
    ): List<LoadPhotoResponse>

    @GET("/collections/{id}/photos")
    suspend fun loadCollectionsDetails(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String,
        @Query("page") page: Int,
    ): List<LoadPhotoResponse>

    @GET("/collections")
    suspend fun loadCollections(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Query("page") page: Int,
    ): List<LoadCollections>

    @GET("/photos")
    suspend fun loadPhotos(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Query("page") page: Int,
    ): List<LoadPhotoResponse>

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Query("page") page: Int,
        @Query("query") query: String = searchRequest
    ): SearchResult

    @GET("/photos/{id}")
    suspend fun loadPhotoDetails(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String
    ): PhotoDetails

    @POST("/photos/{id}/like")
    suspend fun loadPhotoDetailsLike(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String
    ): PhotoLike

    @DELETE("/photos/{id}/like")
    suspend fun loadPhotoDetailsUnlike(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi",
        @Path("id") id: String
    ): PhotoLike

    @GET("/me")
    suspend fun loadUserInfo(
        @Header("Authorization") Request: String? = "Bearer $accessTokenApi"
    ): UserInfoResponse
}