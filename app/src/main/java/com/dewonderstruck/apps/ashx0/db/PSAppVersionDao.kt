package com.dewonderstruck.apps.ashx0.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.PSAppVersion

@Dao
interface PSAppVersionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(psAppVersion: PSAppVersion?)

    @Query("DELETE FROM PSAppVersion")
    fun deleteAll()
}