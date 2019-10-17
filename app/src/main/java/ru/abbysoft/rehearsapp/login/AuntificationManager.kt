package ru.abbysoft.rehearsapp.login

import android.content.Context
import androidx.core.util.Consumer
import androidx.databinding.ObservableBoolean
import ru.abbysoft.rehearsapp.model.User
import ru.abbysoft.rehearsapp.util.getVkUserInfo

object AuntificationManager {

    var user: User? = null

    var loggedIn: ObservableBoolean = ObservableBoolean(false)

    fun signUpOrLogIn(context: Context) {
        context.getVkUserInfo(Consumer { onGetVkUser(it) })
    }

    private fun onGetVkUser(vkUser: VkUser) {
        user = User().apply {
            firstName = vkUser.firstName?: ""
            lastName = vkUser.lastName?: ""
            login = firstName
            points = 5
        }

        loggedIn.set(true)
    }
}