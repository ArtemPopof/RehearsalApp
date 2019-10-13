package ru.abbysoft.rehearsapp.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoomCreationViewModel : ViewModel() {
    val hasPhotos = MutableLiveData<Boolean>()
    val name = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val area = MutableLiveData<String>()
}