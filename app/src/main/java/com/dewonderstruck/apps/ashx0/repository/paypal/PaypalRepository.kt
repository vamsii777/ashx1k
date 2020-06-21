package com.dewonderstruck.apps.ashx0.repository.paypal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.successWithMsg
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PaypalRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?) : PSRepository(apiService!!, appExecutors!!, db!!) {
    val paypalToekn: LiveData<Resource<Boolean?>>
        get() {
            val statusLiveData = MutableLiveData<Resource<Boolean?>>()
            appExecutors.networkIO().execute {
                val response: Response<ApiStatus?>
                try {
                    response = apiService.getPaypalToken(Config.API_KEY)!!.execute()
                    val apiResponse = ApiResponse(response)
                    if (apiResponse.isSuccessful) {
                        if (apiResponse.body != null) {
                            statusLiveData.postValue(successWithMsg(apiResponse.body.message, true))
                        } else {
                            statusLiveData.postValue(error(apiResponse.errorMessage, false))
                        }
                    } else {
                        statusLiveData.postValue(error(apiResponse.errorMessage, false))
                    }
                } catch (e: IOException) {
                    statusLiveData.postValue(error(e.message, false))
                }
            }
            return statusLiveData
        }

    init {
        Utils.psLog("Inside PaypalRepository")
    }
}