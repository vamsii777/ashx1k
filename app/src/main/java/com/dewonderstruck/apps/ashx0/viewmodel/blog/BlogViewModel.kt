package com.dewonderstruck.apps.ashx0.viewmodel.blog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.blog.BlogRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Blog
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class BlogViewModel @Inject internal constructor(repository: BlogRepository) : PSViewModel() {
    val newsFeedData: LiveData<Resource<List<Blog>>>
    private val newsFeedObj = MutableLiveData<TmpDataHolder>()
    val nextPageNewsFeedData: LiveData<Resource<Boolean>>
    private val nextPageNewsFeedObj = MutableLiveData<TmpDataHolder>()
    val blogByIdData: LiveData<Resource<Blog>>
    private val blogByIdObj = MutableLiveData<BlogByIdTmpDataHolder>()
    var shopName: String? = null
    var shopId: String? = null
    fun setNewsFeedObj(limit: String?, offset: String?) {
        val tmpDataHolder = TmpDataHolder(limit!!, offset!!)
        newsFeedObj.value = tmpDataHolder
    }

    fun setNextPageNewsFeedObj(limit: String?, offset: String?) {
        val tmpDataHolder = TmpDataHolder(limit!!, offset!!)
        nextPageNewsFeedObj.value = tmpDataHolder
    }

    fun setBlogByIdObj(id: String) {
        val blogByIdTmpDataHolder = BlogByIdTmpDataHolder(id)
        blogByIdObj.value = blogByIdTmpDataHolder
    }

    internal inner class TmpDataHolder(var limit: String, var offset: String)

    internal inner class BlogByIdTmpDataHolder(var id: String)

    init {
        newsFeedData = Transformations.switchMap(newsFeedObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Blog>>>()
            }
            repository.getNewsFeedList(obj.limit, obj.offset)
        }
        nextPageNewsFeedData = Transformations.switchMap(nextPageNewsFeedObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextPageNewsFeedList(Config.API_KEY, obj.limit, obj.offset)
        }
        blogByIdData = Transformations.switchMap(blogByIdObj) { obj: BlogByIdTmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Blog>>()
            }
            repository.getBlogById(obj.id)
        }
    }
}