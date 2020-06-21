package com.dewonderstruck.apps.ashx0.viewmodel.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.city.CityRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.City
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class CityViewModel @Inject internal constructor(repository: CityRepository) : PSViewModel() {
    val cityListData: LiveData<Resource<List<City>>>
    private val cityListObj = MutableLiveData<TmpDataHolder>()
    val nextPageCityListData: LiveData<Resource<Boolean>>
    private val nextPageCityListObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var countryId: String? = null
    fun setCityListObj(shopId: String, countryId: String, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.shopId = shopId
        tmpDataHolder.countryId = countryId
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        cityListObj.value = tmpDataHolder
    }

    fun setNextPageCityListObj(shopId: String, countryId: String, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.shopId = shopId
        tmpDataHolder.countryId = countryId
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        nextPageCityListObj.value = tmpDataHolder
    }

    internal inner class TmpDataHolder {
        var loginUserId = ""
        var limit = ""
        var offset = ""
        var shopId = ""
        var countryId = ""
    }

    init {
        Utils.psLog("Inside CityViewModel")
        cityListData = Transformations.switchMap(cityListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<City>>>()
            }
            repository.getCityListWithShopId(Config.API_KEY, obj.shopId, obj.countryId, obj.limit, obj.offset)
        }
        nextPageCityListData = Transformations.switchMap(nextPageCityListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextPageCityListWithShopId(obj.shopId, obj.countryId, obj.limit, obj.offset)
        }
    }
}