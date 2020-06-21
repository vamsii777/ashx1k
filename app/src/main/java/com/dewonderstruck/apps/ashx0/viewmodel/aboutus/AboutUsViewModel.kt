package com.dewonderstruck.apps.ashx0.viewmodel.aboutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.aboutus.AboutUsRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 12/30/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class AboutUsViewModel @Inject internal constructor(repository: AboutUsRepository) : PSViewModel() {
    //endregion
    //region Variables
    // Get AboutUs
    val aboutUsData: LiveData<Resource<AboutUs>>
    private val aboutUsObj = MutableLiveData<String>()
    var aboutId: String? = null

    //endregion
    //region Public Methods
    fun setAboutUsObj(aboutUsObj: String) {
        this.aboutUsObj.value = aboutUsObj
    }

    //endregion
    //region Constructors
    init {
        aboutUsData = Transformations.switchMap(aboutUsObj) { newsId: String ->
            if (newsId.isEmpty()) {
                return@switchMap AbsentLiveData.create<Resource<AboutUs>>()
            }
            repository.getAboutUs(Config.API_KEY)
        }
    }
}