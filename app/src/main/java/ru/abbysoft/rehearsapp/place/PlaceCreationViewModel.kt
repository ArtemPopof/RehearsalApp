package ru.abbysoft.rehearsapp.place

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ru.abbysoft.rehearsapp.R

class PlaceCreationViewModel(app: Application) : AndroidViewModel(app) {

    var name = app.getString(R.string.default_place_name)
    var position: LatLng? = null
}
