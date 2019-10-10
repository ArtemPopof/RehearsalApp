package ru.abbysoft.rehearsapp.rest

import retrofit2.Call
import retrofit2.http.*
import ru.abbysoft.rehearsapp.model.Place

interface PlaceService {

    @GET("/place/{id}/")
    fun getPlace(@Path("id") id: Long) : Call<Place>

    @GET("/place/getAll/")
    fun getAllPlaces() : Call<List<Place>>

    @POST("/place/add/")
    fun addPlace(@Body place: Place) : Call<Long>

    @PUT("/place/update/")
    fun updatePlace(@Body place: Place) : Call<Boolean>

    @PATCH("/place/{id}/")
    fun patchPlace(@Path("id") id: Long, @Body fields: Map<String, @JvmSuppressWildcards Any>): Call<Boolean>
}

