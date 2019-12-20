package ru.abbysoft.rehearsapp.map

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import androidx.core.util.Consumer
import com.google.android.gms.maps.model.LatLng
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.util.showErrorMessage
import java.io.IOException
import java.util.*

private val TAG = FetchAddressIntentService::class.java.name

class FetchAddressIntentService : IntentService("FetchAddressService") {
    private var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        intent ?: return

        receiver = getReceiverFromIntent(intent)?: return

        var errorMessage = ""

        // Get the location passed to this service through an extra.
        val location: LatLng = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA) ?: return

        val geocoder = Geocoder(this, Locale.getDefault())
        // ...

        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available)
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used)
            Log.e(TAG, "$errorMessage. Latitude = $location.latitude , " +
                    "Longitude =  $location.longitude", illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found)
                Log.e(TAG, errorMessage)
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
        } else {
            val address = addresses[0]
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            Log.d(TAG, getString(R.string.address_found))
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                addressFragments.joinToString(separator = "\n"))
        }
    }

    private fun getReceiverFromIntent(intent: Intent): ResultReceiver? {
        return intent.extras?.get(Constants.RECEIVER) as ResultReceiver
    }

    private fun deliverResultToReceiver(resultCode: Int, address: String) {
        val bundle = Bundle().apply { putString(Constants.RESULT_DATA_KEY, address) }
        receiver?.send(resultCode, bundle)
    }

    object Constants {
        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1
        const val PACKAGE_NAME = "ru.abbysoft.rehearsapp.map"
        const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
        const val LOCATION_DATA_EXTRA = "${PACKAGE_NAME}.LOCATION_DATA_EXTRA"
    }
}

class AddressResultReceiver(private val consumer: Consumer<String>) : ResultReceiver(Handler()) {
    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        // Display the address string
        // or an error message sent from the intent service.
        val addressOutput = resultData?.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY) ?: ""

        consumer.accept(addressOutput)
    }
}