package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.ProductColor

@Dao
abstract class ProductColorDao {
    //region product color
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(productColorList: List<ProductColor?>?)

    @Query("SELECT * FROM ProductColor WHERE productId =:productId")
    abstract fun getProductColorById(productId: String?): LiveData<List<ProductColor?>?>?

    @Query("DELETE FROM ProductColor WHERE productId =:productId")
    abstract fun deleteProductColorById(productId: String?)

    @Query("DELETE FROM ProductColor")
    abstract fun deleteAll()
}