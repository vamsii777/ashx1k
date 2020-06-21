package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct

@Dao
abstract class HistoryDao {
    //region history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(basket: HistoryProduct?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(basket: HistoryProduct?)

    @Query("DELETE FROM HistoryProduct")
    abstract fun deleteHistoryProduct()

    @Query("SELECT * FROM (SELECT * FROM HistoryProduct ORDER BY historyDate DESC) LIMIT :limit Offset 0")
    abstract fun getAllHistoryProductListData(limit: String?): LiveData<List<HistoryProduct?>?>?

    @Query("DELETE FROM HistoryProduct WHERE id =:id")
    abstract fun deleteHistoryProductById(id: String?) //endregion
}