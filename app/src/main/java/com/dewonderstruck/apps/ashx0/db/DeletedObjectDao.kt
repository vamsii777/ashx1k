package com.dewonderstruck.apps.ashx0.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.DeletedObject

@Dao
interface DeletedObjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deletedObject: DeletedObject?)

    @Query("DELETE FROM DeletedObject")
    fun deleteAll()
}