package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Rating

@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ratingList: List<Rating?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rating: Rating?)

    @Query("DELETE FROM Rating")
    fun deleteAll()

    @Query("SELECT * FROM Rating WHERE productId = :productId")
    fun getRatingById(productId: String?): LiveData<List<Rating?>?>?
}