package com.dewonderstruck.apps.ashx0.repository.aboutus

import android.content.Context
import androidx.lifecycle.LiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.AboutUsDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.NotificationTask
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 12/8/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
class AboutUsRepository //endregion
//region Constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region Variables
                             private val aboutUsDao: AboutUsDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    //endregion
    //region About Us Repository Functions for ViewModel
    /**
     * Load About Us
     */
    fun getAboutUs(apiKey: String?): LiveData<Resource<AboutUs?>> {
        val functionKey = "getAboutUs"
        return object : NetworkBoundResource<AboutUs?, List<AboutUs?>?>(appExecutors) {
            override fun saveCallResult(item: List<AboutUs?>?) {
                db.beginTransaction()
                try {
                    aboutUsDao.deleteTable()
                    aboutUsDao.insertAll(item)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in inserting about us.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: AboutUs?): Boolean {
                // API level cache
                //if (connectivity.isConnected())
                //if (data == null || rateLimiter.shouldFetch(functionKey)) return true;
                //return false;
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<AboutUs?> {
                Utils.psLog("Load AboutUs From DB.")
                return aboutUsDao.get()!!
            }

            override fun createCall(): LiveData<ApiResponse<List<AboutUs?>?>?> {
                Utils.psLog("Call About us webservice.")
                return apiService.getAboutUs(apiKey)!!
            }

            override fun onFetchFailed(responseErrorMessage: String?) {
                Utils.psLog("Fetch Failed of About Us")
                rateLimiter.reset(functionKey)
            }
        }.asLiveData()
    }
    //endregion
    /**
     * Function to call background task to register Notification
     *
     * @param platform Current Platform
     * @return Status of Process
     */
    fun registerNotification(context: Context?, platform: String?, token: String?): LiveData<Resource<Boolean>> {
        val notificationTask = NotificationTask(context,
                apiService, platform, true, token)
        Utils.psLog("Register Notification : News repository.")
        appExecutors.networkIO().execute(notificationTask)
        return notificationTask.statusLiveData
    }

    /**
     * Function to call background task to un-register notification.
     *
     * @param platform Current Platform
     * @return Status of Process
     */
    fun unregisterNotification(context: Context?, platform: String?, token: String?): LiveData<Resource<Boolean>> {
        val notificationTask = NotificationTask(context,
                apiService, platform, false, token)
        Utils.psLog("Unregister Notification : News repository.")
        appExecutors.networkIO().execute(notificationTask)
        return notificationTask.statusLiveData
    }

}