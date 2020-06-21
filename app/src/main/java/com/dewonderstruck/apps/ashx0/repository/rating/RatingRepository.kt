package com.dewonderstruck.apps.ashx0.repository.rating

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.RatingDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Rating
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RatingRepository @Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, ratingDao: RatingDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    private val ratingDao: RatingDao

    //region Constructor
    @JvmField
    @Inject
    var pref: SharedPreferences? = null

    //Get Rating List by CatId
    fun getRatingListByProductId(apiKey: String?, productId: String?, limit: String?, offset: String?): LiveData<Resource<List<Rating?>?>> {
        return object : NetworkBoundResource<List<Rating?>?, List<Rating?>?>(appExecutors) {
            protected override fun saveCallResult(ratingList: List<Rating?>) {
                Utils.psLog("SaveCallResult of recent products.")
                db.beginTransaction()
                try {
                    ratingDao.deleteAll()
                    ratingDao.insertAll(ratingList)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of product list by catId .", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Rating?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Rating?>> {
                Utils.psLog("Load product From Db by catId")
                return ratingDao.getRatingById(productId)
            }

            override fun createCall(): LiveData<ApiResponse<List<Rating?>?>?> {
                Utils.psLog("Call API Service to get product list by catId.")
                return apiService.getAllRatingList(apiKey, productId, limit, offset)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (get product list by catId) : $message")
            }
        }.asLiveData()
    }

    // get next page Rating List by catId
    fun getNextPageRatingListByProductId(productId: String?, limit: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getAllRatingList(Config.API_KEY, productId, limit, offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Rating?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (apiResponse.value != null) {
                            ratingDao.insertAll(apiResponse.value!!.body)
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
    //rating post
    fun uploadRatingPostToServer(title: String?, description: String?, rating: String?, userId: String?, productId: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response: Response<Rating?>
                response = apiService.postRating(Config.API_KEY, title, description, rating, userId, productId)!!.execute()


                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            ratingDao.insert(apiResponse.body)
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
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, false))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, false))
            }
        }
        return statusLiveData
    } //endregion

    init {
        Utils.psLog("Inside RatingRepository")
        this.ratingDao = ratingDao
    }
}