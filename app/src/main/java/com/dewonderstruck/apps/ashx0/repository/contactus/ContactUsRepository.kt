package com.dewonderstruck.apps.ashx0.repository.contactus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 7/2/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class ContactUsRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?) : PSRepository(apiService!!, appExecutors!!, db!!) {
    /**
     * Post Contact Us
     * @param apiKey APIKey to access Web Service
     * @param contactName Name
     * @param contactEmail Email
     * @param contactDesc Desc
     * @return Status of Post
     */
    fun postContactUs(apiKey: String?, contactName: String?, contactEmail: String?, contactDesc: String?, contactPhone: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response: Response<ApiStatus> = apiService.rawPostContact(apiKey, contactName, contactEmail, contactDesc, contactPhone)!!.execute()

                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)
                Utils.psLog("apiResponse $apiResponse")
                // If response is successful
                if (apiResponse.isSuccessful) {
                    statusLiveData.postValue(success(true))
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, true))
                }
            } catch (e: Exception) {
                statusLiveData.postValue(error(e.message, true))
            }
        }
        return statusLiveData
    }
}