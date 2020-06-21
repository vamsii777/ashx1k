package com.dewonderstruck.apps.ashx0.viewmodel.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.shop.ShopRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Shop
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 3/19/19.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class ShopViewModel @Inject internal constructor(repository: ShopRepository) : PSViewModel() {
    //region Variables
    val shopData: LiveData<Resource<Shop>>
    private val shopObj = MutableLiveData<ShopProfileTmpDataHolder>()
    @JvmField
    var selectedShopId: String? = null
    @JvmField
    var flag: String? = null
    @JvmField
    var stripePublishableKey: String? = null

    //endregion
    //region Shop Detail
    fun setShopObj(apiKey: String?) {
        val tmpDataHolder = ShopProfileTmpDataHolder(apiKey)
        shopObj.value = tmpDataHolder
    }

    //endregion
    //region Holders
    internal class ShopProfileTmpDataHolder(var apiKey: String?)  //endregion

    //endregion
    //region Constructors
    init {
        shopData = Transformations.switchMap(shopObj) { obj: ShopProfileTmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Shop>>()
            }
            repository.getShop(obj.apiKey)
        }
    }
}