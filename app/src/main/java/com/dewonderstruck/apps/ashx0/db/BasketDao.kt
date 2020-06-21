package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.Product

@Dao
abstract class BasketDao {
    //region basket
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(basket: Basket?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(basket: Basket?)

    @get:Query("SELECT * FROM Basket")
    abstract val allBasketList: LiveData<List<Basket?>?>?

    @Query("SELECT id FROM Basket where productId = :productId and selectedAttributes = :selectedAttributes and selectedColorId = :selectedColorId")
    abstract fun getBasketId(productId: String?, selectedAttributes: String?, selectedColorId: String?): Int

    @get:Query("SELECT * FROM Basket")
    abstract val allBasketListData: List<Basket>

    @Query("SELECT * FROM Product WHERE id = :productId LIMIT 1")
    abstract fun getProductById(productId: String?): Product?

    @Query("UPDATE Basket SET count =:count WHERE id = :id")
    abstract fun updateBasketById(id: Int, count: Int)

    @Query("DELETE FROM Basket WHERE id =:id")
    abstract fun deleteBasketById(id: Int)

    @Query("DELETE FROM Basket")
    abstract fun deleteAllBasket()

    //endregion
    val allBasketWithProduct: List<Basket>
        get() {
            val allBasketListData = allBasketListData
            for (i in allBasketListData.indices) {
                allBasketListData[i].product = getProductById(allBasketListData[i].productId)
            }
            return allBasketListData
        }
}