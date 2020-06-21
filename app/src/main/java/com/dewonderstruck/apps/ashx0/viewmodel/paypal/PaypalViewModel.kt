package com.dewonderstruck.apps.ashx0.viewmodel.paypal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.paypal.PaypalRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class PaypalViewModel @Inject internal constructor(repository: PaypalRepository) : PSViewModel() {
    val paypalTokenData: LiveData<Resource<Boolean>>
    private val paypalTokenObj = MutableLiveData<Boolean>()
    fun setPaypalTokenObj() {
        paypalTokenObj.value = true
    }

    init {
        paypalTokenData = Transformations.switchMap(paypalTokenObj) { obj: Boolean? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("paypalTokenData")
            repository.paypalToekn
        }
    }
}