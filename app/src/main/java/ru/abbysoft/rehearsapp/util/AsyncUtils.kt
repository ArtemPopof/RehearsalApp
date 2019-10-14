package ru.abbysoft.rehearsapp.util

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.core.util.Consumer
import retrofit2.Call
import ru.abbysoft.rehearsapp.R
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
                                   private val timeoutSec: Long = 10L) : AsyncTask<Call<T>, Any, T>() {

    private var exception: Exception? = null

    private val TAG = AsyncServiceRequest::class.java.name

    val pool = Executors.newScheduledThreadPool(1)

    override fun doInBackground(vararg params: Call<T>): T {
        if (timeoutSec > 0) {
            startTimer(Runnable {
                this.cancel(true)
                exception = TimeoutException()
            })
        }

        try {
            val result = params[0].execute()
            if (!result.isSuccessful || result.body() == null) {
                val errorBody = result.errorBody()
                errorBody?.let {
                    handleException(RestServiceException(it.string()))
                }
            } else {
                return result.body() as T
            }
        } catch (ex: Exception) {
            handleException(ex)
        } finally {
            pool.shutdownNow()
        }

        throw IllegalStateException("some error")
    }

    override fun onPostExecute(result: T) {
        consumer.accept(result)
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

fun Context.updateRoomAsync(room: Room, errorMessage: String,
                            finishCallback: Runnable = Runnable {  }) {
    AsyncServiceRequest(
        Consumer <Boolean> { if (!it) showErrorMessage(errorMessage, this); finishCallback.run() },
        Consumer { showErrorMessage(errorMessage, this) }
    ).execute(ServiceFactory.getDatabaseService().updateRoom(room.id, room))
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

fun Context.loadImageDataAsync(imageId: String, consumer: Consumer<ByteArray>) {
    AsyncServiceRequest(
        Consumer<ByteArray> { consumer.accept(it) },
        Consumer { showErrorMessage(getString(R.string.cannot_load_image), this) }
    ).execute(ServiceFactory.getImageService().getImage(imageId))
}