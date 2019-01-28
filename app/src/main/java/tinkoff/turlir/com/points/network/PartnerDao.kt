package tinkoff.turlir.com.points.network

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface PartnerDao {

    @Query("SELECT * FROM partners WHERE id = :id")
    fun partner(id: String): Maybe<Partner>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPartners(lst: List<Partner>)
}