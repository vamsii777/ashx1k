package com.dewonderstruck.apps.ashx0.viewmodel.homelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.category.CategoryRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.holder.CategoryParameterHolder
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import javax.inject.Inject

class HomeTrendingCategoryListViewModel @Inject internal constructor(repository: CategoryRepository) : PSViewModel() {
    val homeTrendingCategoryListData: LiveData<Resource<List<Category>>>
    private val homeTrendingCategoryListobj = MutableLiveData<TmpDataHolder>()
    val homeTrendingCategoryLoadNetworkData: LiveData<Resource<Boolean>>
    private val homeTrendingCategoryLoadNetworkListObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var productParameterHolder = ProductParameterHolder()
    @JvmField
    var categoryParameterHolder = CategoryParameterHolder().trendingCategories
    fun setHomeTrendingCatrgoryListDataObj(categoryParameterHolder: CategoryParameterHolder?, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        tmpDataHolder.categoryParameterHolder = categoryParameterHolder
        homeTrendingCategoryListobj.value = tmpDataHolder
    }

    fun setHomeTrendingCategoryLoadNetworkObj(loginUserId: String, limit: String, offset: String, categoryParameterHolder: CategoryParameterHolder?) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.categoryParameterHolder = categoryParameterHolder
            homeTrendingCategoryLoadNetworkListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    internal inner class TmpDataHolder {
        var loginUserId = ""
        var limit = ""
        var offset = ""
        var shopId = ""
        var orderBy = ""
        var categoryParameterHolder: CategoryParameterHolder? = null
    }

    init {
        Utils.psLog("Inside ProductViewModel")
        homeTrendingCategoryListData = Transformations.switchMap(homeTrendingCategoryListobj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Category>>>()
            }
            repository.getAllSearchCategory(obj.categoryParameterHolder, obj.limit, obj.offset)
        }
        homeTrendingCategoryLoadNetworkData = Transformations.switchMap(homeTrendingCategoryLoadNetworkListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextSearchCategory(obj.limit, obj.offset, obj.categoryParameterHolder)
        }
    }
}