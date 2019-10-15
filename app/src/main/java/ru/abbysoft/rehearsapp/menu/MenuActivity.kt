package ru.abbysoft.rehearsapp.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.util.Consumer
import kotlinx.android.synthetic.main.activity_menu.*
import ru.abbysoft.rehearsapp.MapsActivity
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.login.VkUser
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.place.PlaceCreationActivity
import ru.abbysoft.rehearsapp.place.PlaceViewActivity
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import ru.abbysoft.rehearsapp.util.getVkUserInfo
import ru.abbysoft.rehearsapp.util.showErrorMessage
import java.util.*

const val LOGIN_HAPPENED_EXTRA = "LOGIN_HAPPENED"

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        if (isLoginSkipped()) {
            hideLoginField()
        } else {
            tryToGetUserInfo()
        }
    }

    private fun isLoginSkipped(): Boolean {
        if (intent.extras == null) {
            return true
        }
        return !(intent.extras as Bundle).containsKey(LOGIN_HAPPENED_EXTRA)
    }

    private fun hideLoginField() {
        welcome_message.text = getString(R.string.welcome_to_app)
    }

    private fun tryToGetUserInfo() {
        getVkUserInfo(Consumer { updateMessage(it) })
    }

    private fun updateMessage(userInfo: VkUser) {
        welcome_message.text = getString(R.string.welcome_message, "${userInfo.firstName} ${userInfo.lastName}")
    }

    fun onMapClicked(view : View) {
        MapsActivity.launchFrom(this)
    }

    fun onPlaceViewClicked(view : View) {
        AsyncServiceRequest<List<Place>>(
            Consumer {
                PlaceViewActivity.launchForView(this, getRandomId(it))
            },
            Consumer {
                showErrorMessage("Cannot open random place", this)
            }
        ).execute(ServiceFactory.getDatabaseService().getAllPlaces())
    }

    private fun getRandomId(places: List<Place>) : Long {
        val rand = Random(System.currentTimeMillis())

        val randIndex = rand.nextInt(places.size)
        return places[randIndex].id
    }

    fun onAddNewPlaceClicked(view : View) {
        PlaceCreationActivity.launch(view)
    }


}
