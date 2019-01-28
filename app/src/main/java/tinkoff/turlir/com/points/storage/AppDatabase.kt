package tinkoff.turlir.com.points.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Partner::class, DataPoint::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun partnerDao(): PartnerDao

    abstract fun pointDao(): PointDao
}