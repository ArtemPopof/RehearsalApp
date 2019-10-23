package ru.abbysoft.rehearsapp.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.EditText
import androidx.core.util.Consumer
import androidx.databinding.Observable
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.login.AuntificationManager
import ru.abbysoft.rehearsapp.login.VkAccountInfoRequest
import ru.abbysoft.rehearsapp.login.VkUser
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.model.User
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import java.lang.IllegalStateException

fun Context.showErrorMessage(message: String) {
    showErrorMessage(message, this)
}

fun Context.showInfoMessage(message: String) {
    showMessage(message, this, android.R.drawable.ic_dialog_info)
}

fun showErrorMessage(message: String, context: Context) {
    showMessage(message, context, android.R.drawable.ic_dialog_alert)
}

private fun showMessage(message: String, context: Context, icon: Int) {
    AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.oops))
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .setIcon(icon)
        .show()
}

fun EditText.validateThatNotBlank() : Boolean {
    if (text.isBlank()) {
        error = ("This field cannot be blank")
        return false
    }

    return true
}

fun pickImage(activity: Activity) {
    val intent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }

    activity.startActivityForResult(intent, PICK_IMAGE)
}

fun handlePickImageResult(data: Intent?, consumer: Consumer<Uri>, context: Context) {
    if (data?.data == null) {
        Log.e("ImagePicker", "Cannot load picked image")
        showErrorMessage("Cannot load picked image", context)
    } else {
        consumer.accept(data.data)
    }

}

fun saveImageToServer(bytes: ByteArray, onResult: Consumer<String>, onFail: Runnable? = null) {
    ImageLoader(onFail,  onResult).execute(bytes)
}

class ImageLoader(private val failCallback: Runnable?, private val onSuccess: Consumer<String>)
    : AsyncTask<ByteArray, Void, String>() {

    override fun doInBackground(vararg args: ByteArray): String {
        val response = ServiceFactory.getImageService().addPlace(args[0]).execute().body()
        if (response?.imageId == null) {
            cancel(true)
        } else {
            return response.imageId as String
        }

        throw IllegalStateException("Look to the code")
    }

    override fun onCancelled() {
        failCallback?.run()
    }

    override fun onPostExecute(result: String?) {
        onSuccess.accept(result as String)
    }
}

fun getRoomFromPlace(index: Int, place: Place?): Room? {
    return place?.getRoom(index)
}

fun Place.getRoom(index: Int): Room? {
    val lastIndex = rooms.lastIndex
    if (lastIndex < index) {
        return null
    }

    return rooms[index]
}

fun <T> add(list: List<T>, element: T): List<T> {
    val newList = ArrayList<T>(list.size + 5)
    newList.addAll(list)
    newList.add(element)
    return newList
}

fun <T: Activity> launchActivity(fromContext: Context, toActivity: Class<T>,
                                 extra: String? = null) {
    val intent = Intent(fromContext, toActivity)

    if (extra != null) {
        intent.putExtra(extra, 0)
    }

    fromContext.startActivity(intent)
}

// TODO extract activity loader to separate class
fun <T: Activity> Context.launchActivity(toActivity: Class<T>): ActivityLoader<T> {
    return ActivityLoader(this, toBeLoaded =  toActivity)
}

fun <T: Activity> Activity.launchForResult(toActivity: Class<T>): ActivityLoader<T> {
    return ActivityLoader(this, this, toActivity)
}

fun <T: Activity> launchActivityForResult(
    fromActivity: Activity, toActivity: Class<T>, request: Int) {

    val intent = Intent(fromActivity, toActivity)
    fromActivity.startActivityForResult(intent, request)
}

class ActivityLoader <T: Activity> (private val context: Context,
                                    private val activity: Activity? = null,
                                    toBeLoaded: Class<T>) {

    private val intent = Intent(context, toBeLoaded)
    private var requestCode: Int? = null

    fun putExtra(extraKey: String, extraValue: Long): ActivityLoader<T> {
        intent.putExtra(extraKey, extraValue)

        return this
    }

    fun putExtra(extraKey: String, extra: String): ActivityLoader<T> {
        intent.putExtra(extraKey, extra)

        return this
    }

    fun withRequestCode(requestCode: Int): ActivityLoader<T> {
        this.requestCode = requestCode

        return this
    }

    fun start() {
        if (activity == null) {
            context.startActivity(intent)
        } else {
            startForResult()
        }
    }

    private fun startForResult() {
        checkNotNull(requestCode) { "Request code is null" }

        activity!!.startActivityForResult(intent, requestCode as Int)
    }
}

fun imageUrl(url: String): String {
    return "$baseUrl/image/$url.jpeg"
}

fun Context.getVkUserInfo(consumer: Consumer<VkUser>) {
    VK.execute(VkAccountInfoRequest(), VKUserCallback(this, consumer))
}

private class VKUserCallback(val context: Context, val consumer: Consumer<VkUser>) : VKApiCallback<VkUser> {
    override fun fail(error: VKApiExecutionException) {
        Log.e("UTILS", "Failed to get user info through VK SDK")
        error.printStackTrace()
        showErrorMessage("Login error, try another method or skip", context)
    }

    override fun success(result: VkUser) {
        consumer.accept(result)
        Log.i("UTILS", "Continue as $result")
    }
}

fun whenUserChanged(consumer: Consumer<User>) {
    AuntificationManager.loggedIn.addOnPropertyChangedCallback(UserChangedListener(consumer))
}

class UserChangedListener (val consumer: Consumer<User>) : Observable.OnPropertyChangedCallback() {
    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        consumer.accept(AuntificationManager.user)
    }

}
