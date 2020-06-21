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

class ProductListByCatIdViewModel @Inject internal constructor(repository: ProductRepository) : PSViewModel() {
    // region variables
    private val productList: LiveData<Resource<List<Product>>>
    private val productListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region setProductListObj
    fun setProductListObj(loginUserId: String, offset: String, catId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.offset = offset
            tmpDataHolder.categoryId = catId
            productListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    fun getproductList(): LiveData<Resource<List<Product>>> {
        return productList
    }

    //Get Latest Product Next Page
    fun setNextPageLoadingStateObj(loginUserId: String, offset: String, catId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.offset = offset
            tmpDataHolder.categoryId = catId
            nextPageLoadingStateObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal inner class TmpDataHolder {
        var productId = ""
        var loginUserId = ""
        var offset = ""
        var categoryId = ""
        var isConnected = false
    }

    //region Constructor
    init {
        Utils.psLog("Inside ProductListByCatIdViewModel")

        // Latest Product List
        productList = Transformations.switchMap(productListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Product>>>()
            }
            Utils.psLog("productList")
            repository.getProductListByCatId(Config.API_KEY, obj.loginUserId, obj.offset, obj.categoryId)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("nextPageLoadingStateData")
            repository.getNextPageProductListByCatId(obj.loginUserId, obj.offset, obj.categoryId)
        }
    }
}