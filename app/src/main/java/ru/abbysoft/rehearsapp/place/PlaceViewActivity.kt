package ru.abbysoft.rehearsapp.place

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_place_view.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.cache.CacheFactory
import ru.abbysoft.rehearsapp.databinding.ActivityPlaceViewBinding
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import java.lang.IllegalArgumentException

const val PLACE_ID_EXTRA = "PlaceidExtra"

class PlaceViewActivity : AppCompatActivity() {

    val TAG = PlaceViewActivity::class.java.name

    lateinit var binding : ActivityPlaceViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_view)

        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        configureActivity(getIdFromIntent())
    }

    private fun getIdFromIntent(): Long {
        if (!intent.hasExtra(PLACE_ID_EXTRA)) throw IllegalArgumentException("id missing from intent")

        return intent.getLongExtra(PLACE_ID_EXTRA, -1)
    }

    private fun configureActivity(id : Long) {
        AsyncServiceRequest(
            Consumer<Place> {
                placeLoaded(it)
            },
            Consumer {
                Log.e(TAG, "Cannot get place from repo")
                it.printStackTrace()
                finish()
            },
            3
        ).execute(ServiceFactory.getDatabaseService().getPlace(id))
    }

    private fun placeLoaded(place: Place) {
        binding.placeName = place.name
    }

    companion object {
        fun launchForView(context: Context,  id : Long) {
            val intent = Intent(context, PlaceViewActivity::class.java)
            intent.putExtra(PLACE_ID_EXTRA, id)
            context.startActivity(intent)
        }
    }
}
