package ru.abbysoft.rehearsapp

import com.google.android.gms.maps.model.LatLng
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Test
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.baseUrl

class RestServiceTest {

    @Test
    fun initialisationOfRestServiceMustSuccesseed() {
        ServiceFactory.init(baseUrl)
    }

    @Test
    fun testAddResourceAndGetItBack() {
        // TODO make mocking, or wipe data out of real db
//        ServiceFactory.init(baseUrl)
//
//        val place = Place(-1, "place", 3, LatLng(25.44, 44.22).toString())
//        val placeAddCall = ServiceFactory.getDatabaseService().addPlace(place)
//        val placeId = placeAddCall.execute().body()
//
//        assertNotNull(placeId)
//
//        val getPlaceCall = ServiceFactory.getDatabaseService().getPlace(placeId as Long)
//        val returnedPlace = getPlaceCall.execute().body()
//
//        assertEquals(returnedPlace?.id, placeId)
//        assertEquals(returnedPlace?.name, place.name)
//        assertEquals(returnedPlace?.position, place.position)
    }
}