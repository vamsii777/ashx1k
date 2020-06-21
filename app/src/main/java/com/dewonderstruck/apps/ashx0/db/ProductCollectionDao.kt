package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollection
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader

/**
 * Created by Vamsi Madduluri on 10/27/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Dao
abstract class ProductCollectionDao {
    //region Collection Header
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllCollectionHeader(productCollectionHeaderList: List<ProductCollectionHeader?>?)

    @get:Query("SELECT * FROM ProductCollectionHeader ORDER BY addedDate DESC")
    abstract val all: LiveData<List<ProductCollectionHeader?>?>?

    @Query("SELECT * FROM (SELECT * FROM ProductCollectionHeader ORDER BY addedDate DESC) LIMIT :limit")
    abstract fun getAllListByLimit(limit: Int): List<ProductCollectionHeader>

    @Query("SELECT * FROM Product WHERE id in " +
            "(SELECT productId FROM ProductCollection WHERE collectionId = :collectionId ) " +
            "ORDER BY addedDate DESC")
    abstract fun getProductListByCollectionId(collectionId: String?): LiveData<List<Product?>?>?

    @Query("SELECT * FROM " +
            "(SELECT * FROM Product WHERE id in " +
            "(SELECT productId FROM ProductCollection WHERE collectionId = :collectionId ) " +
            "ORDER BY addedDate DESC ) " +
            "LIMIT :limit")
    abstract fun getProductListByCollectionIdWithLimit(collectionId: String?, limit: Int): List<Product?>?

    @Query("DELETE FROM ProductCollectionHeader")
    abstract fun deleteAll()
    fun getAllIncludingProductList(collectionLimit: Int, productLimit: Int): List<ProductCollectionHeader> {
        val productCollectionHeaderList = getAllListByLimit(collectionLimit)
        for (i in productCollectionHeaderList.indices) {
            productCollectionHeaderList[i].productList = getProductListByCollectionIdWithLimit(productCollectionHeaderList[i].id, productLimit)
        }
        return productCollectionHeaderList
    }

    //endregion
    //region Collection
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(productCollection: ProductCollection?)

    @Query("DELETE FROM ProductCollection WHERE collectionId = :id ")
    abstract fun deleteAllBasedOnCollectionId(id: String?) //endregion
}