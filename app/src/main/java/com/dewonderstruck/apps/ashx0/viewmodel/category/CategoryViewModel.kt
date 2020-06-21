package com.dewonderstruck.apps.ashx0.viewmodel.category

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

/**
 * Created by Vamsi Madduluri on 12/02/2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class CategoryViewModel @Inject internal constructor(repository: CategoryRepository) : PSViewModel() {
    //region Variables
    val categoryListData: LiveData<Resource<List<Category>>>
    private val categoryListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var productParameterHolder = ProductParameterHolder()
    @JvmField
    var categoryParameterHolder = CategoryParameterHolder()

    //endregion
    fun setCategoryListObj(loginUserId: String, categoryParameterHolder: CategoryParameterHolder?, limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.offset = offset
            tmpDataHolder.limit = limit
            tmpDataHolder.categoryParameterHolder = categoryParameterHolder
            categoryListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //Get Latest Category Next Page
    fun setNextPageLoadingStateObj(limit: String, offset: String, categoryParameterHolder: CategoryParameterHolder?) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.offset = offset
            tmpDataHolder.limit = limit
            tmpDataHolder.categoryParameterHolder = categoryParameterHolder
            nextPageLoadingStateObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    internal inner class TmpDataHolder {
        var loginUserId = ""
        var limit = ""
        var offset = ""
        var isConnected = false
        var shopId = ""
        var orderBy = ""
        var categoryParameterHolder: CategoryParameterHolder? = null
    }

    //endregion
    //region Constructors
    init {
        Utils.psLog("CategoryViewModel")
        categoryListData = Transformations.switchMap(categoryListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Category>>>()
            }
            Utils.psLog("CategoryViewModel : categories")
            repository.getAllSearchCategory(obj.categoryParameterHolder, obj.limit, obj.offset)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Category List.")
            repository.getNextSearchCategory(obj.limit, obj.offset, obj.categoryParameterHolder)
        }
    }
}