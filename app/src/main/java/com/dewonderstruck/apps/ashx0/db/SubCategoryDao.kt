package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory

@Dao
interface SubCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subCategory: SubCategory?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(subCategory: SubCategory?)

    @Query("DELETE FROM SUBCATEGORY")
    fun deleteAllSubCategory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(subCategories: List<SubCategory?>?)

    @get:Query("SELECT * FROM SUBCATEGORY ORDER BY addedDate DESC")
    val allSubCategory: LiveData<List<SubCategory?>?>?

    @Query("SELECT * FROM SUBCATEGORY WHERE catId=:catId")
    fun getSubCategoryList(catId: String?): LiveData<List<SubCategory?>?>?
}