package com.dewonderstruck.apps.ashx0.repository.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.TransactionDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.TransactionHeaderUpload
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository //end region
//region constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region variable
                             private val transactionDao: TransactionDao) : PSRepository(apiService!!, appExecutors!!, db!!) {

    //Get transaction list
    fun getTransactionList(apiKey: String?, userId: String?, loadFromDB: String, offset: String?): LiveData<Resource<List<TransactionObject?>?>> {
        return object : NetworkBoundResource<List<TransactionObject?>?, List<TransactionObject?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<TransactionObject?>) {
                Utils.psLog("SaveCallResult of recent transaction.")
                db.beginTransaction()
                try {
                    transactionDao.deleteAllTransactionList()
                    transactionDao.insertAllTransactionList(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of recent transaction list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<TransactionObject?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<TransactionObject?>> {
                Utils.psLog("Load Recent transaction From Db")
                return if (loadFromDB == "") {
                    transactionDao.allTransactionList
                } else {
                    transactionDao.getAllTransactionListByLimit(loadFromDB)
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<TransactionObject?>?>?> {
                return apiService.getTransList(apiKey, Utils.checkUserId(userId), java.lang.String.valueOf(Config.TRANSACTION_COUNT), offset)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getRecentTransactionList) : $message")
            }
        }.asLiveData()
    }

    // Get next page transaction list
    fun getNextPageTransactionList(userId: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getTransList(Config.API_KEY, Utils.checkUserId(userId), java.lang.String.valueOf(Config.TRANSACTION_COUNT), offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<TransactionObject?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            db.transactionDao()!!.insertAllTransactionList(response.body)
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
    }

    fun uploadTransDetailToServer(transactionHeaderUpload: TransactionHeaderUpload?): LiveData<Resource<TransactionObject?>> {
        val statusLiveData = MutableLiveData<Resource<TransactionObject?>>()
        appExecutors.networkIO().execute {
            val response: Response<TransactionObject>
            try {
                response = apiService.uploadTransactionHeader(transactionHeaderUpload, Config.API_KEY)!!.execute()
                val apiResponse = ApiResponse(response)
                if (apiResponse.isSuccessful) {
                    statusLiveData.postValue(success(apiResponse.body))
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }

    //Get transaction detail
    fun getTransactionDetail(apiKey: String?, userId: String?, id: String?): LiveData<Resource<TransactionObject?>> {
        return object : NetworkBoundResource<TransactionObject?, TransactionObject?>(appExecutors) {
            protected override fun saveCallResult(itemList: TransactionObject) {
                Utils.psLog("SaveCallResult of recent transaction.")
                db.beginTransaction()
                try {
                    transactionDao.deleteTransactionById(id)
                    transactionDao.insert(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: TransactionObject?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<TransactionObject> {
                Utils.psLog("Load discount From Db")
                return transactionDao.getTransactionById(id)
            }

            override fun createCall(): LiveData<ApiResponse<TransactionObject?>?> {
                Utils.psLog("Call API Service to get discount.")
                return apiService.getTransactionDetail(apiKey, userId, id)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getDiscount) : $message")
            }
        }.asLiveData()
    }

}