package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.ShippingMethod

@Dao
interface ShippingMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(shippingMethods: List<ShippingMethod?>?)

    @get:Query("SELECT * FROM ShippingMethod")
    val shippingMethods: LiveData<List<ShippingMethod?>?>?

    @Query("DELETE FROM ShippingMethod")
    fun deleteAll()
}