package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail

@Dao
abstract class ProductAttributeDetailDao {
    //region product attribute detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(productAttributeDetailList: List<ProductAttributeDetail?>?)

    @Query("SELECT * FROM ProductAttributeDetail WHERE productId =:productId AND headerId=:headerId")
    abstract fun getProductAttributeDetailById(productId: String?, headerId: String?): LiveData<List<ProductAttributeDetail?>?>?

    @Query("DELETE FROM ProductAttributeDetail WHERE productId =:productId AND headerId=:headerId")
    abstract fun deleteProductAttributeDetailById(productId: String?, headerId: String?)

    @Query("DELETE FROM productattributedetail")
    abstract fun deleteAll()
}