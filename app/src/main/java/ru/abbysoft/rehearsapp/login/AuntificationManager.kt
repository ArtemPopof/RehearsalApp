package ru.abbysoft.rehearsapp.login

import android.content.Context
import androidx.core.util.Consumer
import androidx.databinding.ObservableBoolean
import ru.abbysoft.rehearsapp.model.User
import ru.abbysoft.rehearsapp.util.getVkUserInfo
import ru.abbysoft.rehearsapp.util.saveAsync

object AuntificationManager {

    var user: User? = null

    var loggedIn: ObservableBoolean = ObservableBoolean(false)

    fun signUpOrLogIn(context: Context) {
        context.getVkUserInfo(Consumer { onGetVkUser(it, context) })
    }

    private fun onGetVkUser(vkUser: VkUser, context: Context) {
        user = User().apply {
            firstName = vkUser.firstName?: ""
            lastName = vkUser.lastName?: ""
            login = firstName
            points = 5
        }

        context.saveAsync(user as User, Consumer {
            user!!.id = it as Long
            loggedIn.set(true)
        }, "Failed to login, try again")
    }
}