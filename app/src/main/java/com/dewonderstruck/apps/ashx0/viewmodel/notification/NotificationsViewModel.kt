package com.dewonderstruck.apps.ashx0.viewmodel.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.notification.NotificationRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Noti
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(notificationRepository: NotificationRepository) : PSViewModel() {
    //for recent comment list
    val notificationListData: LiveData<Resource<List<Noti>>>
    private val notificationListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()
    val notificationDetailData: LiveData<Resource<Noti>>
    private val notificationDetailObj = MutableLiveData<TmpDataHolder>()
    val notiReadData: LiveData<Resource<Boolean>>
    private val notiPostObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var token = ""
    @JvmField
    var notiId = ""

    //endregion
    //region Getter And Setter for Comment List
    fun setNotificationListObj(userId: String, deviceToken: String, limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.userId = userId
            tmpDataHolder.deviceToken = deviceToken
            notificationListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //Get Comment Next Page
    fun setNextPageLoadingStateObj(userId: String, deviceToken: String, limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.userId = userId
            tmpDataHolder.deviceToken = deviceToken
            nextPageLoadingStateObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Getter And Setter for product detail List
    fun setNotificationDetailObj(notificationId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.notificationId = notificationId
            notificationDetailObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Getter And Setter for noti post
    fun setNotiReadObj(notificationId: String, userId: String, deviceToken: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.notificationId = notificationId
        tmpDataHolder.userId = userId
        tmpDataHolder.deviceToken = deviceToken
        notiPostObj.value = tmpDataHolder

        // start loading
        setLoadingState(true)
    }

    //endregion
    internal inner class TmpDataHolder {
        var limit = ""
        var offset = ""
        var isConnected = false
        var notificationId = ""
        var userId = ""
        var deviceToken = ""
    }

    init {
        notificationListData = Transformations.switchMap(notificationListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Noti>>>()
            }
            Utils.psLog("Notification List.")
            notificationRepository.getNotificationList(Config.API_KEY, obj.userId, obj.limit, obj.offset, obj.deviceToken)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Notification List.")
            notificationRepository.getNextPageNotificationList(obj.userId, obj.deviceToken, obj.limit, obj.offset)
        }
        notificationDetailData = Transformations.switchMap(notificationDetailObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Noti>>()
            }
            Utils.psLog("Notification detail List.")
            notificationRepository.getNotificationDetail(Config.API_KEY, obj.notificationId)
        }
        notiReadData = Transformations.switchMap(notiPostObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Notification detail List.")
            notificationRepository.uploadNotiPostToServer(obj.notificationId, obj.userId, obj.deviceToken)
        }
    }
}