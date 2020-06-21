package com.dewonderstruck.apps.ashx0.repository.apploading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.PSAppInfo
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AppLoadingRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?) : PSRepository(apiService!!, appExecutors!!, db!!) {
    fun deleteTheSpecificObjects(startDate: String?, endDate: String?): LiveData<Resource<PSAppInfo?>> {
        val statusLiveData = MutableLiveData<Resource<PSAppInfo?>>()
        appExecutors.networkIO().execute {
            val response: Response<PSAppInfo?>
            try {
                response = apiService.getDeletedHistory(Config.API_KEY, startDate, endDate)!!.execute()
                val apiResponse = ApiResponse(response)
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            if (apiResponse.body.deletedObjects!!.size > 0) {
                                for (deletedObject in apiResponse.body.deletedObjects!!) {
                                    when (deletedObject.typeName) {
                                        Constants.APPINFO_NAME_SHOP -> db.shopDao()!!.deleteShopById(deletedObject.id)
                                        Constants.APPINFO_NAME_PRODUCT -> {
                                            db.productDao()!!.deleteProductById(deletedObject.id)
                                            db.historyDao()!!.deleteHistoryProductById(deletedObject.id)
                                        }
                                        Constants.APPINFO_NAME_CATEGORY -> db.categoryDao()!!.deleteCategoryById(deletedObject.id)
                                    }
                                }
                            }
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
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
}