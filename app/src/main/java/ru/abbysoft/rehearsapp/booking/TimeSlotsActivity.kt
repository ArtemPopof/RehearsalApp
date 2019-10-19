package ru.abbysoft.rehearsapp.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.util.Consumer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_time_slots.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.room.ROOM_EXTRA
import ru.abbysoft.rehearsapp.util.loadRoomAsync

class TimeSlotsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slots)

        // TODO use cache
        getRoomFromIntent()
    }

    private fun getRoomFromIntent() {
        val roomId = intent.extras?.getLong(ROOM_EXTRA)
        loadRoomAsync(roomId as Long, Consumer { configureRecyclerView(it) })
    }

    private fun configureRecyclerView(room: Room) {
        time_slots_recycler.apply {
            setHasFixedSize(true)
            adapter = TimeSlotsAdapter(room.slots)
            layoutManager = LinearLayoutManager(this@TimeSlotsActivity)
        }
    }
}
