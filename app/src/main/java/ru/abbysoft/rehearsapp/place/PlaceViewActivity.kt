package ru.abbysoft.rehearsapp.place

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_place_view.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.databinding.ActivityPlaceViewBinding
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.room.ROOM_EXTRA
import ru.abbysoft.rehearsapp.room.RoomCreationActivity
import ru.abbysoft.rehearsapp.util.*
import java.lang.IllegalArgumentException

const val PLACE_ID_EXTRA = "PlaceidExtra"
const val ROOM_REQUEST = 1

class PlaceViewActivity : AppCompatActivity() {

    private val TAG = PlaceViewActivity::class.java.name

    private lateinit var binding : ActivityPlaceViewBinding

    var id: Long? = -1L

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
        this.id = id

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
        binding.place = place
        loadBitmapOrNull(place, Consumer {binding.background = it}, this)
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

        val success = resultCode == Activity.RESULT_OK
        if (!success) {
            return
        }

        when (requestCode) {
            PICK_IMAGE -> handlePickImageResult(data, Consumer { headerImageLoaded(it) }, this)
            ROOM_REQUEST -> roomRequestResult(data, success)
        }
    }

    private fun roomRequestResult(data: Intent?, success: Boolean) {
        if (data == null || !success) {
            showErrorMessage(getString(R.string.failed_to_add_room), this)
        } else {
            // TODO cache
            val roomId = data.extras?.getLong(ROOM_EXTRA) ?: return
            getRoomFromCache(roomId, Consumer { roomAdded(it) })
        }
    }

    private fun headerImageLoaded(data: Uri) {
        val stream = contentResolver.openInputStream(data)
        var bitmap = BitmapFactory.decodeStream(stream)
        bitmap = shrinkWithProportion(bitmap, width = place_header_image.width)
        bitmap = cropImage(bitmap, place_header_image.width, place_header_image.height)

        binding.background = bitmap

        saveImageToServer(bitmap.toByteArray(),
            Consumer {
            // update place info with new header image
                updateImage(id, it)
            },
            Runnable {
                showErrorMessage( getString(R.string.cannot_upload_header_image), this)
            })
    }

    private fun updateImage(id: Long?, headerId: String) {
        if (id == null) {
            return
        }

        val updatedPlace = binding.place?.apply { headerImageId = headerId }

        AsyncServiceRequest(
            Consumer<Boolean>{
                if (!it) {
                    Log.e(TAG, "Cannot save headerImage, server error")
                } else {
                    Log.i(TAG, "Image updated")
                }
            },
            Consumer {
                showErrorMessage(getString(R.string.cannot_upload_header_image), this)
            }
        ).execute(ServiceFactory.getDatabaseService()
            .updatePlace(updatedPlace as Place))
    }

    fun addNewRoom(view: View) {
        launchActivityForResult(this, RoomCreationActivity::class.java, ROOM_REQUEST)
    }

    private fun roomAdded(room: Room) {
        val place = binding.place as Place
        place.rooms = add(place.rooms, room)
        updatePlaceAsync(place, "Failed to add new room", this)
        binding.place = place
    }

}
