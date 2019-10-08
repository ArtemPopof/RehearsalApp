package ru.abbysoft.rehearsapp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.core.util.Consumer
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import ru.abbysoft.rehearsapp.util.baseUrl
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class RehearsalApp : Application() {

    private val TAG = "RehearsalApp"

    override fun onCreate() {
        super.onCreate()

        initRestService()
        generateTestData()

        AsyncServiceRequest(
            Consumer<List<Place>> { logAllPlaces(it) },
            Consumer { failedToConnect(it) }
        ).execute(ServiceFactory.getDatabaseService().getAllPlaces())
    }

    private fun failedToConnect(ex: Exception) {
        Log.e(TAG, "Shouldn't connect to db")
        ex.printStackTrace()
    }

    private fun logAllPlaces(place: List<Place>?) {
        place?.forEach { Log.i(TAG, it.toString()) }
    }

    private fun initRestService() {
        ServiceFactory.init(baseUrl)
    }

    private fun generateTestData() {
        // inflate test data
//        val cache = CacheFactory.getDefaultCacheInstance()
//        cache.addPlace(
//            Place(
//                "SoundBase", getCurrentLocation(),
//                getImageFromWeb("https://repa24.ru/images/bases/89/1862.jpg")
//            )
//        )
//        cache.addPlace(Place("CloudBase", getImageFromWeb("https://repa24.ru/images/bases/14/329.jpg")))
//        cache.addPlace(Place("Good Place", getImageFromWeb("https://repa24.ru/images/bases/26/568.jpg")))
//        cache.addPlace(Place("Don't go here", getImageFromWeb("https://www.studiorent.ru/upload_data/4808/upldEbXvAL.jpg")))
    }
}