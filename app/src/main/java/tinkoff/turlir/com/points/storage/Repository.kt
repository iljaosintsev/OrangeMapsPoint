package tinkoff.turlir.com.points.storage

import io.reactivex.Completable
import io.reactivex.Flowable
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

    private val dataMapsPointMapper = DataMapsPointMapper()
    private val dataMapsPointListMapper = DataMapsPointListMapper(dataMapsPointMapper)

    fun pointById(id: String): Maybe<MapsPoint> {
        return pointDao.point(id).map(dataMapsPointMapper)
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
            .map(dataMapsPointListMapper)
    }

    fun allPoints(): Flowable<List<MapsPoint>> {
        return pointDao.points()
            .map(dataMapsPointListMapper)
    }

    fun setPointViewed(id: String): Completable {
        return Completable.fromAction {
            pointDao.setPointViewed(id)
        }
    }

    private fun savePoints(lst: List<Point>): Single<List<DataPoint>> {
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
            mapped
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