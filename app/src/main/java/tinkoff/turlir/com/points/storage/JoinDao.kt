package tinkoff.turlir.com.points.storage

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface JoinDao {

    @Query("SELECT points.*, partners.picture FROM points INNER JOIN partners ON points.partnerName = partners.id")
    fun merge(): Flowable<List<PointMergePartner>>
}