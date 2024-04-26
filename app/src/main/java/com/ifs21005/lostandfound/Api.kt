package com.ifs21005.lostandfound

import com.ifs21005.lostandfound.models.AddLostFoundRequest
import com.ifs21005.lostandfound.models.AddLostFoundResponse
import com.ifs21005.lostandfound.models.DeleteResponse
import com.ifs21005.lostandfound.models.GetAllLostAndFoundsResponse
import com.ifs21005.lostandfound.models.GetCurrentUserResponse
import com.ifs21005.lostandfound.models.LoginRequest
import com.ifs21005.lostandfound.models.LoginResponse
import com.ifs21005.lostandfound.models.LostFoundDetailResponse
import com.ifs21005.lostandfound.models.RegisterRequest
import com.ifs21005.lostandfound.models.RegisterResponse
import com.ifs21005.lostandfound.models.UpdateData
import com.ifs21005.lostandfound.models.UpdateLostFoundResponse
import com.ifs21005.lostandfound.models.UploadCoverResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @POST("auth/login")
    fun loginApi(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun registerApi(@Body request: RegisterRequest) : Call<RegisterResponse>

    @GET("users/me")
    fun getCurrentUserApi(
        @Header("Authorization") token: String
    ): Call<GetCurrentUserResponse>

    @DELETE("lost-founds/{id}")
    fun deleteLostFoundApi(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DeleteResponse>

    @GET("lost-founds")
    fun getLostFoundApi(
    @Header("Authorization") token: String,
        @Query("is_completed") isCompleted: Int?,
        @Query("is_me") isMe: Int?,
            @Query("status") status: String?
        ): Call<GetAllLostAndFoundsResponse>


        @Multipart
        @POST("lost-founds/{id}/cover")
    fun uploadCoverApi(
        @Header("Authorization") authorization: String,
        @Part cover: MultipartBody.Part,
        @Path("id") id: String
    ): Call<UploadCoverResponse>

        @GET("lost-founds/{id}")
        fun getDetailLostFoundApi(
            @Header("Authorization") authorization: String,
            @Path("id") id : Int
        ) : Call<LostFoundDetailResponse>

        @PUT("lost-founds/{id}")
    fun updateLostFoundApi (
        @Header("Authorization") authorization: String,
        @Path("id") id : String,
        @Body updateData : UpdateData
        ) : Call<UpdateLostFoundResponse>

    @POST("lost-founds")
    fun createLostFoundApi(
        @Header("Authorization") authorization: String,
        @Body lostFoundData:AddLostFoundRequest
    ): Call<AddLostFoundResponse>

    @Multipart
    @POST("users/photo")
    fun simpanFotoProfil (
        @Header("Authorization") authorization: String,
        @Part photo: MultipartBody.Part,
    ) : Call<Void>

}