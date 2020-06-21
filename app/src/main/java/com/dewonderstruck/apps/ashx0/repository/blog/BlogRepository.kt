package com.dewonderstruck.apps.ashx0.repository.blog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.BlogDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Blog
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, private val blogDao: BlogDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    fun getNewsFeedList(limit: String?, offset: String?): LiveData<Resource<List<Blog?>?>> {
        return object : NetworkBoundResource<List<Blog?>?, List<Blog?>?>(appExecutors) {
            override fun saveCallResult(itemList: List<Blog?>) {
                Utils.psLog("SaveCallResult of getNewsFeedList.")
                db.beginTransaction()
                try {
                    blogDao.deleteAll()
                    blogDao.insertAll(itemList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getNewsFeedList.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Blog?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Blog?>?> {
                Utils.psLog("Load getNewsFeedList From Db")
                return blogDao.allNewsFeed!!
            }

            override fun createCall(): LiveData<ApiResponse<List<Blog?>?>?> {
                Utils.psLog("Call API Service to getNewsFeedList.")
                return apiService.getAllNewsFeed(Config.API_KEY, limit, offset)!!
            }

            override fun onFetchFailed(message: String) {
                Utils.psLog("Fetch Failed (getNewsFeedList) : $message")
            }
        }.asLiveData()
    }

    //    public LiveData<Resource<List<Blog>>> getNewsFeedListByShopId( String limit, String offset) {
    //        return new NetworkBoundResource<List<Blog>, List<Blog>>(appExecutors) {
    //
    //            @Override
    //            protected void saveCallResult(@NonNull List<Blog> itemList) {
    //                Utils.psLog("SaveCallResult of getNewsFeedListByShopId.");
    //
    //                db.beginTransaction();
    //
    //                try {
    //
    //                    blogDao.insertAll(itemList);
    //
    //                    db.setTransactionSuccessful();
    //
    //                } catch (Exception e) {
    //                    Utils.psErrorLog("Error in doing transaction of getNewsFeedListByShopId.", e);
    //                } finally {
    //                    db.endTransaction();
    //                }
    //            }
    //
    //            @Override
    //            protected boolean shouldFetch(@Nullable List<Blog> data) {
    //
    //                // Recent news always load from server
    //                return connectivity.isConnected();
    //
    //            }
    //
    //            @NonNull
    //            @Override
    //            protected LiveData<List<Blog>> loadFromDb() {
    //                Utils.psLog("Load getNewsFeedListByShopId From Db");
    //
    //                return blogDao.getAllNewsFeed();
    //            }
    //
    //            @NonNull
    //            @Override
    //            protected LiveData<ApiResponse<List<Blog>>> createCall() {
    //
    //                Utils.psLog("Call API Service to getNewsFeedListByShopId.");
    //                return psApiService.getAllNewsFeed(Config.API_KEY,  limit, offset);
    //
    //            }
    //
    //            @Override
    //            protected void onFetchFailed(String message) {
    //                Utils.psLog("Fetch Failed (getNewsFeedListByShopId) : " + message);
    //            }
    //        }.asLiveData();
    //    }
    fun getNextPageNewsFeedList(apiKey: String?, limit: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getAllNewsFeed(apiKey, limit, offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Blog?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        db.blogDao()!!.insertAll(response.body)
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

    fun getBlogById(id: String?): LiveData<Resource<Blog?>> {
        return object : NetworkBoundResource<Blog?, Blog?>(appExecutors) {
            override fun saveCallResult(blog: Blog) {
                Utils.psLog("SaveCallResult of getBlogById.")
                db.beginTransaction()
                try {
                    blogDao.insert(blog)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getBlogById.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(blog: Blog?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<Blog?> {
                Utils.psLog("Load getBlogById From Db")
                return blogDao.getBlogById(id)!!
            }

            override fun createCall(): LiveData<ApiResponse<Blog?>?> {
                Utils.psLog("Call API Service to getBlogById.")
                return apiService.getNewsById(Config.API_KEY, id)!!
            }

            override fun onFetchFailed(message: String) {
                Utils.psLog("Fetch Failed (getBlogById) : $message")
            }
        }.asLiveData()
    }

}