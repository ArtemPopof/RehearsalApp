package ru.abbysoft.rehearsapp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.menu.LOGIN_HAPPENED_EXTRA
import ru.abbysoft.rehearsapp.menu.MenuActivity
import ru.abbysoft.rehearsapp.util.launchActivity
import ru.abbysoft.rehearsapp.util.showErrorMessage

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
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
        override fun onLogin(token: VKAccessToken) {
            AuntificationManager.signUpOrLogIn(this@LoginActivity)
            launchActivity(this@LoginActivity, MenuActivity::class.java)
        }

        override fun onLoginFailed(errorCode: Int) {
            showErrorMessage("Cannot sign in, try another method, or skip", this@LoginActivity)
        }
    }


    fun skipLogin(view: View) {
        launchActivity(this, MenuActivity::class.java)
    }
}