package ru.abbysoft.rehearsapp

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.location
import java.util.*

private const val LAT = 42.5124
private const val LNG = -20.5355
private const val DELTA = 0.0001

class PlaceExtensionTest {

    private val random = Random(System.currentTimeMillis())

    @Test
    fun testLocationConversionShouldNotRaiseException() {
        val place = Place().apply { position = "$LAT,$LNG" }

        val result = place.location()

        assertEquals(LAT, result.latitude, DELTA)
        assertEquals(LNG, result.longitude, DELTA)
    }

    @Test
    fun testEmptyLocationConversionShouldReturnNull() {
        val place = Place()

        val result = place.location()

        assertEquals(null, result)
    }

    private fun generatePlaceWithRandomLocation(): Place {
        val place = Place()
        val randLat = random.nextFloat() * 90
        val randLng = random.nextFloat() * 90
        val randPlace = "$randLat,$randLng"

        return place.apply { position = randPlace }
    }
}