package ru.abbysoft.rehearsapp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import ru.abbysoft.rehearsapp.cache.CacheFactory
import ru.abbysoft.rehearsapp.cache.model.Place
import ru.abbysoft.rehearsapp.util.getCurrentLocation
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class RehearsalApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // inflate test data
        val cache = CacheFactory.getDefaultCacheInstance()
        cache.addPlace(
            Place(
                "SoundBase", getCurrentLocation(),
                getImageFromWeb("https://repa24.ru/images/bases/89/1862.jpg")
            )
        )
//        cache.addPlace(Place("CloudBase", getImageFromWeb("https://repa24.ru/images/bases/14/329.jpg")))
//        cache.addPlace(Place("Good Place", getImageFromWeb("https://repa24.ru/images/bases/26/568.jpg")))
//        cache.addPlace(Place("Don't go here", getImageFromWeb("https://www.studiorent.ru/upload_data/4808/upldEbXvAL.jpg")))
    }

    private fun getImageFromWeb(path: String): Bitmap? {
        val bitmap = ImageDownloader().execute(path)
            ?: null

        return bitmap?.get()
    }

    private class ImageDownloader : AsyncTask<String, Int, Bitmap>() {

        override fun doInBackground(vararg path: String?): Bitmap? {
            if (path.isEmpty()) {
                return null
            }

            return try {
                val url = URL(path[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                // Log exception
                e.printStackTrace()
                return null
            }
        }

    }
}