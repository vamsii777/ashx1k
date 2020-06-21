package com.dewonderstruck.apps.ashx0.viewmodel.apploading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.apploading.AppLoadingRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.PSAppInfo
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class AppLoadingViewModel @Inject internal constructor(repository: AppLoadingRepository) : PSViewModel() {
    val deleteHistoryData: LiveData<Resource<PSAppInfo>>
    private val deleteHistoryObj = MutableLiveData<TmpDataHolder>()
    var psAppInfo: PSAppInfo? = null
    fun setDeleteHistoryObj(startDate: String?, endDate: String?) {
        val tmpDataHolder = TmpDataHolder(startDate, endDate)
        deleteHistoryObj.value = tmpDataHolder
    }

    internal class TmpDataHolder(var startDate: String?, var endDate: String?)

    init {
        deleteHistoryData = Transformations.switchMap(deleteHistoryObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<PSAppInfo>>()
            }
            Utils.psLog("AppLoadingViewModel")
            repository.deleteTheSpecificObjects(obj.startDate, obj.endDate)
        }
    }
}