package ru.abbysoft.rehearsapp.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.databinding.RoomCardBinding
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.room.ROOM_EXTRA
import ru.abbysoft.rehearsapp.room.RoomViewActivity
import ru.abbysoft.rehearsapp.util.launchActivity


class RoomCardView : FrameLayout {

    private var binding: RoomCardBinding? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = DataBindingUtil.inflate(inflater, R.layout.room_card, this, true)

        configureOnClick()
    }

    private fun configureOnClick() {
        setOnClickListener {
            val roomId = binding?.room?.id
            if (roomId != null) {
                it.context.launchActivity(RoomViewActivity::class.java)
                    .putExtra(ROOM_EXTRA, roomId)
                    .start()
            }
        }
    }

    fun setRoom(room: Room?) {
        if (room == null) {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
        binding?.room = room
    }
}