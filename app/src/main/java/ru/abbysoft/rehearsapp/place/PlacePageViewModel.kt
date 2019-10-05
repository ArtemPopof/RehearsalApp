package ru.abbysoft.rehearsapp.place

import androidx.lifecycle.ViewModel
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.Room

class PlacePageViewModel : ViewModel() {

    var place : Place? = null

    fun getRoom(index: Int) : Room? {
        return place?.rooms?.get(index)
    }
}