package tinkoff.turlir.com.points.storage

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
)