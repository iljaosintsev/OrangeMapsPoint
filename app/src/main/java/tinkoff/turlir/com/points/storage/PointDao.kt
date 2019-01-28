package tinkoff.turlir.com.points.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PointDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(lst: List<DataPoint>)
}