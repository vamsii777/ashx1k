package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class ProductFavouriteViewModel @Inject constructor(productRepository: ProductRepository) : PSViewModel() {
    //for product favourite list
    val productFavouriteData: LiveData<Resource<List<Product>>>
    private val productFavouriteListObj = MutableLiveData<TmpDataHolder>()
    val nextPageFavouriteLoadingData: LiveData<Resource<Boolean>>
    private val nextPageLoadingFavouriteObj = MutableLiveData<TmpDataHolder>()
    val favouritePostData: LiveData<Resource<Boolean>>
    private val sendFavouriteDataPostObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region Getter And Setter for product detail List
    fun setProductFavouriteListObj(loginUserId: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.offset = offset
            productFavouriteListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //Get Favourite Next Page
    fun setNextPageLoadingFavouriteObj(offset: String, loginUserId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.offset = offset
            nextPageLoadingFavouriteObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //fav post
    fun setFavouritePostDataObj(product_id: String, userId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = product_id
            tmpDataHolder.userId = userId
            sendFavouriteDataPostObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    internal class TmpDataHolder {
        var productId = ""
        var userId = ""
        var shopId = ""
        var loginUserId = ""
        var offset = ""
        var limit = ""
        var isConnected = false
    } //endregion

    //endregion
    //region Constructor
    init {
        //  product detail List
        productFavouriteData = Transformations.switchMap(productFavouriteListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Product>>>()
            }
            Utils.psLog("productFavouriteData")
            productRepository.getFavouriteList(Config.API_KEY, obj.loginUserId, obj.offset)
        }
        nextPageFavouriteLoadingData = Transformations.switchMap(nextPageLoadingFavouriteObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("nextPageFavouriteLoadingData")
            productRepository.getNextPageFavouriteProductList(obj.loginUserId, obj.offset)
        }
        favouritePostData = Transformations.switchMap(sendFavouriteDataPostObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            productRepository.uploadFavouritePostToServer(obj.productId, obj.userId)
        }
    }
}