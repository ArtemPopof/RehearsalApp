package ru.abbysoft.rehearsapp.place

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import ru.abbysoft.rehearsapp.R

class PlaceCreationViewModel(app: Application) : AndroidViewModel(app) {

    var name: String = app.getString(R.string.new_place_placeholder)
    var position: LatLng? = null
}
