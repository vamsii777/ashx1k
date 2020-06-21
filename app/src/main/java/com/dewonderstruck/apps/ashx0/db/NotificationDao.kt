package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.Noti

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noti: Noti?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(noti: Noti?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNotificationList(NotiList: List<Noti?>?)

    @Query("DELETE FROM Noti")
    fun deleteAllNotificationList()

    @get:Query("SELECT * FROM Noti order by addedDate desc")
    val allNotificationList: LiveData<List<Noti?>?>?

    @Query("DELETE FROM Noti WHERE id = :id")
    fun deleteNotificationById(id: String?)

    @Query("SELECT * FROM Noti WHERE id = :id")
    fun getNotificationById(id: String?): LiveData<Noti?>?
}