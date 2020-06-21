package com.dewonderstruck.apps.ashx0.repository.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.NotificationDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Noti
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository //end region
//region constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region variable
                             private val notificationDao: NotificationDao) : PSRepository(apiService!!, appExecutors!!, db!!) {

    //end region
    //Get notification list
    fun getNotificationList(apiKey: String?, userId: String?, limit: String?, offset: String?, deviceToken: String?): LiveData<Resource<List<Noti?>?>> {
        return object : NetworkBoundResource<List<Noti?>?, List<Noti?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<Noti?>) {
                Utils.psLog("SaveCallResult of getNotificationList.")
                db.beginTransaction()
                try {
                    notificationDao.deleteAllNotificationList()
                    notificationDao.insertAllNotificationList(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of recent notification list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Noti?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Noti?>> {
                Utils.psLog("Load Recent notification From Db")
                return notificationDao.allNotificationList
            }

            override fun createCall(): LiveData<ApiResponse<List<Noti?>?>?> {
                return apiService.getNotificationList(apiKey,
                        limit,
                        offset,
                        userId,
                        deviceToken)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getRecentNotificationList) : $message")
            }
        }.asLiveData()
    }

    fun getNextPageNotificationList(userId: String?, deviceToken: String?, limit: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getNotificationList(Config.API_KEY, limit, offset, userId, deviceToken)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Noti?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            db.notificationDao()!!.insertAllNotificationList(response.body)
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

    //Get Product detail
    fun getNotificationDetail(apiKey: String?, notificationId: String?): LiveData<Resource<Noti?>> {
        return object : NetworkBoundResource<Noti?, Noti?>(appExecutors) {
            protected override fun saveCallResult(itemList: Noti) {
                Utils.psLog("SaveCallResult of recent products.")
                db.beginTransaction()
                try {
                    notificationDao.deleteNotificationById(notificationId)
                    notificationDao.insert(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: Noti?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<Noti> {
                Utils.psLog("Load discount From Db")
                return notificationDao.getNotificationById(notificationId)
            }

            override fun createCall(): LiveData<ApiResponse<Noti?>?> {
                Utils.psLog("Call API Service to get discount.")
                return apiService.getNotificationDetail(apiKey, notificationId)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getDiscount) : $message")
            }
        }.asLiveData()
    }

    //noti read post
    fun uploadNotiPostToServer(noti_id: String?, userId: String?, device_token: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.networkIO().execute {
            try {
                // Call the API Service
                val response: Response<Noti?>
                response = apiService
                        .isReadNoti(Config.API_KEY, noti_id, userId, device_token)!!.execute()

                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            db.notificationDao()!!.insert(response.body())
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(success(apiResponse.nextPage != null))
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, false))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, false))
            }
        }
        return statusLiveData
    } //endregion

}