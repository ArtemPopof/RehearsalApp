package ru.abbysoft.rehearsapp.rest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.abbysoft.rehearsapp.model.ImageControllerResponse

interface ImageService {

    @GET("/image/{id}/")
    fun getImage(@Path("id") id: String) : Call<ByteArray>

    @POST("/image/save/")
    fun addPlace(@Body bytes: ByteArray) : Call<ImageControllerResponse>
}