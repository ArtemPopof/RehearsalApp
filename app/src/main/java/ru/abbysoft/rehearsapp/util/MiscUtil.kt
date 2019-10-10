package ru.abbysoft.rehearsapp.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.widget.EditText
import androidx.core.util.Consumer
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.rest.ServiceFactory

const val PICK_IMAGE = 0

fun showErrorMessage(message: String, context: Context) {
    AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.oops))
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .setIcon(android.R.drawable.ic_dialog_alert)
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

fun saveImageToServer(bytes: ByteArray, onResult: Consumer<String>, onFail: Runnable? = null) {
    ImageLoader(onFail,  onResult).execute(bytes)
}

class ImageLoader(private val failCallback: Runnable?, private val onSuccess: Consumer<String>)
    : AsyncTask<ByteArray, Void, Unit>() {

    override fun doInBackground(vararg args: ByteArray) {
        val response = ServiceFactory.getImageService().addPlace(args[0]).execute().body()
        if (response?.imageId == null) {
            cancel(true)
        } else {
            onSuccess.accept(response.imageId)
        }
    }

    override fun onCancelled() {
        failCallback?.run()
    }

}