package com.dewonderstruck.apps.ashx0.viewmodel.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.subcategory.SubCategoryRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class SubCategoryViewModel @Inject internal constructor(repository: SubCategoryRepository) : PSViewModel() {
    val allSubCategoryListData: LiveData<Resource<List<SubCategory>>>
    private val allSubCategoryListObj = MutableLiveData<TmpDataHolder>()

    //    private LiveData<Resource<List<SubCategory>>> subCategoryListData;
    //    private MutableLiveData<TmpDataHolder> subCategoryListObj = new MutableLiveData<>();
    //    private LiveData<Resource<Boolean>> nextPageLoadingStateData;
    //    private MutableLiveData<TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();
    private val subCategoryListWithCatIdData: LiveData<Resource<List<SubCategory>>>
    private val subCategoryListWithCatIdObj = MutableLiveData<TmpDataHolder>()
    private val nextPageLoadingStateWithCatIdData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateWithCatIdObj = MutableLiveData<TmpDataHolder>()
    fun setSubCategoryListWithCatIdObj(loginUserId: String, offset: String, catId: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.loginUserId = loginUserId
        tmpDataHolder.offset = offset
        tmpDataHolder.catId = catId
        subCategoryListWithCatIdObj.value = tmpDataHolder
    }

    fun getsubCategoryListWithCatIdData(): LiveData<Resource<List<SubCategory>>> {
        return subCategoryListWithCatIdData
    }

    fun getnextPageLoadingStateWithCatIdData(): LiveData<Resource<Boolean>> {
        return nextPageLoadingStateWithCatIdData
    }

    fun setNextPageLoadingStateWithCatIdObj(loginUserId: String, limit: String, offset: String, catId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loginUserId = loginUserId
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.catId = catId
            nextPageLoadingStateWithCatIdObj.value = tmpDataHolder
            setLoadingState(true)
        }
    }

    fun setAllSubCategoryListObj() {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            allSubCategoryListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    internal inner class TmpDataHolder {
        var loginUserId = ""
        var shopId = ""
        var offset = ""
        var limit = ""
        var catId = ""
        var isConnected = false
    }

    init {
        Utils.psLog("Inside SubCategoryViewModel")
        allSubCategoryListData = Transformations.switchMap(allSubCategoryListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<SubCategory>>>()
            }
            Utils.psLog("allSubCategoryListData")
            repository.getAllSubCategoryList(Config.API_KEY)
        }

//        subCategoryListData = Transformations.switchMap(subCategoryListObj, obj -> {
//            if (obj == null) {
//                return AbsentLiveData.create();
//            }
//            Utils.psLog("subCategoryListData");
//            return repository.getSubCategoryList(Config.API_KEY, obj.limit, obj.offset);
//        });

//        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
//            if (obj == null) {
//                return AbsentLiveData.create();
//            }
//            Utils.psLog("nextPageLoadingStateData");
//            return repository.getNextPageSubCategory( obj.limit, obj.offset);
//        });
        subCategoryListWithCatIdData = Transformations.switchMap(subCategoryListWithCatIdObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<SubCategory>>>()
            }
            repository.getSubCategoriesWithCatId(obj.loginUserId, obj.offset, obj.catId)
        }
        nextPageLoadingStateWithCatIdData = Transformations.switchMap(nextPageLoadingStateWithCatIdObj) { obj ->
            if (obj == null) {
                AbsentLiveData.create()
            } else repository.getNextPageSubCategoriesWithCatId(obj.loginUserId, obj.limit, obj.offset, obj.catId)
        }
    }
}