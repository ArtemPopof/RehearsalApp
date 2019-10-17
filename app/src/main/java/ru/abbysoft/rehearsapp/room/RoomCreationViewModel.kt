package ru.abbysoft.rehearsapp.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abbysoft.rehearsapp.model.Room

class RoomCreationViewModel : ViewModel() {
//    val name = MutableLiveData<String>()
//    val priceFrom = MutableLiveData<String>()
//    val area = MutableLiveData<String>()

    val room = MutableLiveData<Room>()
}