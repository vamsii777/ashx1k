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

class ProductRelatedViewModel @Inject internal constructor(productRepository: ProductRepository) : PSViewModel() {
    //for product detail list
    val productRelatedData: LiveData<Resource<List<Product>>>
    private val productRelatedListObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region Getter And Setter for product detail List
    fun setProductRelatedListObj(productId: String, catId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = productId
            tmpDataHolder.catId = catId
            productRelatedListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var offset = ""
        var productId = ""
        var catId = ""
        var shopId = ""
        var isConnected = false
    } //endregion

    //region Constructor
    init {
        //  product detail List
        productRelatedData = Transformations.switchMap(productRelatedListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Product>>>()
            }
            Utils.psLog("ProductRelatedViewModel.")
            productRepository.getRelatedList(Config.API_KEY, obj.productId, obj.catId)
        }
    }
}