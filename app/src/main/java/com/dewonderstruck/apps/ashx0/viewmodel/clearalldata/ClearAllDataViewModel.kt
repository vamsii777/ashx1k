package com.dewonderstruck.apps.ashx0.viewmodel.clearalldata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.clearpackage.ClearPackageRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class ClearAllDataViewModel @Inject constructor(repository: ClearPackageRepository) : PSViewModel() {
    val deleteAllDataData: LiveData<Resource<Boolean>>
    private val deleteAllDataObj = MutableLiveData<Boolean>()
    fun setDeleteAllDataObj() {
        deleteAllDataObj.value = true
    }

    init {
        deleteAllDataData = Transformations.switchMap(deleteAllDataObj) { obj: Boolean? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.clearAllTheData()
        }
    }
}