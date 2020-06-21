package com.dewonderstruck.apps.ashx0.viewmodel.shippingmethod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.shippingmethod.ShippingMethodRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ShippingCost
import com.dewonderstruck.apps.ashx0.viewobject.ShippingCostContainer
import com.dewonderstruck.apps.ashx0.viewobject.ShippingMethod
import com.dewonderstruck.apps.ashx0.viewobject.ShippingProductContainer
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import java.util.*
import javax.inject.Inject

class ShippingMethodViewModel @Inject internal constructor(repository: ShippingMethodRepository) : PSViewModel() {
    val shippingMethodsData: LiveData<Resource<List<ShippingMethod>>>
    private val shippingMethodsObj = MutableLiveData<Boolean>()
    var shippingCostByZone: String? = null
    var shippingProductContainer: List<ShippingProductContainer> = ArrayList()
    private val shippingCostByCountryAndCityData: LiveData<Resource<ShippingCost>>
    private val shippingCostByCountryAndCityDataObj = MutableLiveData<TmpDataHolder>()
    fun setShippingMethodsObj() {
        shippingMethodsObj.value = true
    }

    //get shipping cost by zone
    fun setshippingCostByCountryAndCityObj(shippingCostContainer: ShippingCostContainer?) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.shippingCostContainer = shippingCostContainer
        shippingCostByCountryAndCityDataObj.value = tmpDataHolder
    }

    fun getshippingCostByCountryAndCityData(): LiveData<Resource<ShippingCost>> {
        return shippingCostByCountryAndCityData
    }

    internal inner class TmpDataHolder {
        var shippingCostContainer: ShippingCostContainer? = null
    }

    init {
        shippingMethodsData = Transformations.switchMap(shippingMethodsObj) { obj: Boolean? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<ShippingMethod>>>()
            }
            repository.allShippingMethods
        }
        shippingCostByCountryAndCityData = Transformations.switchMap(shippingCostByCountryAndCityDataObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<ShippingCost>>()
            }
            repository.postShippingByCountryAndCity(obj.shippingCostContainer)
        }
    }
}