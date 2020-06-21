package com.dewonderstruck.apps.ashx0.repository.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.TransactionOrderDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.TransactionDetail
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionOrderRepository //end region
//region constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region variable
                             private val transactionOrderDao: TransactionOrderDao) : PSRepository(apiService!!, appExecutors!!, db!!) {

    //end constructor
    //region start get transaction order list
    fun getTransactionOrderList(apiKey: String?, transactionHeaderId: String?, offset: String?): LiveData<Resource<List<TransactionDetail?>?>> {
        return object : NetworkBoundResource<List<TransactionDetail?>?, List<TransactionDetail?>?>(appExecutors) {
            protected override fun saveCallResult(item: List<TransactionDetail?>) {
                Utils.psLog("SaveCallResult of recent transaction order.")
                db.beginTransaction()
                try {
                    transactionOrderDao.deleteAllTransactionOrderList(transactionHeaderId)
                    transactionOrderDao.insertAllTransactionOrderList(item)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction order of recent transaction order list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<TransactionDetail?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<TransactionDetail?>> {
                Utils.psLog("Load Recent transaction From Db")
                return transactionOrderDao.getAllTransactionOrderList(transactionHeaderId)
            }

            override fun createCall(): LiveData<ApiResponse<List<TransactionDetail?>?>?> {
                return apiService.getTransactionOrderList(apiKey, transactionHeaderId, java.lang.String.valueOf(Config.TRANSACTION_ORDER_COUNT), offset)!!
            }
        }.asLiveData()
    }

    //region end of transaction order list
    //region start next page transaction order list
    fun getNextPageTransactionOrderList(transactionHeaderId: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getTransactionOrderList(Config.API_KEY, transactionHeaderId, java.lang.String.valueOf(Config.TRANSACTION_ORDER_COUNT), offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<TransactionDetail?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            db.transactionOrderDao()!!.insertAllTransactionOrderList(response.body)
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(success(true))
                }
            } else {
                statusLiveData.postValue(error(response.errorMessage, false))
            }
        }
        return statusLiveData
    } //region end of next page transaction order list

}