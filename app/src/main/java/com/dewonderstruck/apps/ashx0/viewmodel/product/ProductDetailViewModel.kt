package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class ProductDetailViewModel @Inject internal constructor(productRepository: ProductRepository) : PSViewModel() {
    //for product detail list
    @JvmField
    var productId = ""
    @JvmField
    var historyFlag = ""
    @JvmField
    var basketFlag = ""
    @JvmField
    var currencySymbol = ""
    @JvmField
    var price = ""
    @JvmField
    var attributes = ""
    @JvmField
    var count = ""
    @JvmField
    var colorId = ""
    var basket: Basket? = null
    @JvmField
    var basketId = 0
    @JvmField
    var productContainer: Product? = null
    @JvmField
    var isAddtoCart = false
    val productDetailData: LiveData<Resource<Product>>
    private val productDetailObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region Getter And Setter for product detail List
    fun setProductDetailObj(productId: String, historyFlag: String, userId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = productId
            tmpDataHolder.historyFlag = historyFlag
            tmpDataHolder.userId = userId
            productDetailObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var offset = ""
        var productId = ""
        var historyFlag = ""
        var userId = ""
        var shopId = ""
        var isConnected = false
    } //endregion

    //region Constructor
    init {
        //  product detail List
        productDetailData = Transformations.switchMap(productDetailObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Product>>()
            }
            Utils.psLog("product detail List.")
            productRepository.getProductDetail(Config.API_KEY, obj.productId, obj.historyFlag, obj.userId)
        }
    }
}