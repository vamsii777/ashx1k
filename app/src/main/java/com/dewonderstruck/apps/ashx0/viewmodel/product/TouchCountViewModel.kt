package com.dewonderstruck.apps.ashx0.viewmodel.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class TouchCountViewModel @Inject internal constructor(productRepository: ProductRepository) : PSViewModel() {
    val touchCountPostData: LiveData<Resource<Boolean>>
    private val sendTouchCountDataPostObj = MutableLiveData<TmpDataHolder>()
    fun setTouchCountPostDataObj(userId: String, typeId: String, typeName: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.userId = userId
        tmpDataHolder.typeId = typeId
        tmpDataHolder.typeName = typeName
        sendTouchCountDataPostObj.value = tmpDataHolder
    }

    internal class TmpDataHolder {
        var userId = ""
        var typeId = ""
        var typeName = ""
        var shopId = ""
    }

    init {
        touchCountPostData = Transformations.switchMap(sendTouchCountDataPostObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            productRepository.uploadTouchCountPostToServer(obj.userId, obj.typeId, obj.typeName)
        }
    }
}