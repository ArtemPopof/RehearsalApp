package ru.abbysoft.rehearsapp.util

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.core.util.Consumer
import retrofit2.Call
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class AsyncServiceRequest<T : Any>(private val consumer: Consumer<T>,
                                   private val failCallback: Consumer<Exception>? = null,
                                   private val timeoutSec: Long = 10L) : AsyncTask<Call<T>, Any, Any>() {

    private var exception: Exception? = null

    private val TAG = AsyncServiceRequest::class.java.name

    val pool = Executors.newScheduledThreadPool(1)

    override fun doInBackground(vararg params: Call<T>): Nothing? {
        if (timeoutSec > 0) {
            startTimer(Runnable {
                this.cancel(true)
                exception = TimeoutException()
            })
        }

        try {
            val result = params[0].execute()
            if (!result.isSuccessful) {
                val errorBody = result.errorBody()
                errorBody?.let {
                    handleException(RestServiceException(it.string()))
                }
            } else {
                consumer.accept(result.body())
            }
        } catch (ex: Exception) {
            handleException(ex)
        } finally {
            pool.shutdownNow()
        }

        return null
    }

    fun execute(call: Call<T>) {
        super.execute(call)
    }

    private fun handleException(ex: Exception) {
        ex.printStackTrace()
        Log.e(TAG, ex.toString())
        this.exception = ex
        this.cancel(true)
    }

    override fun onCancelled() {
        super.onCancelled()

        if (exception != null) {
            failCallback?.accept(exception)
        } else {
            Log.e(TAG, "error is null")
        }
    }

    private fun startTimer(callback: Runnable) {
        pool.schedule(callback, timeoutSec, TimeUnit.SECONDS)
    }

    class RestServiceException(message: String) : Exception(message)
}

fun updatePlaceAsync(place: Place, errorMessage: String, context: Context) {
    AsyncServiceRequest(
        Consumer<Boolean> { if (!it) showErrorMessage(errorMessage, context); },
        Consumer { showErrorMessage(errorMessage, context) }
    ).execute(ServiceFactory.getDatabaseService().updatePlace(place))
}

fun <T: Any> saveAsync(entity: Any, callback: Consumer<T>, errorMessage: String, context: Context) {
    AsyncServiceRequest(
        Consumer<T> { callback.accept(it) },
        Consumer { showErrorMessage(errorMessage, context) }
    ).execute(getCall(entity) as Call<T>)
}

fun getCall(entity: Any): Call<*> {
    when (entity) {
        is Room -> return ServiceFactory.getDatabaseService().saveRoom(entity)
    }

    throw IllegalStateException("not realised another options")
}