package com.dewonderstruck.apps.ashx0.repository.common;

import com.dewonderstruck.apps.ashx0.api.ApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.viewobject.Category;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * General Save Task Sample
 * Created by Vamsi Madduluri on 12/6/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

public class SaveTask implements Runnable {


    //region Variables

    private final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

    public final ApiService service;
    private final PSCoreDb db;
    private final Object obj;

    //endregion


    //region Constructor

    /**
     * Constructor of SaveTask.
     * @param service Vamsi Madduluri API Service Instance
     * @param db Vamsi Madduluri DB Instance
     * @param obj Object to Save
     *
     */
    SaveTask(ApiService service, PSCoreDb db, Object obj) {
        this.service = service;
        this.db = db;
        this.obj = obj;
    }

    //endregion


    //region Override Methods

    @Override
    public void run() {
        try {
            try{
                db.beginTransaction();

                if(obj instanceof Category) {
                    //db.categoryDao().insert((Category) obj);
                    db.setTransactionSuccessful();
                }

            }finally {
                db.endTransaction();
            }
            statusLiveData.postValue(Resource.success(true));
        } catch (Exception e) {
            statusLiveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    //endregion


    //region public SyncCategory Methods

    /**
     * This function will return Status of Process
     * @return statusLiveData
     */
    public LiveData<Resource<Boolean>> getStatusLiveData() { return statusLiveData; }

    //endregion

}
