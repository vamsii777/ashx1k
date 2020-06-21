package com.dewonderstruck.apps.ashx0.viewmodel.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.image.ImageRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Image
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 12/8/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class ImageViewModel @Inject internal constructor(repository: ImageRepository) : PSViewModel() {
    //region Variables
    // Get Image Video List
    val imageListLiveData: LiveData<Resource<List<Image>>>
    private val imageParentObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var id: String? = null
    @JvmField
    var newsImageList: List<Image>? = null
    @JvmField
    var imgId: String? = null
    @JvmField
    var imgType: String? = null

    //endregion
    //region Methods
    fun setImageParentId(imageType: String?, imageParentId: String?) {
        val tmpDataHolder = TmpDataHolder(imageType, imageParentId)
        imageParentObj.value = tmpDataHolder
    }

    internal class TmpDataHolder(var imgType: String?, var imageParentId: String?)  //endregion

    //endregion
    //region Constructors
    init {
        imageListLiveData = Transformations.switchMap(imageParentObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Image>>>()
            }
            repository.getImageList(obj.imgType, obj.imageParentId)
        }
    }
}