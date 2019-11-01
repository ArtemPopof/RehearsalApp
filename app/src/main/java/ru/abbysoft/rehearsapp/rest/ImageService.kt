package ru.abbysoft.rehearsapp.rest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import ru.abbysoft.rehearsapp.model.ImageControllerResponse

interface ImageService {

    @GET("/image/{id}/")
    fun getImage(@Path("id") id: String) : Call<ByteArray>

    @POST("/image/save/")
    fun uploadImageOld(@Body bytes: ByteArray) : Call<ImageControllerResponse>

    @Multipart
    @POST("/image/save/")
    fun uploadImage(@Part part: MultipartBody.Part): Call<ImageControllerResponse>
}