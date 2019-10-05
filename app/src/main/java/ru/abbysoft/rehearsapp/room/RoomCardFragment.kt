package ru.abbysoft.rehearsapp.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.databinding.RoomCardBinding
import ru.abbysoft.rehearsapp.model.Room

class RoomCardFragment : Fragment() {

    lateinit var binding: RoomCardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
            .inflate(inflater, R.layout.room_card, container, false)

        binding.context = context

        return binding.root
    }

    fun setRoom(room: Room) {
        binding.room = room
    }


}