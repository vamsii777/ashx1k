package com.dewonderstruck.apps.ashx0.viewmodel.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.collection.ProductCollectionRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 10/27/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class ProductCollectionViewModel @Inject internal constructor(repository: ProductCollectionRepository) : PSViewModel() {
    // for ProductCollectionHeader
    val productCollectionHeaderListDataForHome: LiveData<Resource<List<ProductCollectionHeader>>>
    private val productCollectionHeaderListForHomeObj = MutableLiveData<TmpDataHolder>()
    val productCollectionHeaderListData: LiveData<Resource<List<ProductCollectionHeader>>>
    private val productCollectionHeaderListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region ProductCollectionHeader
    // Get ProductCollectionHeader
    fun setProductCollectionHeaderListObj(limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            productCollectionHeaderListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    fun setProductCollectionHeaderListForHomeObj(collectionLimit: String, colProductLimit: String, productLimit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.collectionLimit = collectionLimit
            tmpDataHolder.colProductLimit = colProductLimit
            tmpDataHolder.productLimit = productLimit
            tmpDataHolder.offset = offset
            productCollectionHeaderListForHomeObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //Get Latest ProductCollectionHeader Next Page
    fun setNextPageLoadingStateObj(limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
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
        var limit = ""
        var collectionLimit = ""
        var colProductLimit = ""
        var productLimit = ""
        var offset = ""
        var isConnected = false
        var shopId = ""
    } //endregion

    //region Constructor
    init {
        Utils.psLog("Inside ProductViewModel")


        // Latest ProductCollectionHeader List
        productCollectionHeaderListData = Transformations.switchMap(productCollectionHeaderListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<ProductCollectionHeader>>>()
            }
            repository.getProductionCollectionHeaderList(Config.API_KEY, obj.limit, obj.offset)
        }
        productCollectionHeaderListDataForHome = Transformations.switchMap(productCollectionHeaderListForHomeObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<ProductCollectionHeader>>>()
            }
            repository.getProductionCollectionHeaderListForHome(Config.API_KEY, obj.collectionLimit, obj.colProductLimit, obj.productLimit, obj.offset)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextPageProductionCollectionHeaderList(obj.limit, obj.offset)
        }
    }
}