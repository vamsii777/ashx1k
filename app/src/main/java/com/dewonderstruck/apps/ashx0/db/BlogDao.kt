package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Blog

@Dao
interface BlogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(blogs: List<Blog?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blog: Blog?)

    @Query("SELECT * FROM Blog WHERE id = :id")
    fun getBlogById(id: String?): LiveData<Blog?>?

    @get:Query("SELECT * FROM Blog ORDER BY addedDate desc")
    val allNewsFeed: LiveData<List<Blog?>?>?

    @Query("DELETE FROM Blog")
    fun deleteAll()
}