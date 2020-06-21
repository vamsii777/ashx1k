package com.dewonderstruck.apps.ashx0.ui.common

import android.content.Context
import com.dewonderstruck.apps.ashx0.repository.aboutus.AboutUsRepository
import com.dewonderstruck.apps.ashx0.utils.Utils

/**
 * Created by Vamsi Madduluri on 12/5/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class NotificationTaskHandler(override var repository: AboutUsRepository) : BackgroundTaskHandler() {
    fun registerNotification(context: Context?, platform: String?, token: String?) {
        if (platform == null) return
        if (platform == "") return
        Utils.psLog("Register Notification : Notification Handler")
        holdLiveData = repository.registerNotification(context, platform, token)
        loadingState.setValue(LoadingState(true, null))
        holdLiveData!!.observeForever(this)
    }

    fun unregisterNotification(context: Context?, platform: String?, token: String?) {
        if (platform == null) return
        if (platform == "") return
        Utils.psLog("Unregister Notification : Notification Handler")
        holdLiveData = repository.unregisterNotification(context, platform, token)
        loadingState.setValue(LoadingState(true, null))
        holdLiveData!!.observeForever(this)
    }

}