package tinkoff.turlir.com.points.network

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
) {
    fun picture(density: String): String {
        return "https://static.tinkoff.ru/icons/deposition-partners-v3/$density/$picture"
    }
}