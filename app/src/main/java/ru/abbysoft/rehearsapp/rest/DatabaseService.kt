package ru.abbysoft.rehearsapp.rest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.abbysoft.rehearsapp.model.Place

interface DatabaseService {

    @GET("/place/{id}/")
    fun getPlace(@Path("id") id: Long) : Call<Place>

    @GET("/place/getAll/")
    fun getAllPlaces() : Call<List<Place>>

    @POST("/place/add/")
    fun addPlace(@Body place: Place) : Call<Long>
}