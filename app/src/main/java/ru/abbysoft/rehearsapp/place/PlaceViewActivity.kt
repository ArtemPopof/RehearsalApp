package ru.abbysoft.rehearsapp.place

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_place_view.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.cache.CacheFactory
import ru.abbysoft.rehearsapp.databinding.ActivityPlaceViewBinding
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.room.RoomCardFragment
import ru.abbysoft.rehearsapp.util.*
import java.lang.IllegalArgumentException

const val PLACE_ID_EXTRA = "PlaceidExtra"

class PlaceViewActivity : AppCompatActivity() {

    val TAG = PlaceViewActivity::class.java.name

    lateinit var binding : ActivityPlaceViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_view)

        setSupportActionBar(toolbar)
        change_header_fab.setOnClickListener { view ->
            pickImage(this)
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

        configureRoomFragments(place)
    }

    private fun configureRoomFragments(place: Place) {
        val roomCards = this.supportFragmentManager.fragments
        for (i in place.rooms.indices) {
            (roomCards[i] as RoomCardFragment).setRoom(place.rooms[i])
        }
    }

    companion object {
        fun launchForView(context: Context,  id : Long) {
            val intent = Intent(context, PlaceViewActivity::class.java)
            intent.putExtra(PLACE_ID_EXTRA, id)
            context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != PICK_IMAGE) {
            return
        }

        if (resultCode == Activity.RESULT_OK && data != null) {
            headerImageLoaded(data)
        } else {
            showErrorMessage(getString(R.string.error_loading_image), this)
        }
    }

    private fun headerImageLoaded(intent: Intent) {
        if (intent.data == null) {
            return
        }
        val stream = contentResolver.openInputStream(intent.data as Uri)
        var bitmap = BitmapFactory.decodeStream(stream)
        bitmap = shrinkWithProportion(bitmap, width = place_header_image.width)
        bitmap = cropImage(bitmap, place_header_image.width, place_header_image.height)

        binding.background = bitmap
    }
}
