package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail
import javax.inject.Inject

class ProductAttributeDetailViewModel @Inject constructor(productRepository: ProductRepository) : PSViewModel() {
    //endregion
    //region Getter And Setter for product attribute detail List
    //for product attribute detail list
    val productAttributeDetailListData: LiveData<List<ProductAttributeDetail>>
    private val ProductAttributeDetailObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var offset = ""
        var productId = ""
        var headerId = ""
        var isConnected = false
    } //endregion

    //endregion
    //region Constructor
    init {
        //  product attribute detail List
        productAttributeDetailListData = Transformations.switchMap(ProductAttributeDetailObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<ProductAttributeDetail>>()
            }
            Utils.psLog("product attribute detail List.")
            productRepository.getProductAttributeDetail(obj.productId, obj.headerId)
        }
    }
}