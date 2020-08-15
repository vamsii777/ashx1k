package com.dewonderstruck.apps.ashx0.ui.notification.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentNotificationBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.notification.NotificationsViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Noti
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

class NotificationFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var notificationViewModel: NotificationsViewModel? = null
    private var notiId: String? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentNotificationBinding>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentNotificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
    }

    override fun initViewModels() {
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotificationsViewModel::class.java)
    }

    override fun initAdapters() {}
    @SuppressLint("UseRequireInsteadOfGet")
    override fun initData() {
        try {
            if (activity != null) {
                if (activity!!.intent.extras != null) {
                    val NOTI_ID_KEY = Constants.NOTI_ID
                    notiId = activity!!.intent.extras!!.getString(NOTI_ID_KEY)
                }
            }
        } catch (e: Exception) {
            Utils.psErrorLog("", e)
        }
        try {
            if (activity != null) {
                if (activity!!.intent.extras != null) {
                    val TOKEN_KEY = Constants.NOTI_TOKEN
                    notificationViewModel!!.token = activity!!.intent.extras!!.getString(TOKEN_KEY)
                }
            }
        } catch (e: Exception) {
            Utils.psErrorLog("", e)
        }
        notificationViewModel!!.setNotificationDetailObj(notiId)
        val notificationDetail = notificationViewModel!!.notificationDetailData
        notificationDetail?.observe(this, Observer { listResource: Resource<Noti?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get().root)

                            // Update the data
                            replaceNotificationDetailData(listResource.data)
                            if (listResource.data.isRead == Constants.ZERO) {
                                sendNotiReadPostData()
                            }
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceNotificationDetailData(listResource.data)
                        }
                        notificationViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        notificationViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {
                notificationViewModel!!.setLoadingState(false)
                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })

        //getter and setter notification post method
        notificationViewModel!!.notiReadData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this@NotificationFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                        notificationViewModel!!.setLoadingState(false)
                    }
                } else if (result.status == Status.ERROR) {
                    if (this@NotificationFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                        notificationViewModel!!.setLoadingState(false)
                    }
                }
            }
        })
    }

    //send favourite data
    private fun sendNotiReadPostData() {


        // Don't call to server when both loginUserId and Token are blank.
        if (!(loginUserId == "" && notificationViewModel!!.token.trim { it <= ' ' } == "")) {
            notificationViewModel!!.setNotiReadObj(notiId, loginUserId, notificationViewModel!!.token)
        } else {
            Utils.psLog("No Call")
        }
    }

    private fun replaceNotificationDetailData(noti: Noti?) {
        binding!!.get().notification = noti
    }
}