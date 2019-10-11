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
    }

    fun setRoom(room: Room?) {
        binding?.room = room
    }
}