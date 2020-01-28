package ru.abbysoft.rehearsapp

import android.util.Log
import androidx.core.util.Consumer
import androidx.multidex.MultiDexApplication
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import ru.abbysoft.rehearsapp.util.baseUrl
import java.lang.Exception
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.VK

class RehearsalApp : MultiDexApplication() {

    private val TAG = "RehearsalApp"

    private var tokenTracker: VKTokenExpiredHandler = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // expired token
        }
    }

    override fun onCreate() {
        super.onCreate()

        initRestService()

        VK.initialize(this)

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
        Log.i(TAG, baseUrl)
        Log.i(TAG, ServiceFactory.getDatabaseService().toString())
        place?.forEach { Log.i(TAG, it.toString()) }
    }

    private fun initRestService() {
        ServiceFactory.init(baseUrl)
    }

}