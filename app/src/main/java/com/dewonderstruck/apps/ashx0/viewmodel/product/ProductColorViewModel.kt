package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ProductColor
import java.util.*
import javax.inject.Inject

class ProductColorViewModel @Inject internal constructor(productRepository: ProductRepository) : PSViewModel() {
    //for product color list
    @JvmField
    var isColorData = false
    val productColorData: LiveData<List<ProductColor>>
    private val productColorObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var proceededColorListData: List<ProductColor> = ArrayList()
    @JvmField
    var colorSelectId = ""
    @JvmField
    var colorSelectValue = ""

    //endregion
    //region Getter And Setter for product color List
    fun setProductColorListObj(productId: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.productId = productId
        tmpDataHolder.colorSelectedId = colorSelectId
        tmpDataHolder.colorSelectedValue = colorSelectValue
        productColorObj.value = tmpDataHolder

        // start loading
        setLoadingState(true)
    }

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var offset = ""
        var productId = ""
        var colorSelectedId = ""
        var colorSelectedValue = ""
        var isConnected = false
    } //endregion

    //region Constructor
    init {
        //  product color List
        productColorData = Transformations.switchMap(productColorObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<ProductColor>>()
            }
            Utils.psLog("product color List.")
            productRepository.getProductColor(obj.productId)
        }
    }
}