package ru.abbysoft.rehearsapp.rest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface BookService {
    @POST("/book/{id}")
    fun bookSlot(@Path("id") id: Long, @Body userId: Long): Call<Boolean>
}