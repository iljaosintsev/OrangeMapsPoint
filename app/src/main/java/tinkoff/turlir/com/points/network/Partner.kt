package tinkoff.turlir.com.points.network

class PartnerContainer(override val payload: List<Partner>) : Container<Partner>()

data class Partner(
    val id: String,
    val name: String,
    val picture: String,
    val desc: String
) {
    fun picture(density: String): String {
        return "https://static.tinkoff.ru/icons/deposition-partners-v3/$density/$picture"
    }
}