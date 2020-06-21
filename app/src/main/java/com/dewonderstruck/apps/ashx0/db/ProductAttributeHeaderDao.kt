package com.dewonderstruck.apps.ashx0.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeHeader

@Dao
abstract class ProductAttributeHeaderDao {
    //region product attribute header
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(productAttributeHeaderList: List<ProductAttributeHeader?>?)

    @Query("SELECT * FROM ProductAttributeHeader WHERE productId =:productId order by addedDate desc")
    abstract fun getProductAttributeHeaderById(productId: String?): List<ProductAttributeHeader>

    @Query("SELECT * FROM ProductAttributeDetail WHERE productId =:productId AND headerId=:headerId")
    abstract fun getProductAttributeDetailById(productId: String?, headerId: String?): List<ProductAttributeDetail?>?

    @Query("DELETE FROM ProductAttributeHeader WHERE productId =:productId")
    abstract fun deleteProductAttributeHeaderById(productId: String?)

    @Query("DELETE FROM ProductAttributeHeader")
    abstract fun deleteAll()
    fun getProductAttributeHeaderAndDetailById(productId: String?): List<ProductAttributeHeader> {
        val productAttributeHeaderList = getProductAttributeHeaderById(productId)
        for (i in productAttributeHeaderList.indices) {
            productAttributeHeaderList[i].attributesDetailList = getProductAttributeDetailById(productId, productAttributeHeaderList[i].id)
        }
        return productAttributeHeaderList
    }
}