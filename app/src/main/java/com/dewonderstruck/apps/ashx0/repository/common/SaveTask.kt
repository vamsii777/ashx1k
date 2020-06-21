package com.dewonderstruck.apps.ashx0.repository.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success

/**
 * General Save Task Sample
 * Created by Vamsi Madduluri on 12/6/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class SaveTask
/**
 * Constructor of SaveTask.
 * @param service Vamsi Madduluri API Service Instance
 * @param db Vamsi Madduluri DB Instance
 * @param obj Object to Save
 */ internal constructor(val service: ApiService, private val db: PSCoreDb, private val obj: Any) : Runnable {
    //region Variables
    private val statusLiveData = MutableLiveData<Resource<Boolean?>>()

    //endregion
    //region Override Methods
    override fun run() {
        try {
            try {
                db.beginTransaction()
                if (obj is Category) {
                    //db.categoryDao().insert((Category) obj);
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
    //endregion
    //region Constructor
}