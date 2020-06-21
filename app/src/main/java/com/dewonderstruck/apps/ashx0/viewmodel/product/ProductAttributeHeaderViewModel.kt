package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeHeader
import java.util.*
import javax.inject.Inject

class ProductAttributeHeaderViewModel @Inject constructor(productRepository: ProductRepository) : PSViewModel() {
    //for product attribute detail list
    val productAttributeHeaderListData: LiveData<List<ProductAttributeHeader>>
    private val ProductAttributeHeaderObj = MutableLiveData<TmpDataHolder>()
    var headerId: String? = null
    @JvmField
    var isHeaderData = false
    @JvmField
    var productAttributeDetail: ProductAttributeDetail? = null
    @JvmField
    var price = 0f
    @JvmField
    var originalPrice = 0f
    @JvmField
    var headerIdList: List<String> = ArrayList()
    @JvmField
    var basketItemHolderHashMap: Map<String, String> = HashMap()
    @JvmField
    var attributeHeaderHashMap: Map<String, Int> = HashMap()
    @JvmField
    var priceAfterAttribute = 0f
    @JvmField
    var originalPriceAfterAttribute = 0f

    //endregion
    //region Getter And Setter for product attribute detail List
    fun setProductAttributeHeaderListObj(productId: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.productId = productId
        ProductAttributeHeaderObj.value = tmpDataHolder
    }

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var offset = ""
        var productId = ""
        var isConnected = false
    } //endregion

    //endregion
    //region Constructor
    init {
        //  product attribute detail List
        productAttributeHeaderListData = Transformations.switchMap(ProductAttributeHeaderObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<ProductAttributeHeader>>()
            }
            Utils.psLog("ProductAttributeHeaderListData")
            productRepository.getProductAttributeHeader(obj.productId)
        }
    }
}