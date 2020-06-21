package com.dewonderstruck.apps.ashx0.viewmodel.contactus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.contactus.ContactUsRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 6/2/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class ContactUsViewModel @Inject internal constructor(repository: ContactUsRepository) : PSViewModel() {
    //    private final ContactUsBackgroundTaskHandler backgroundTaskHandler;
    val postContactUsData: LiveData<Resource<Boolean>>
    private val postContactUsObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var isLoading = false
    @JvmField
    var contactName = ""
    @JvmField
    var contactEmail = ""
    @JvmField
    var contactDesc = ""
    @JvmField
    var contactPhone = ""
    fun postContactUs(apiKey: String?, contactName: String?, contactEmail: String?, contactDesc: String?, contactPhone: String?) {
        setLoadingState(true)
        val holder = TmpDataHolder(contactName!!, contactEmail!!, contactDesc!!, contactPhone!!)
        postContactUsObj.value = holder
    }

    //region Holder
    internal inner class TmpDataHolder(contactName: String, contactEmail: String, contactDesc: String, contactPhone: String) {
        var contactName = ""
        var contactEmail = ""
        var contactDesc = ""
        var contactPhone = ""
        var isConnected = false

        init {
            this.contactName = contactName
            this.contactEmail = contactEmail
            this.contactDesc = contactDesc
            this.contactPhone = contactPhone
        }
    } //endregion

    init {
        Utils.psLog("Inside ContactUsViewModel")
        postContactUsData = Transformations.switchMap(postContactUsObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Contact Us.")
            repository.postContactUs(Config.API_KEY, obj.contactName, obj.contactEmail, obj.contactDesc, obj.contactPhone)
        }
    }
}