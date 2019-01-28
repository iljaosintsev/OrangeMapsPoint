package tinkoff.turlir.com.points.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Network {

    @GET("deposition_points")
    fun points(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int,
        @Query("partners") partners: String? = null
    ): Single<PointsContainer>

    @GET("deposition_partners")
    fun partners(@Query("accountType") type: String = "Credit"): Single<PartnerContainer>
}