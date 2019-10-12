package ru.abbysoft.rehearsapp.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoomCreationViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val area = MutableLiveData<String>()

    fun validate(): Boolean {
        if (name.value.isNullOrBlank()) {
            return false
        }
        if (price.value.isNullOrBlank()) {
            return false
        }
        if (area.value.isNullOrBlank()) {
            return false
        }

        return true
    }
}