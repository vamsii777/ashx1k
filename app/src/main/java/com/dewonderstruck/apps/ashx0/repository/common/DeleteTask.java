package com.dewonderstruck.apps.ashx0.repository.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dewonderstruck.apps.ashx0.api.ApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.viewobject.User;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;


/**
 * General Delete Task Sample
 * Created by Vamsi Madduluri on 12/14/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
public class DeleteTask implements Runnable {


    //region Variables

    private final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

    public final ApiService service;
    private final PSCoreDb db;
    private final Object obj;

    //endregion


    //region Constructor

    DeleteTask(ApiService service, PSCoreDb db, Object obj) {
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

                if(obj instanceof User) {
                    db.userDao().deleteUserLogin();

                    db.productDao().deleteAllFavouriteProducts();

                    db.transactionDao().deleteAllTransactionList();

                    db.transactionOrderDao().deleteAll();

                    db.basketDao().deleteAllBasket();

                    db.historyDao().deleteHistoryProduct();

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