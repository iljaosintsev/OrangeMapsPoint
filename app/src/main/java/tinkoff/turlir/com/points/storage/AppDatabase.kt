package tinkoff.turlir.com.points.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Partner::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun partnerDao(): PartnerDao
}