package tinkoff.turlir.com.points.network

import io.reactivex.Single
import javax.inject.Inject

class PointsRepository @Inject constructor(private val network: Network) {

    fun loadPoints(
        latitude: Double,
        longitude: Double,
        radius: Int,
        partners: String? = null
    ): Single<List<Point>> {

        return network.points(latitude, longitude, radius, partners)
            .map { it.payload }
    }
}