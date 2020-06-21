package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.TransactionDetail

@Dao
interface TransactionOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transactionDetail: TransactionDetail?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(transactionDetail: TransactionDetail?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTransactionOrderList(transactionDetailList: List<TransactionDetail?>?)

    @Query("DELETE FROM TransactionDetail WHERE transactionsHeaderId = :transactionsHeaderId")
    fun deleteAllTransactionOrderList(transactionsHeaderId: String?)

    @Query("SELECT * FROM TransactionDetail WHERE transactionsHeaderId = :transactionsHeaderId")
    fun getAllTransactionOrderList(transactionsHeaderId: String?): LiveData<List<TransactionDetail?>?>?

    @Query("DELETE FROM TransactionDetail")
    fun deleteAll()
}