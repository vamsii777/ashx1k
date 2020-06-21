package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ProductSpecs
import javax.inject.Inject

class ProductSpecsViewModel @Inject constructor(productRepository: ProductRepository) : PSViewModel() {
    //for product specs list
    @JvmField
    var isSpecsData = false
    val productSpecsListData: LiveData<List<ProductSpecs>>
    private val productSpecsObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region setProductSpecsListObj
    fun setProductSpecsListObj(productId: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.productId = productId
        productSpecsObj.value = tmpDataHolder
    }

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var offset = ""
        var productId = ""
        var isConnected = false
    } //endregion

    //region Constructor
    init {
        //  product specs List
        productSpecsListData = Transformations.switchMap(productSpecsObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<ProductSpecs>>()
            }
            Utils.psLog("product color List.")
            productRepository.getProductSpecs(obj.productId)
        }
    }
}