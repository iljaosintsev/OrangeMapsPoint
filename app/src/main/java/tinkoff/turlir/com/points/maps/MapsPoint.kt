package tinkoff.turlir.com.points.maps

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

data class MapsPoint(
    val externalId: String,
    val partnerName: String,
    val workHours: String?,
    val addressInfo: String?,
    val fullAddress: String,
    val location: LatLng,
    val viewed: Boolean
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readParcelable(LatLng::class.java.classLoader)!!,
        parcel.readInt() == 1
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(externalId)
        dest.writeString(partnerName)
        dest.writeString(workHours)
        dest.writeString(addressInfo)
        dest.writeString(fullAddress)
        dest.writeParcelable(location, flags)
        dest.writeInt(if (viewed) 1 else 0)
    }

    override fun describeContents() = 0

    fun picture(name: String, density: String): String {
        return "https://static.tinkoff.ru/icons/deposition-partners-v3/$density/$name"
    }

    companion object CREATOR : Parcelable.Creator<MapsPoint> {
        override fun createFromParcel(parcel: Parcel): MapsPoint {
            return MapsPoint(parcel)
        }

        override fun newArray(size: Int): Array<MapsPoint?> {
            return arrayOfNulls(size)
        }
    }
}