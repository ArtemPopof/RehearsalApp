package ru.abbysoft.rehearsapp.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.databinding.ActivityRoomCreationBinding
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.place.ROOM_REQUEST
import ru.abbysoft.rehearsapp.util.saveAsync

const val ROOM_EXTRA = "Room"

class RoomCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_creation)
        configureModel()
    }

    private fun configureModel() {
        val model = ViewModelProviders.of(this)[RoomCreationViewModel::class.java]
        binding.model = model
        model.name = getString(R.string.new_room)
        model.area = "0"
        model.price = "0"
    }

    fun save(view: View) {
        // TODO redo with cache
        val model = binding.model as RoomCreationViewModel
        val room = Room()
        room.name = model.name
        room.price = model.price.toFloat()
        room.area = model.area.toFloat()

        saveAsync(room, Consumer {result(it as Long)}, getString(R.string.cannot_save_room), this)
    }

    private fun result(id: Long) {
        val intent = Intent()
        intent.putExtra(ROOM_EXTRA, id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
