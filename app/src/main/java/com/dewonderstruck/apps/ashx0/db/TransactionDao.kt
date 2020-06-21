package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transactionObject: TransactionObject?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(transactionObject: TransactionObject?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTransactionList(transactionObjectList: List<TransactionObject?>?)

    @Query("DELETE FROM TransactionObject")
    fun deleteAllTransactionList()

    @get:Query("SELECT * FROM TransactionObject order by addedDate desc")
    val allTransactionList: LiveData<List<TransactionObject?>?>?

    @Query("SELECT * FROM TransactionObject order by addedDate desc limit:loadFromDB")
    fun getAllTransactionListByLimit(loadFromDB: String?): LiveData<List<TransactionObject?>?>?

    @Query("DELETE FROM TransactionObject WHERE id = :id")
    fun deleteTransactionById(id: String?)

    @Query("SELECT * FROM TransactionObject WHERE id = :id")
    fun getTransactionById(id: String?): LiveData<TransactionObject?>?
}