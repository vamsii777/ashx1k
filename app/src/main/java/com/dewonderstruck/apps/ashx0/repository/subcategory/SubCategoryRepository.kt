package com.dewonderstruck.apps.ashx0.repository.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.SubCategoryDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, subCategoryDao: SubCategoryDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    private val subCategoryDao: SubCategoryDao
    fun getAllSubCategoryList(apiKey: String?): LiveData<Resource<List<SubCategory?>?>> {
        return object : NetworkBoundResource<List<SubCategory?>?, List<SubCategory?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<SubCategory?>) {
                Utils.psLog("SaveCallResult of getAllSubCategoryList.")
                try {
                    db.beginTransaction()
                    subCategoryDao.deleteAllSubCategory()
                    subCategoryDao.insertAll(itemList)
                    for (item in itemList) {
                        subCategoryDao.insert(SubCategory(item!!.id, item.catId, item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getAllSubCategoryList.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<SubCategory?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<SubCategory?>> {
                return subCategoryDao.allSubCategory
            }

            override fun createCall(): LiveData<ApiResponse<List<SubCategory?>?>?> {
                return apiService.getAllSubCategoryList(apiKey)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of $message")
            }
        }.asLiveData()
    }

    fun getSubCategoriesWithCatId(loginUserId: String?, offset: String?, catId: String?): LiveData<Resource<List<SubCategory?>?>> {
        return object : NetworkBoundResource<List<SubCategory?>?, List<SubCategory?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<SubCategory?>) {
                Utils.psLog("SaveCallResult of Sub Category.")
                try {
                    db.beginTransaction()
                    subCategoryDao.insertAll(itemList)
                    for (item in itemList) {
                        subCategoryDao.insert(SubCategory(item!!.id, item.catId, item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of recent product list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<SubCategory?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<SubCategory?>> {
                return subCategoryDao.getSubCategoryList(catId)
            }

            override fun createCall(): LiveData<ApiResponse<List<SubCategory?>?>?> {
                return apiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, "", offset)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of $message")
            }
        }.asLiveData()
    }

    fun getNextPageSubCategoriesWithCatId(loginUserId: String?, limit: String?, offset: String?, catId: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, limit, offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<SubCategory?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            for (news in response.body) {
                                db.subCategoryDao()!!.insert(SubCategory(news!!.id, news!!.catId, news!!.name, news!!.status, news!!.ordering, news!!.addedDate, news!!.addedUserId, news!!.updatedDate, news!!.updatedUserId, news!!.updatedFlag, news!!.addedDateStr, news!!.defaultPhoto, news!!.defaultIcon))
                            }
                            db.subCategoryDao()!!.insertAll(response.body)
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

    init {
        Utils.psLog("Inside SubCategoryRepository")
        this.subCategoryDao = subCategoryDao
    }
}