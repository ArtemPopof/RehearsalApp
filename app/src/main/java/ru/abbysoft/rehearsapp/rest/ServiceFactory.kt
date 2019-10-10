package ru.abbysoft.rehearsapp.rest

import retrofit2.converter.gson.GsonConverterFactory

class ServiceFactory private constructor(baseUrl: String) {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var databaseService: PlaceService
    var imageService: ImageService

    init {
        databaseService = retrofit.create(PlaceService::class.java)
        imageService = retrofit.create(ImageService::class.java)
    }

    companion object {

        private var instance : ServiceFactory? = null

        fun init(baseUrl: String) {
            instance = ServiceFactory(baseUrl)
        }

        fun getDatabaseService() : PlaceService {
            checkInstance()

            return instance?.databaseService as PlaceService
        }

        private fun checkInstance() {
            checkNotNull(instance) { "Must call init() method first to init service" }
        }

        fun getImageService() : ImageService {
            checkInstance()

            return instance?.imageService as ImageService
        }
    }
}