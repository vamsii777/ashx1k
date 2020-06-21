package com.dewonderstruck.apps.ashx0.viewmodel.coupondiscount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.coupondiscount.CouponDiscountRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.CouponDiscount
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class CouponDiscountViewModel @Inject internal constructor(repository: CouponDiscountRepository) : PSViewModel() {
    val couponDiscountData: LiveData<Resource<CouponDiscount>>
    private val couponDiscountObj = MutableLiveData<TmpDataHolder>()
    fun setCouponDiscountObj(code: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.code = code
        couponDiscountObj.value = tmpDataHolder
    }

    internal inner class TmpDataHolder {
        var code = ""
        var shopId = ""
    }

    init {
        couponDiscountData = Transformations.switchMap(couponDiscountObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<CouponDiscount>>()
            }
            repository.getCouponDiscount(obj.code)
        }
    }
}