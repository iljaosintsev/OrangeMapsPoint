package tinkoff.turlir.com.points.storage

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

class PartnerContainer(override val payload: List<Partner>) : Container<Partner>()

@Entity(tableName = "partners")
data class Partner(
    @PrimaryKey
    val id: String,
    val name: String,
    val picture: String,
    val description: String
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(picture)
        dest.writeString(description)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Partner> {
        override fun createFromParcel(parcel: Parcel): Partner {
            return Partner(parcel)
        }

        override fun newArray(size: Int): Array<Partner?> {
            return arrayOfNulls(size)
        }
    }
}