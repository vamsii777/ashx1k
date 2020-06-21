package com.dewonderstruck.apps.ashx0.repository.coupondiscount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.viewobject.CouponDiscount
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class CouponDiscountRepository
/**
 * Constructor of PSRepository
 *
 * @param apiService Vamsi Madduluri API Service Instance
 * @param appExecutors Executors Instance
 * @param db           Vamsi Madduluri DB
 */
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?) : PSRepository(apiService!!, appExecutors!!, db!!) {
    fun getCouponDiscount(code: String?): LiveData<Resource<CouponDiscount?>> {
        val statusLiveData = MutableLiveData<Resource<CouponDiscount?>>()
        appExecutors.networkIO().execute {
            try {
                // Call the API Service
                val response: Response<CouponDiscount>
                response = apiService.checkCouponDiscount(Config.API_KEY, code)!!.execute()

                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    statusLiveData.postValue(success(apiResponse.body))
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage.toString(), null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }
}