package com.dewonderstruck.apps.ashx0.viewmodel.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.collection.ProductCollectionRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class ProductCollectionProductViewModel @Inject internal constructor(repository: ProductCollectionRepository) : PSViewModel() {
    // for ProductCollectionHeader
    val productCollectionProductListData: LiveData<Resource<List<Product>>>
    private val productCollectionProductListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region ProductCollectionHeader
    // Get ProductCollectionHeader
    fun setProductCollectionProductListObj(limit: String, offset: String, id: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.id = id
            productCollectionProductListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //Get Latest ProductCollectionHeader Next Page
    fun setNextPageLoadingStateObj(limit: String, offset: String, id: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.id = id
            nextPageLoadingStateObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal inner class TmpDataHolder {
        var offset = ""
        var limit = ""
        var id = ""
        var isConnected = false
        var shopId = ""
    } //endregion

    //region Constructor
    init {
        Utils.psLog("Inside ProductViewModel")


        // Latest ProductCollectionHeader List
        productCollectionProductListData = Transformations.switchMap(productCollectionProductListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Product>>>()
            }
            repository.getProductCollectionProducts(Config.API_KEY, obj.limit, obj.offset, obj.id)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextPageProductCollectionProduct(obj.limit, obj.offset, obj.id)
        }
    }
}