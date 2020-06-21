package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct
import javax.inject.Inject

class HistoryProductViewModel @Inject internal constructor(productRepository: ProductRepository) : PSViewModel() {
    val allHistoryProductList: LiveData<List<HistoryProduct>>
    private val historyListObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region Getter And Setter for basket List
    fun setHistoryProductListObj(offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.offset = offset
        historyListObj.value = tmpDataHolder
    }

    //endregion
    //region Holder
    internal inner class TmpDataHolder {
        var id = 0
        var productId = ""
        var loginUserId = ""
        var offset = ""
        var isConnected = false
    } //endregion

    init {
        //  basket List
        allHistoryProductList = Transformations.switchMap(historyListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<HistoryProduct>>()
            }
            Utils.psLog("get basket")
            productRepository.getAllHistoryList(obj.offset)
        }
    }
}