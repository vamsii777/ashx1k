package com.dewonderstruck.apps.ashx0.viewmodel.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.rating.RatingRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Rating
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class RatingViewModel @Inject internal constructor(repository: RatingRepository) : PSViewModel() {
    //region variables
    val ratingList: LiveData<Resource<List<Rating>>>
    private val ratingListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()
    val ratingPostData: LiveData<Resource<Boolean>>
    private val ratingPostObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var productId: String? = null
    @JvmField
    var numStar = 0f
    @JvmField
    var isData = false

    //endregion
    //Get Latest Rating
    fun setRatingListObj(productId: String, limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = productId
            tmpDataHolder.offset = offset
            tmpDataHolder.limit = limit
            ratingListObj.value = tmpDataHolder

            //start loading
            setLoadingState(true)
        }
    }

    //Get Latest Rating Next Page
    fun setNextPageLoadingStateObj(productId: String, limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = productId
            tmpDataHolder.offset = offset
            tmpDataHolder.limit = limit
            nextPageLoadingStateObj.value = tmpDataHolder

            //start loading
            setLoadingState(true)
        }
    }

    //endregion
    //Get Rating post
    fun setRatingPostObj(title: String, description: String, rating: String, userId: String, productId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = productId
            tmpDataHolder.loginUserId = userId
            tmpDataHolder.rating = rating
            tmpDataHolder.description = description
            tmpDataHolder.title = title
            ratingPostObj.value = tmpDataHolder

            //start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal inner class TmpDataHolder {
        var productId = ""
        var loginUserId = ""
        var offset = ""
        var limit = ""
        var categoryId = ""
        var isConnected = false
        var title = ""
        var description = ""
        var rating = ""
        var shopId = ""
    }

    //region Constructor
    init {
        Utils.psLog("Inside RatingViewModel")

        //Latest Rating List
        ratingList = Transformations.switchMap(ratingListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Rating>>>()
            }
            Utils.psLog("ratingList")
            repository.getRatingListByProductId(Config.API_KEY, obj.productId, obj.limit, obj.offset)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("nextPageLoadingStateData")
            repository.getNextPageRatingListByProductId(obj.productId, obj.limit, obj.offset)
        }
        ratingPostData = Transformations.switchMap(ratingPostObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("ratingPostData")
            repository.uploadRatingPostToServer(obj.title, obj.description, obj.rating, obj.loginUserId, obj.productId)
        }
    }
}