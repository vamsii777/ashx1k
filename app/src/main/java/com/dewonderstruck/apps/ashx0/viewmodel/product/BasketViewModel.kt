package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.basket.BasketRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class BasketViewModel @Inject constructor(basketRepository: BasketRepository) : PSViewModel() {
    //for basket
    val allBasketList: LiveData<List<Basket>>
    private val basketListObj = MutableLiveData<TmpDataHolder>()
    val allBasketWithProductList: LiveData<List<Basket>>
    private val basketListWithProductObj = MutableLiveData<TmpDataHolder>()
    val basketSavedData: LiveData<Resource<Boolean>>
    private val basketSavedStateObj = MutableLiveData<TmpDataHolder>()
    val basketUpdateData: LiveData<Resource<Boolean>>
    private val basketUpdateStateObj = MutableLiveData<TmpDataHolder>()
    val basketDeleteData: LiveData<Resource<Boolean>>
    private val basketDeleteStateObj = MutableLiveData<TmpDataHolder>()
    val wholeBasketDeleteData: LiveData<Resource<Boolean>>
    private val wholeBasketDeleteStateObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var totalPrice = 0f
    @JvmField
    var basketCount = 0
    @JvmField
    var isDirectCheckout = false

    //endregion
    //region Getter And Setter for basket List
    fun setBasketListObj() {
        val tmpDataHolder = TmpDataHolder()
        basketListObj.value = tmpDataHolder
    }

    fun setBasketListWithProductObj() {
        val tmpDataHolder = TmpDataHolder()
        basketListWithProductObj.value = tmpDataHolder
    }

    fun setWholeBasketDeleteStateObj() {
        val tmpDataHolder = TmpDataHolder()
        wholeBasketDeleteStateObj.value = tmpDataHolder
    }

    //endregion
    //save basket
    fun setSaveToBasketListObj(id: Int, productId: String, count: Int, selectedAttributes: String, selectedColorId: String, selectedColorValue: String, selectedAttributeTotalPrice: String, basketPrice: Float, basketOriginalPrice: Float, shopId: String, priceStr: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.id = id
        tmpDataHolder.count = count
        tmpDataHolder.productId = productId
        tmpDataHolder.selectedAttributes = selectedAttributes
        tmpDataHolder.selectedColorId = selectedColorId
        tmpDataHolder.selectedColorValue = selectedColorValue
        tmpDataHolder.basketPrice = basketPrice
        tmpDataHolder.basketOriginalPrice = basketOriginalPrice
        tmpDataHolder.shopId = shopId
        tmpDataHolder.selectedAttributeTotalPrice = selectedAttributeTotalPrice
        tmpDataHolder.selectedAttributesPrice = priceStr
        basketSavedStateObj.setValue(tmpDataHolder)
    }

    //endregion
    //update basket
    fun setUpdateToBasketListObj(id: Int, count: Int) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.count = count
        tmpDataHolder.id = id
        basketUpdateStateObj.value = tmpDataHolder
    }

    //endregion
    //delete basket
    fun setDeleteToBasketListObj(id: Int) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.id = id
        basketDeleteStateObj.value = tmpDataHolder
    }

    //endregion
    //region Holder
    internal class TmpDataHolder {
        var id = 0
        var productId = ""
        var count = 0
        var selectedAttributes = ""
        var shopId = ""
        var selectedColorId = ""
        var selectedColorValue = ""
        var basketPrice = 0f
        private val basketOriginalPrice = 0f
        var isConnected = false
        private val selectedAttributeTotalPrice = ""
        private val selectedAttributesPrice = ""
    } //endregion

    //region Constructor
    init {
        //  basket List
        allBasketList = Transformations.switchMap(basketListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<Basket>>()
            }
            basketRepository.allBasketList
        }
        allBasketWithProductList = Transformations.switchMap(basketListWithProductObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<Basket>>()
            }
            basketRepository.allBasketWithProduct
        }

        //save
        basketSavedData = Transformations.switchMap(basketSavedStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            basketRepository.saveProduct(obj.id, obj.productId, obj.count, obj.selectedAttributes, obj.selectedColorId, obj.selectedColorValue, obj.selectedAttributeTotalPrice, obj.basketPrice, obj.basketOriginalPrice, obj.shopId, obj.selectedAttributesPrice)
        }

        //update
        basketUpdateData = Transformations.switchMap(basketUpdateStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            basketRepository.updateProduct(obj.id, obj.count)
        }

        //delete
        basketDeleteData = Transformations.switchMap(basketDeleteStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            basketRepository.deleteProduct(obj.id)
        }
        wholeBasketDeleteData = Transformations.switchMap(wholeBasketDeleteStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            basketRepository.deleteStoredBasket()
        }
    }
}