package ru.abbysoft.rehearsapp.util

import android.os.AsyncTask
import androidx.core.util.Consumer
import retrofit2.Call
import java.lang.Exception
import java.net.ConnectException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class AsyncServiceRequest<T : Any>(private val consumer: Consumer<T>,
                                   private val failCallback: Consumer<Exception>? = null,
                                   private val timeoutSec: Long = 10L) : AsyncTask<Call<T>, T, Nothing>() {

    val pool = Executors.newScheduledThreadPool(1)

    override fun doInBackground(vararg params: Call<T>): Nothing? {
        if (timeoutSec > 0) {
            startTimer(Runnable {
                this.cancel(true)
                failCallback?.accept(TimeoutException())
            })
        }

        try {
            consumer.accept(params[0].execute().body())
        } catch (ex : ConnectException) {
            failCallback?.accept(ex)
        }

        pool.shutdownNow()

        return null
    }

    private fun startTimer(callback: Runnable) {
        pool.schedule(callback, timeoutSec, TimeUnit.SECONDS)
    }

}
