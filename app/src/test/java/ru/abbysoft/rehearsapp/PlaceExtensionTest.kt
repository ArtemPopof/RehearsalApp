package ru.abbysoft.rehearsapp

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.location
import ru.abbysoft.rehearsapp.util.MOSCOW
import java.util.*

private const val LAT = 59.89771530274918
private const val LNG = 30.414091050624847
private const val POSITION = "lat/lng: (59.89771530274918,30.414091050624847)"
private const val DELTA = 0.000000000000001

class PlaceExtensionTest {

    private val random = Random(System.currentTimeMillis())

    @Test
    fun testLocationConversionShouldNotRaiseException() {
        val place = Place().apply { position = POSITION }

        val result = place.location()

        assertEquals(LAT, result.latitude, DELTA)
        assertEquals(LNG, result.longitude, DELTA)
    }

    @Test
    fun testEmptyLocationConversionShouldReturnDefaultLocation() {
        val place = Place()

        val result = place.location()

        assertEquals(MOSCOW, result)
    }

    private fun generatePlaceWithRandomLocation(): Place {
        val place = Place()
        val randLat = random.nextFloat() * 90
        val randLng = random.nextFloat() * 90
        val randPlace = "$randLat,$randLng"

        return place.apply { position = randPlace }
    }
}