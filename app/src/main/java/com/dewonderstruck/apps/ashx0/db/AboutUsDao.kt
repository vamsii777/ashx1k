package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs

/**
 * Created by Vamsi Madduluri on 12/30/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Dao
interface AboutUsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aboutUs: AboutUs?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(aboutUsList: List<AboutUs?>?)

    @Query("SELECT * FROM AboutUs LIMIT '1'")
    fun get(): LiveData<AboutUs?>?

    @get:Query("SELECT * FROM AboutUs")
    val all: LiveData<List<AboutUs?>?>?

    @Query("DELETE FROM AboutUs")
    fun deleteTable()
}