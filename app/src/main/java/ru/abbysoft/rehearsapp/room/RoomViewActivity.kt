package ru.abbysoft.rehearsapp.room

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_room_creation.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.booking.TimeSlotsActivity
import ru.abbysoft.rehearsapp.component.PhotoSliderAdapter
import ru.abbysoft.rehearsapp.databinding.ActivityRoomCreationBinding
import ru.abbysoft.rehearsapp.model.Image
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.util.*

const val ROOM_EXTRA = "Room"

class RoomViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomCreationBinding
    private val photos = ArrayList<Image>(10)
    private lateinit var adapter: PhotoSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_creation)

        configureModel()
        configureGallery()
    }

    private fun configureModel() {
        val model = ViewModelProviders.of(this)[RoomCreationViewModel::class.java]
        binding.model = model

        // if this activity used for view of existing room
        loadRoomFromIntent()

        if (model.room.value == null) {
            configureNewRoom(model)
        }
    }

    private fun loadRoomFromIntent() {
        val roomId = intent.extras?.getLong(ROOM_EXTRA) ?: return

        loadRoomAsync(roomId, Consumer {openRoomForView(it)})
    }

    private fun openRoomForView(room: Room) {
        binding.model?.room?.value = room

        saveButton.visibility = View.GONE
        bookButton.visibility = View.VISIBLE
        room_creation_name.isEnabled = false
        room_area_field.isEnabled = false
        room_price_field.isEnabled = false
    }

    private fun configureNewRoom(model: RoomCreationViewModel) {
        val room = Room()
        room.name = getString(R.string.new_room)

        model.room.value = room
    }

    private fun configureGallery() {
        adapter = PhotoSliderAdapter(photos, this)
        imageSlider.sliderAdapter = adapter
    }

    fun save(view: View) {
        // TODO redo with cache
        val room = Room()

        if (!room_creation_name.validateThatNotBlank()) return
        if (!room_area_field.validateThatNotBlank()) return
        if (!room_price_field.validateThatNotBlank()) return

        room.name = room_creation_name.text.toString()
        room.price = room_price_field.text.toString().toFloat()
        room.area = room_area_field.text.toString().toFloat()
        room.images = photos

        saveAsync(room, Consumer {finishWithResult(it as Long)}, getString(R.string.cannot_save_room))
    }

    private fun finishWithResult(id: Long) {
        val intent = Intent()
        intent.putExtra(ROOM_EXTRA, id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            handlePickImageResult(data, Consumer { savePhoto(it) }, this)
        }
    }

    private fun savePhoto(data: Uri) {
        val stream = contentResolver.openInputStream(data)

        val bitmap = BitmapFactory.decodeStream(stream) ?: return
        val bytes = bitmap.toByteArray()
        saveImageToServer(bytes, Consumer { adapter.onPhotoAdded(it) }, Runnable { showErrorMessage(getString(
                    R.string.failed_to_save), this) })
    }

    fun book(view: View) {
        val roomId = binding.model?.room?.value?.id
        if (roomId == null) {
            Log.e("RoomViewActivity", "Something goes wrong")
            return
        }

        this.launchActivity(TimeSlotsActivity::class.java).putExtra(ROOM_EXTRA, roomId).start()
    }
}
