package com.dewonderstruck.apps.ashx0.repository.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success

/**
 * General Delete Task Sample
 * Created by Vamsi Madduluri on 12/14/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class DeleteTask //endregion
//region Constructor
internal constructor(val service: ApiService, private val db: PSCoreDb, private val obj: Any) : Runnable {
    //region Variables
    private val statusLiveData = MutableLiveData<Resource<Boolean?>>()

    //endregion
    //region Override Methods
    override fun run() {
        try {
            try {
                db.beginTransaction()
                if (obj is User) {
                    db.userDao()!!.deleteUserLogin()
                    db.productDao()!!.deleteAllFavouriteProducts()
                    db.transactionDao()!!.deleteAllTransactionList()
                    db.transactionOrderDao()!!.deleteAll()
                    db.basketDao()!!.deleteAllBasket()
                    db.historyDao()!!.deleteHistoryProduct()
                    db.setTransactionSuccessful()
                }
            } finally {
                db.endTransaction()
            }
            statusLiveData.postValue(success(true))
        } catch (e: Exception) {
            statusLiveData.postValue(error(e.message, true))
        }
    }
    //endregion
    //region public SyncCategory Methods
    /**
     * This function will return Status of Process
     * @return statusLiveData
     */
    fun getStatusLiveData(): LiveData<Resource<Boolean?>> {
        return statusLiveData
    } //endregion

}