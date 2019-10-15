package ru.abbysoft.rehearsapp.login

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

/**
 * Vk User model class
 *
 * @author https://github.com/VKCOM/vk-android-sdk
 */
data class VkUser(val firstName: String? = "",
                  val lastName: String? = "",
                  val deactivated: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeByte(if (deactivated) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VkUser> {
        override fun createFromParcel(parcel: Parcel): VkUser {
            return VkUser(parcel)
        }

        override fun newArray(size: Int): Array<VkUser?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = VkUser(
                firstName = json.optString("first_name", ""),
                lastName = json.optString("last_name", ""))
    }

}