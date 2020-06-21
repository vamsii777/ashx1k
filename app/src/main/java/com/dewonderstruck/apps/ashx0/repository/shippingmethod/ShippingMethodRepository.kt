package com.dewonderstruck.apps.ashx0.repository.shippingmethod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.ShippingMethodDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.ShippingCost
import com.dewonderstruck.apps.ashx0.viewobject.ShippingCostContainer
import com.dewonderstruck.apps.ashx0.viewobject.ShippingMethod
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ShippingMethodRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, shippingMethodDao: ShippingMethodDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    private val shippingMethodDao: ShippingMethodDao

    // Recent news always load from server
    val allShippingMethods: LiveData<Resource<List<ShippingMethod?>?>>
        get() = object : NetworkBoundResource<List<ShippingMethod?>?, List<ShippingMethod?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<ShippingMethod?>) {
                Utils.psLog("SaveCallResult of getAllShippingMethods.")
                db.beginTransaction()
                try {
                    db.shippingMethodDao()!!.deleteAll()
                    db.shippingMethodDao()!!.insertAll(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getAllShippingMethods.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<ShippingMethod?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<ShippingMethod?>> {
                Utils.psLog("Load getAllShippingMethods From Db")
                return shippingMethodDao.shippingMethods
            }

            override fun createCall(): LiveData<ApiResponse<List<ShippingMethod?>?>?> {
                Utils.psLog("Call API Service to getAllShippingMethods.")
                return apiService.getShipping(Config.API_KEY)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getAllShippingMethods) : $message")
            }
        }.asLiveData()

    fun postShippingByCountryAndCity(shippingCostContainer: ShippingCostContainer?): LiveData<Resource<ShippingCost?>> {
        val statusLiveData = MutableLiveData<Resource<ShippingCost?>>()
        appExecutors.networkIO().execute {
            val response: Response<ShippingCost>
            try {
                response = apiService.postShippingByCountryAndCity(Config.API_KEY, shippingCostContainer)!!.execute()
                val apiResponse = ApiResponse(response)
                if (apiResponse.isSuccessful) {
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

    /**
     * Constructor of PSRepository
     *
     * @param apiService Vamsi Madduluri API Service Instance
     * @param appExecutors Executors Instance
     * @param db           Vamsi Madduluri DB
     */
    init {
        Utils.psLog("Inside ProductRepository")
        this.shippingMethodDao = shippingMethodDao
    }
}