package com.dewonderstruck.apps.ashx0.repository.common

import androidx.lifecycle.LiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Connectivity
import com.dewonderstruck.apps.ashx0.utils.RateLimiter
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Parent Class of All Repository Class in this project
 * Created by Vamsi Madduluri on 12/5/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
abstract class PSRepository protected constructor(apiService: ApiService, appExecutors: AppExecutors, db: PSCoreDb) {
    //region Variables
    @JvmField
    protected val apiService: ApiService
    @JvmField
    protected val appExecutors: AppExecutors
    @JvmField
    protected val db: PSCoreDb

    @JvmField
    @Inject
    var connectivity: Connectivity? = null
    @JvmField
    protected var rateLimiter = RateLimiter<String>(Config.API_SERVICE_CACHE_LIMIT, TimeUnit.MINUTES)

    //endregion
    //region public Methods
    fun save(obj: Any?): LiveData<Resource<Boolean>> {
        if (obj == null) {
            return AbsentLiveData.create()
        }
        val saveTask = SaveTask(apiService, db, obj)
        appExecutors.diskIO().execute(saveTask)
        return saveTask.statusLiveData
    }

    fun delete(obj: Any?): LiveData<Resource<Boolean>> {
        if (obj == null) {
            return AbsentLiveData.create()
        }
        val deleteTask = DeleteTask(apiService, db, obj)
        appExecutors.diskIO().execute(deleteTask)
        return deleteTask.statusLiveData
    } //endregion
    //endregion
    //region Constructor
    /**
     * Constructor of PSRepository
     * @param apiService Vamsi Madduluri API Service Instance
     * @param appExecutors Executors Instance
     * @param db Vamsi Madduluri DB
     */
    init {
        Utils.psLog("Inside NewsRepository")
        this.apiService = apiService
        this.appExecutors = appExecutors
        this.db = db
    }
}