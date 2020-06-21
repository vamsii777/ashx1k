package com.dewonderstruck.apps.ashx0.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.ProductMap

@Dao
interface ProductMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productMap: ProductMap?)

    @Query("DELETE FROM ProductMap WHERE mapKey = :key")
    fun deleteByMapKey(key: String?)

    @Query("SELECT max(sorting) from ProductMap WHERE mapKey = :value ")
    fun getMaxSortingByValue(value: String?): Int

    @get:Query("SELECT * FROM ProductMap")
    val all: List<ProductMap?>?

    @Query("DELETE FROM ProductMap")
    fun deleteAll()
}