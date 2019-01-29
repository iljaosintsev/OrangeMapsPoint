package tinkoff.turlir.com.points.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface PointDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(lst: List<DataPoint>)

    @Query("SELECT * FROM points WHERE externalId = :id")
    fun point(id: String): Maybe<DataPoint>

    @Query("UPDATE points SET viewed = 1 WHERE externalId = :id")
    fun setPointViewed(id: String)

    @Query("SELECT * FROM points")
    fun points(): Flowable<List<DataPoint>>
}