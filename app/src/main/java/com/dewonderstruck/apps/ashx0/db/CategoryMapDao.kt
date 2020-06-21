package com.dewonderstruck.apps.ashx0.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.CategoryMap

@Dao
interface CategoryMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoryMap: CategoryMap?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categoryMapList: List<Category?>?)

    @Query("DELETE FROM CategoryMap WHERE mapKey = :key")
    fun deleteByMapKey(key: String?)

    @Query("SELECT max(sorting) from CategoryMap WHERE mapKey = :value ")
    fun getMaxSortingByValue(value: String?): Int

    @get:Query("SELECT * FROM CategoryMap")
    val all: List<CategoryMap?>?

    @Query("DELETE FROM CategoryMap")
    fun deleteAll()
}