package com.dewonderstruck.apps.ashx0.viewmodel.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.country.CountryRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Country
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class CountryViewModel @Inject internal constructor(repository: CountryRepository) : PSViewModel() {
    val countryListData: LiveData<Resource<List<Country>>>
    private val countryListObj = MutableLiveData<TmpDataHolder>()
    val nextPageCountryListData: LiveData<Resource<Boolean>>
    private val nextPageCountryListObj = MutableLiveData<TmpDataHolder>()
    fun setCountryListObj(shopId: String, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.shopId = shopId
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        countryListObj.value = tmpDataHolder
    }

    fun setNextPageCountryListObj(shopId: String, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.shopId = shopId
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        nextPageCountryListObj.value = tmpDataHolder
    }

    internal inner class TmpDataHolder {
        var loginUserId = ""
        var limit = ""
        var offset = ""
        var shopId = ""
    }

    init {
        Utils.psLog("Inside CountryViewModel")
        countryListData = Transformations.switchMap(countryListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Country>>>()
            }
            repository.getCountryListWithShopId(Config.API_KEY, obj.shopId, obj.limit, obj.offset)
        }
        nextPageCountryListData = Transformations.switchMap(nextPageCountryListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextPageCityListWithShopId(Config.API_KEY, obj.shopId, obj.limit, obj.offset)
        }
    }
}