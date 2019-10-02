package ru.abbysoft.rehearsapp.rest

import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

class ServiceFactory private constructor(baseUrl: String) {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var databaseService: DatabaseService

    init {
        databaseService = retrofit.create(DatabaseService::class.java)
    }

    private fun getDatabaseService() : DatabaseService {
        return databaseService
    }

    companion object {

        private var instance : ServiceFactory? = null

        fun init(baseUrl: String) {
            instance = ServiceFactory(baseUrl)
        }

        fun getDatabaseService() : DatabaseService {
            if (instance == null) {
                throw IllegalStateException("Must call init() method first to init service")
            }

            return instance?.getDatabaseService() as DatabaseService
        }
    }
}