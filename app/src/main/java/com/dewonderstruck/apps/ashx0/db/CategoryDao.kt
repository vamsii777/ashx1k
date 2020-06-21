package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(category: Category?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Category?>?)

    @Query("DELETE FROM Category")
    fun deleteAllCategory()

    @Query("DELETE FROM Category WHERE id = :id")
    fun deleteCategoryById(id: String?)

    @Query("SELECT c.* FROM Category c, CategoryMap cm WHERE c.id = cm.categoryId AND cm.mapKey = :value ORDER BY cm.sorting asc")
    fun getCategoryByKeyTest(value: String?): LiveData<List<Category?>?>?

    @Query("SELECT c.* FROM Category c, CategoryMap cm WHERE c.id = cm.categoryId AND cm.mapKey = :value ORDER BY cm.sorting asc limit:loadFromDB")
    fun getCategoryByKeyTestByLimit(value: String?, loadFromDB: String?): LiveData<List<Category?>?>?
}