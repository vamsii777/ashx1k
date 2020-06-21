package com.dewonderstruck.apps.ashx0.repository.shop

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.ShopDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Shop
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class ShopRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, shopDao: ShopDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    private val shopDao: ShopDao

    //endregion
    //region Constructor
    @JvmField
    @Inject
    var pref: SharedPreferences? = null
    fun getShop(api_key: String?): LiveData<Resource<Shop?>> {
        return object : NetworkBoundResource<Shop?, Shop?>(appExecutors) {
            protected override fun saveCallResult(itemList: Shop) {
                Utils.psLog("SaveCallResult of recent products.")
                db.beginTransaction()
                try {
                    db.shopDao()!!.insert(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: Shop?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<Shop> {
                Utils.psLog("Load discount From Db")
                return shopDao.shopById
            }

            override fun createCall(): LiveData<ApiResponse<Shop?>?> {
                Utils.psLog("Call API Service to get discount.")
                return apiService.getShopById(api_key)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getDiscount) : $message")
            }
        }.asLiveData()
    }

    init {
        Utils.psLog("Inside ShopRepository")
        this.shopDao = shopDao
    }
}