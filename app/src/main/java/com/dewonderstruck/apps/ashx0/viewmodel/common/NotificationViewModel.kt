package com.dewonderstruck.apps.ashx0.viewmodel.common

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dewonderstruck.apps.ashx0.repository.aboutus.AboutUsRepository
import com.dewonderstruck.apps.ashx0.ui.common.NotificationTaskHandler
import com.dewonderstruck.apps.ashx0.utils.Utils
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 1/4/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class NotificationViewModel @Inject internal constructor(repository: AboutUsRepository?) : ViewModel() {
    private val backgroundTaskHandler: NotificationTaskHandler
    @JvmField
    var pushNotificationSetting = false
    @JvmField
    var isLoading = false
    fun registerNotification(context: Context?, platform: String?, token: String?) {
        if (token == null || platform == null) return
        if (platform == "") return
        backgroundTaskHandler.registerNotification(context, platform, token)
    }

    fun unregisterNotification(context: Context?, platform: String?, token: String?) {
        if (token == null || platform == null) return
        if (platform == "") return
        backgroundTaskHandler.unregisterNotification(context, platform, token)
    }

    val loadingStatus: LiveData<LoadingState>
        get() = backgroundTaskHandler.loadingState

    init {
        Utils.psLog("Inside NewsViewModel")
        backgroundTaskHandler = NotificationTaskHandler(repository)
    }
}