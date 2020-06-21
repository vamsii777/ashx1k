package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.ProductSpecs

@Dao
abstract class ProductSpecsDao {
    //region product color
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(productSpecsList: List<ProductSpecs?>?)

    @Query("SELECT * FROM ProductSpecs WHERE productId =:productId")
    abstract fun getProductSpecsById(productId: String?): LiveData<List<ProductSpecs?>?>?

    @Query("DELETE FROM ProductSpecs WHERE productId =:productId")
    abstract fun deleteProductSpecsById(productId: String?)

    @Query("DELETE FROM ProductSpecs")
    abstract fun deleteAll()
}