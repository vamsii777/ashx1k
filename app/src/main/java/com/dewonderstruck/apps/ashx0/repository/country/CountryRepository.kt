package com.dewonderstruck.apps.ashx0.repository.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.CountryDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Country
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import javax.inject.Inject

class CountryRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, countryDao: CountryDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    //region Variables
    private val countryDao: CountryDao

    //endregion
    //region Category Repository Functions for ViewModel
    fun getCountryListWithShopId(apiKey: String?, shopId: String?, limit: String?, offset: String?): LiveData<Resource<List<Country?>?>> {
        return object : NetworkBoundResource<List<Country?>?, List<Country?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<Country?>) {
                Utils.psLog("SaveCallResult of getAllSubCategoryList.")
                try {
                    db.beginTransaction()
                    countryDao.deleteCountry()
                    countryDao.insertAll(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getAllSubCategoryList.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Country?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Country?>> {
                return countryDao.allCountry
            }

            override fun createCall(): LiveData<ApiResponse<List<Country?>?>?> {
                return apiService.getCountryListWithShopId(apiKey, shopId, limit, offset)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of $message")
            }
        }.asLiveData()
    }

    fun getNextPageCityListWithShopId(apiKey: String?, shopId: String?, limit: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getCountryListWithShopId(Config.API_KEY, shopId, limit, offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Country?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            for (news in response.body) {
                                db.countryDao()!!.insert(Country(news!!.id, news!!.name, news!!.status, news!!.addedDate, news!!.addedUserId, news!!.updatedDate, news!!.updatedUserId, news!!.updatedFlag, news!!.addedDateStr))
                            }
                            db.countryDao()!!.insertAll(response.body)
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(success(true))
                }
            } else {
                statusLiveData.postValue(error(response.errorMessage, null))
            }
        }
        return statusLiveData
    } //endregion

    //endregion
    //region Constructor
    init {
        Utils.psLog("Inside CategoryRepository")
        this.countryDao = countryDao
    }
}