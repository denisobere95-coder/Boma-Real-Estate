package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Property::class,
        ViewingRequest::class,
        DocumentVaultItem::class,
        MortgageLead::class,
        FavoriteProperty::class,
        RecentSearch::class
    ],
    version = 4,
    exportSchema = false
)
abstract class BomaDatabase : RoomDatabase() {
    abstract fun bomaDao(): BomaDao
    abstract fun favoritePropertyDao(): FavoritePropertyDao
    abstract fun recentSearchDao(): RecentSearchDao

    companion object {
        @Volatile
        private var INSTANCE: BomaDatabase? = null

        fun getDatabase(context: Context): BomaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BomaDatabase::class.java,
                    "boma_real_estate.db"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
