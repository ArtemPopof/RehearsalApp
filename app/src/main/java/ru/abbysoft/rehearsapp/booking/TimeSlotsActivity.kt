package ru.abbysoft.rehearsapp.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.util.Consumer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_time_slots.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.databinding.TimeSlotBinding
import ru.abbysoft.rehearsapp.login.AuntificationManager
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.model.TimeSlot
import ru.abbysoft.rehearsapp.room.ROOM_EXTRA
import ru.abbysoft.rehearsapp.util.bookSlot
import ru.abbysoft.rehearsapp.util.getUserId
import ru.abbysoft.rehearsapp.util.loadRoomAsync
import java.util.logging.Logger

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
            adapter = TimeSlotsAdapter(room.slots, Consumer { onSlotBooked(it) })
            layoutManager = LinearLayoutManager(this@TimeSlotsActivity)
        }
    }

    private fun onSlotBooked(slotBinding: TimeSlotBinding) {
        val userId = getUserId()
        if (userId == null) {
            Log.e("SlotBooking", "User id is null")
            return
        }

        val slot = slotBinding.slot as TimeSlot
        val slotId = slot.id
        bookSlot(
            slotId,
            userId,
            onSuccess= Runnable { slotBinding.bookedBy = AuntificationManager.user?.firstName}
        )
    }
}
