package ru.abbysoft.rehearsapp.util

import android.app.AlertDialog
import android.content.Context
import ru.abbysoft.rehearsapp.R


fun showErrorMessage(message: String, context: Context) {
    AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.oops))
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
}