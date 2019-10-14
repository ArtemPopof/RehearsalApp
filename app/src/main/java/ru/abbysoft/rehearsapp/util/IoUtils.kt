package ru.abbysoft.rehearsapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

//fun getImageFromWeb(path: String): Bitmap? {
//    val bitmap = ImageDownloader().execute(path)
//        ?: null
//
//    return bitmap?.get()
//}

private class ImageDownloader : AsyncTask<String, Int, ByteArray>() {

    override fun doInBackground(vararg path: String?): ByteArray? {
        if (path.isEmpty()) {
            return null
        }

        return try {
            val url = URL(path[0])
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            input.readBytes()
        } catch (e: IOException) {
            // Log exception
            e.printStackTrace()
            null
        }
    }

}