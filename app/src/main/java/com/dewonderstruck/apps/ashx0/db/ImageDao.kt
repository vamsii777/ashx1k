package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Image

/**
 * Created by Vamsi Madduluri on 12/8/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: Image?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(imageList: List<Image?>?)

    @get:Query("SELECT * FROM Image")
    val all: LiveData<List<Image?>?>?

    @Query("SELECT * FROM Image WHERE imgParentId = :imgParentId AND imgType= :imagetype order by imgId")
    fun getByImageIdAndType(imgParentId: String?, imagetype: String?): LiveData<List<Image?>?>?

    @Query("DELETE FROM Image WHERE imgParentId = :imgParentId AND imgType= :imagetype")
    fun deleteByImageIdAndType(imgParentId: String?, imagetype: String?)

    @Query("DELETE FROM Image")
    fun deleteTable()
}