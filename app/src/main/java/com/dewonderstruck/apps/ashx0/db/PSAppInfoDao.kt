package com.dewonderstruck.apps.ashx0.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.PSAppInfo

@Dao
interface PSAppInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(PSAppInfo: PSAppInfo?)

    @Query("DELETE FROM PSAppInfo")
    fun deleteAll()
}