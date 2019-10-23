package ru.abbysoft.rehearsapp.rest

import retrofit2.Call
import retrofit2.http.*
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.model.User

interface PlaceService {

    @GET("/place/{id}/")
    fun getPlace(@Path("id") id: Long) : Call<Place>

    @GET("/room/{id}/")
    fun getRoom(@Path("id") id: Long): Call<Room>

    @GET("/place/getAll/")
    fun getAllPlaces() : Call<List<Place>>

    @POST("/place/add/")
    fun addPlace(@Body place: Place) : Call<Long>

    @POST("/room/")
    fun saveRoom(@Body room: Room): Call<Long>

    @POST("/user")
    fun saveUser(@Body user: User): Call<Long>

    @PUT("/place/update/")
    fun updatePlace(@Body place: Place) : Call<Boolean>

    @PUT("/room/{id}/")
    fun updateRoom(@Path("id") id: Long, @Body room: Room): Call<Boolean>

    @PATCH("/place/{id}/")
    fun patchPlace(@Path("id") id: Long, @Body fields: Map<String, @JvmSuppressWildcards Any>): Call<Boolean>
}

