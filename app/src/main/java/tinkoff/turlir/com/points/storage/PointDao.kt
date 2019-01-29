package tinkoff.turlir.com.points.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface PointDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(lst: List<DataPoint>)

    @Query("SELECT * FROM points WHERE externalId = :id")
    fun point(id: String): Maybe<DataPoint>
}