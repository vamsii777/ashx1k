package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Shop

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(shops: List<Shop?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shop: Shop?)

    @Query("DELETE FROM SHOP")
    fun deleteAll()

    @get:Query("SELECT * FROM SHOP")
    val shopById: LiveData<Shop?>?

    @Query("DELETE FROM SHOP WHERE id = :id")
    fun deleteShopById(id: String?)
}