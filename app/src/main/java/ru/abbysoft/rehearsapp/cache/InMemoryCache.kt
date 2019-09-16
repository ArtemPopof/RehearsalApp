package ru.abbysoft.rehearsapp.cache

import android.util.SparseArray
import ru.abbysoft.rehearsapp.model.Place

var counter = 0

class InMemoryCache : Cache {

    private val storage = SparseArray<Place>(20)

    override fun getPlace(id: Long): Place {
        return storage.get(id.toInt())
    }

    override fun addPlace(place: Place) {
        storage.put(counter++, place)
    }
}