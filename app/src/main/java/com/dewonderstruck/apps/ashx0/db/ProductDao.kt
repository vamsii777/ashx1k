package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.FavouriteProduct
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.ProductListByCatId
import com.dewonderstruck.apps.ashx0.viewobject.RelatedProduct

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Dao
abstract class ProductDao {
    //region product list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(product: Product?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(productList: List<Product?>?)

    @Query("DELETE FROM PRODUCT")
    abstract fun deleteAll()

    @Query("SELECT p.* FROM Product p, ProductMap pm WHERE p.id = pm.productId AND pm.mapKey = :value ORDER BY pm.sorting asc")
    abstract fun getProductsByKey(value: String?): LiveData<List<Product?>?>?

    @Query("SELECT p.* FROM Product p, ProductMap pm WHERE p.id = pm.productId AND pm.mapKey = :value ORDER BY pm.sorting asc limit:loadFromDb")
    abstract fun getProductsByKeyByLimit(value: String?, loadFromDb: String?): LiveData<List<Product?>?>?

    //endregion
    //region product detail
    @Query("SELECT * FROM Product WHERE id =:productId ORDER BY addedDate DESC")
    abstract fun getProductById(productId: String?): LiveData<Product?>?

    //endregion
    //region Product List by Category Id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(productListByCatId: ProductListByCatId?)

    @Query("DELETE FROM ProductListByCatId")
    abstract fun deleteAllProductListByCatIdVOs()

    @Query("SELECT * FROM product WHERE id IN (SELECT productId FROM ProductListByCatId WHERE catId=:catId) ORDER BY addedDate DESC")
    abstract fun getAllProductListByCatId(catId: String?): LiveData<List<Product?>?>?

    //endregion
    //region related
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(relatedProducts: RelatedProduct?)

    @Query("DELETE FROM RelatedProduct")
    abstract fun deleteAllRelatedProducts()

    @get:Query("SELECT * FROM Product WHERE id IN (SELECT id FROM RelatedProduct) ORDER BY addedDate DESC")
    abstract val allRelatedProducts: LiveData<List<Product?>?>?

    @Query("DELETE FROM Product WHERE id in (SELECT id FROM RelatedProduct)")
    abstract fun deleteAllBasedOnRelated()

    //endregion
    //region favourite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavourite(favouriteProduct: FavouriteProduct?)

    @Query("DELETE FROM favouriteproduct")
    abstract fun deleteAllFavouriteProducts()

    @Query("DELETE FROM favouriteproduct where productId = :productId")
    abstract fun deleteFavouriteProductByProductId(productId: String?)

    @get:Query("SELECT prd.* FROM Product prd, favouriteproduct fp WHERE prd.id = fp.productId order by fp.sorting ")
    abstract val allFavouriteProducts: LiveData<List<Product?>?>?

    @get:Query("SELECT max(sorting) from favouriteproduct")
    abstract val maxSortingFavourite: Int

    @Query("UPDATE Product SET isFavourited =:is_favourited WHERE id =:productId")
    abstract fun updateProductForFavById(productId: String?, is_favourited: String?)

    @Query("SELECT isFavourited FROM Product WHERE id =:productId")
    abstract fun selectFavouriteById(productId: String?): String?

    @Query("DELETE FROM Product WHERE id =:productId")
    abstract fun deleteProductById(productId: String?) //endregion
}