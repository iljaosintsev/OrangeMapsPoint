package tinkoff.turlir.com.points.storage

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Maybe
import io.reactivex.Single
import tinkoff.turlir.com.points.maps.MapsPoint
import javax.inject.Inject

class Repository
@Inject constructor(
    database: AppDatabase,
    private val network: Network
) {

    private val partnerDao = database.partnerDao()
    private val pointDao = database.pointDao()

    fun partnerById(id: String): Maybe<Partner> {
        return partnerDao.partner(id)
            .switchIfEmpty(reloadPartners(id))
    }

    fun pointById(id: String): Maybe<MapsPoint> {
        return pointDao.point(id).map { item ->
            MapsPoint(
                externalId = item.externalId,
                partnerName = item.partnerName,
                workHours = item.workHours,
                addressInfo = item.addressInfo,
                fullAddress = item.fullAddress,
                location = LatLng(item.latitude, item.longitude)
            )
        }
    }

    fun loadPoints(
        latitude: Double,
        longitude: Double,
        radius: Int,
        partners: String? = null
    ): Single<List<MapsPoint>> {

        return network.points(latitude, longitude, radius, partners)
            .flatMap {
                savePoints(it.payload)
            }
            .map {
                it.map { item ->
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

    private fun savePoints(lst: List<Point>): Single<List<Point>> {
        return Single.fromCallable {
            val mapped = lst.map {
                DataPoint(
                    externalId = it.externalId,
                    partnerName = it.partnerName,
                    workHours = it.workHours,
                    addressInfo = it.addressInfo,
                    fullAddress = it.fullAddress,
                    latitude = it.location.latitude,
                    longitude = it.location.longitude,
                    viewed = false
                )
            }
            pointDao.insert(mapped)
            lst
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