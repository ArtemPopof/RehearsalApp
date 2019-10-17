package ru.abbysoft.rehearsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import ru.abbysoft.rehearsapp.login.AuntificationManager
import ru.abbysoft.rehearsapp.login.LoginActivity
import ru.abbysoft.rehearsapp.model.Role
import ru.abbysoft.rehearsapp.model.User
import ru.abbysoft.rehearsapp.util.launchActivity

class RoleChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_choose)
    }

    fun continueAsMusician(view: View) {
        changeRoleTo(Role.USER).andGoToLogin()
    }

    fun continueAsOwner(view: View) {
        changeRoleTo(Role.PLACE_OWNER).andGoToLogin()
    }

    private fun changeRoleTo(changeTo: Role): RoleChooseActivity {
        AuntificationManager.user = User().apply { role = changeTo }
        Log.d("RoleChooser", "Continue as $changeTo")
        return this
    }

    private fun andGoToLogin() {
        launchActivity(this, LoginActivity::class.java)
    }
}
