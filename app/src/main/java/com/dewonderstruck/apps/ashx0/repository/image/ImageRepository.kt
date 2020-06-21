package com.dewonderstruck.apps.ashx0.repository.image

import androidx.lifecycle.LiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.ImageDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Image
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 12/8/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
class ImageRepository //endregion
//region Constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region Variables
                             private val imageDao: ImageDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    //endregion
    //region News Repository Functions for ViewModel
    /**
     * Load Image List
     *
     * @param imgType Image Type
     * @param imgParentId Image Parent Id
     * @return Image List filter by news id
     */
    fun getImageList(imgType: String?, imgParentId: String): LiveData<Resource<List<Image?>?>> {
        val functionKey = "getImageList$imgParentId"
        return object : NetworkBoundResource<List<Image?>?, List<Image?>?>(appExecutors) {
            protected override fun saveCallResult(item: List<Image?>) {
                Utils.psLog("SaveCallResult of getImageList.")
                db.beginTransaction()
                try {
                    imageDao.deleteByImageIdAndType(imgParentId, imgType)
                    imageDao.insertAll(item)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Image?>?): Boolean {
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Image?>> {
                Utils.psLog("Load image list from db")
                return imageDao.getByImageIdAndType(imgParentId, imgType)
            }

            override fun createCall(): LiveData<ApiResponse<List<Image?>?>?> {
                Utils.psLog("Call API webservice to get image list." + apiService.getImageList(Config.API_KEY, imgParentId, imgType))
                return apiService.getImageList(Config.API_KEY, imgParentId, imgType)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of getting image list.")
                rateLimiter.reset(functionKey)
            }
        }.asLiveData()
    } //endregion

}