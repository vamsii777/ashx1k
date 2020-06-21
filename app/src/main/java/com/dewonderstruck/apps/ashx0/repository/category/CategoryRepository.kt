package com.dewonderstruck.apps.ashx0.repository.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.CategoryDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.CategoryMap
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import com.dewonderstruck.apps.ashx0.viewobject.holder.CategoryParameterHolder
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 12/02/2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
class CategoryRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, categoryDao: CategoryDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    //region Variables
    private val categoryDao: CategoryDao
    //endregion
    //region Category Repository Functions for ViewModel
    /**
     * Load Category List
     */
    fun getAllSearchCategory(categoryParameterHolder: CategoryParameterHolder, limit: String, offset: String?): LiveData<Resource<List<Category?>?>> {
        return object : NetworkBoundResource<List<Category?>?, List<Category?>?>(appExecutors) {
            override fun saveCallResult(item: List<Category?>?) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId")
                try {
                    db.beginTransaction()
                    val mapKey = categoryParameterHolder.changeToMapValue()
                    db.categoryMapDao()!!.deleteByMapKey(mapKey)
                    categoryDao.insertAll(item)
                    val dateTime = Utils.getDateTime()
                    for (i in item!!.indices) {
                        db.categoryMapDao()!!.insert(CategoryMap(mapKey + item!![i]!!.id, mapKey, item[i]!!.id, i + 1, dateTime))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getAllCategoriesWithUserId", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Category?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Category?>?> {
                Utils.psLog("Load From Db (All Categories)")
                val mapKey = categoryParameterHolder.changeToMapValue()
                return if (limit != java.lang.String.valueOf(Config.LOAD_FROM_DB)) {
                    categoryDao.getCategoryByKeyTest(mapKey)!!
                } else {
                    categoryDao.getCategoryByKeyTestByLimit(mapKey, limit)!!
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<Category?>?>?>? {
                Utils.psLog("Call Get All Categories webservice.")
                return apiService.getSearchCategory(Config.API_KEY, limit, offset, categoryParameterHolder.order_by)
            }

            override fun onFetchFailed(responseErrorMessage: String?) {
                Utils.psLog("Fetch Failed of About Us")
            }
        }.asLiveData()
    }

    fun getNextSearchCategory(limit: String?, offset: String?, categoryParameterHolder: CategoryParameterHolder): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getSearchCategory(Config.API_KEY, limit, offset, categoryParameterHolder.order_by)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Category?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            db.categoryMapDao()!!.insertAll(response.body)
                            val finalIndex = db.categoryMapDao()!!.getMaxSortingByValue(categoryParameterHolder.changeToMapValue())
                            val startIndex = finalIndex + 1
                            val mapKey = categoryParameterHolder.changeToMapValue()
                            val dateTime = Utils.getDateTime()
                            for (i in response.body.indices) {
                                db.categoryMapDao()!!.insert(CategoryMap(mapKey + response.body.get(i)!!.id, mapKey, response.body.get(i)!!.id, startIndex + i, dateTime))
                            }

                            //db.trendingCategoryDao().insertAll(new TrendingCategory(apiResponse.body.));
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
    }

    //endregion
    //region Constructor
    init {
        Utils.psLog("Inside CategoryRepository")
        this.categoryDao = categoryDao
    }
}