package tinkoff.turlir.com.points.network

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Maybe
import io.reactivex.Single
import tinkoff.turlir.com.points.MapsPoint
import javax.inject.Inject

class PointsRepository
@Inject constructor(
    database: AppDatabase,
    private val network: Network
) {

    private val partnerDao = database.partnerDao()

    fun partnerById(id: String): Maybe<Partner> {
        return partnerDao.partner(id)
            .switchIfEmpty(reloadPartners(id))
    }

    fun loadPoints(
        latitude: Double,
        longitude: Double,
        radius: Int,
        partners: String? = null
    ): Single<List<MapsPoint>> {

        return network.points(latitude, longitude, radius, partners)
            .map {
                it.payload.map { item ->
                    MapsPoint(
                        externalId = item.externalId,
                        partnerName = item.partnerName,
                        workHours = item.workHours,
                        addressInfo = item.addressInfo,
                        fullAddress = item.fullAddress,
                        location = LatLng(item.location.latitude, item.location.longitude)
                    )
                }
            }
    }

    private fun reloadPartners(id: String): Maybe<Partner> {
        return loadPartners()
            .toObservable()
            .flatMapSingle { items ->
                savePartners(items)
            }
            .firstElement()
            .flatMap {
                partnerDao.partner(id)
            }

    }

    private fun loadPartners(type: String = "Credit"): Single<List<Partner>> {
        return network.partners(type)
            .map { it.payload }
    }

    private fun savePartners(lst: List<Partner>): Single<Int> {
        return Single.fromCallable {
            partnerDao.insertPartners(lst)
            lst.size
        }
    }
}