package ru.abbysoft.rehearsapp.room

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_room_creation.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.component.PhotoSliderAdapter
import ru.abbysoft.rehearsapp.databinding.ActivityRoomCreationBinding
import ru.abbysoft.rehearsapp.model.Image
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.util.*

const val ROOM_EXTRA = "Room"

class RoomCreationActivity : AppCompatActivity() {

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
        model.name.value = getString(R.string.new_room)
        model.area.value = "0"
        model.price.value = "0"
        model.hasPhotos.value = false
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
}
