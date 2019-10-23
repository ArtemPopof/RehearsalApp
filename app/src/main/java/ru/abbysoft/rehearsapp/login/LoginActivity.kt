package ru.abbysoft.rehearsapp.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.menu.LOGIN_HAPPENED_EXTRA
import ru.abbysoft.rehearsapp.menu.MenuActivity
import ru.abbysoft.rehearsapp.model.User
import ru.abbysoft.rehearsapp.room.PARENT_ACTIVITY_EXTRA
import ru.abbysoft.rehearsapp.room.ROOM_VIEW_ACTIVITY
import ru.abbysoft.rehearsapp.room.RoomViewActivity
import ru.abbysoft.rehearsapp.util.launchActivity
import ru.abbysoft.rehearsapp.util.showErrorMessage

class LoginActivity : AppCompatActivity() {

    var launchedFrom: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        rememberWhoLaunchedThis()
    }

    private fun rememberWhoLaunchedThis() {
        val extra = intent.extras?.getString(PARENT_ACTIVITY_EXTRA)

        if (extra == ROOM_VIEW_ACTIVITY) {
            launchedFrom = RoomViewActivity::class.java
        }
    }

    fun loginVk(view: View) {
        VK.login(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VK.onActivityResult(requestCode, resultCode, data, AuthCallback())) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    inner class AuthCallback : VKAuthCallback {

        private val onLoginResult = Consumer<User> {
            if (launchedFrom == null) {
                launchActivity(this@LoginActivity, MenuActivity::class.java)
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        override fun onLogin(token: VKAccessToken) {
            AuntificationManager.signUpOrLogIn(this@LoginActivity, onLoginResult)
        }

        override fun onLoginFailed(errorCode: Int) {
            showErrorMessage("Cannot sign in, try another method, or skip", this@LoginActivity)
        }
    }


    fun skipLogin(view: View) {
        launchActivity(this, MenuActivity::class.java)
    }
}