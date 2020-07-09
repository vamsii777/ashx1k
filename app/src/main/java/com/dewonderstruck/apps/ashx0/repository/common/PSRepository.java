package com.dewonderstruck.apps.ashx0.repository.common;

import androidx.lifecycle.LiveData;
import com.dewonderstruck.apps.AppExecutors;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.PSApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.utils.Connectivity;
import com.dewonderstruck.apps.ashx0.utils.RateLimiter;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Parent Class of All Repository Class in this project
 * Created by Vamsi Madduluri on 12/5/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

public abstract class PSRepository {


    //region Variables

    protected final PSApiService psApiService;
    protected final AppExecutors appExecutors;
    protected final PSCoreDb db;
    @Inject
    protected Connectivity connectivity;
    protected RateLimiter<String> rateLimiter = new RateLimiter<>( Config.API_SERVICE_CACHE_LIMIT, TimeUnit.MINUTES);

    //endregion


    //region Constructor

    /**
     * Constructor of PSRepository
     * @param psApiService Vamsi Madduluri API Service Instance
     * @param appExecutors Executors Instance
     * @param db Vamsi Madduluri DB
     */
    protected PSRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db) {
        Utils.psLog("Inside NewsRepository");
        this.psApiService = psApiService;
        this.appExecutors = appExecutors;
        this.db = db;
    }

    //endregion


    //region public Methods

    public LiveData<Resource<Boolean>> save(Object obj) {

        if(obj == null) {
            return AbsentLiveData.create();
        }

        SaveTask saveTask = new SaveTask(psApiService, db, obj);
        appExecutors.diskIO().execute(saveTask);
        return saveTask.getStatusLiveData();
    }


    public LiveData<Resource<Boolean>> delete(Object obj) {

        if(obj == null) {
            return AbsentLiveData.create();
        }

        DeleteTask deleteTask = new DeleteTask(psApiService, db, obj);
        appExecutors.diskIO().execute(deleteTask);
        return deleteTask.getStatusLiveData();
    }
    //endregion

}
