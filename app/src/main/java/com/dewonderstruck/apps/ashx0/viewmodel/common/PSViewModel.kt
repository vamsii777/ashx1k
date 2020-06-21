package com.dewonderstruck.apps.ashx0.viewmodel.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewonderstruck.apps.ashx0.utils.Utils.LoadingDirection

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
open class PSViewModel : ViewModel() {
    @JvmField
    var loadingDirection = LoadingDirection.none

    //endregion
    val loadingState = MutableLiveData<Boolean>()
    @JvmField
    var offset = 0
    var limit = 0
    @JvmField
    var forceEndLoading = false
    @JvmField
    var isLoading = false

    //region For loading status
    fun setLoadingState(state: Boolean) {
        isLoading = state
        loadingState.value = state
    }

}