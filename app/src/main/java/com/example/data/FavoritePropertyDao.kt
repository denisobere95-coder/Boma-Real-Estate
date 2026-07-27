package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePropertyDao {
    @Query("SELECT propertyId FROM favorite_properties")
    fun getAllFavoritePropertyIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_properties WHERE propertyId = :propertyId)")
    suspend fun isFavorite(propertyId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteProperty)

    @Query("DELETE FROM favorite_properties WHERE propertyId = :propertyId")
    suspend fun removeFavorite(propertyId: String)

    @Query("DELETE FROM favorite_properties")
    suspend fun clearAllFavorites()
}
