package com.dewonderstruck.apps.ashx0.ui.notification.list

import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import androidx.databinding.DataBindingComponent
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.viewmodel.notification.NotificationsViewModel
import com.dewonderstruck.apps.ashx0.ui.notification.list.adapter.NotificationListAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.ui.notification.list.adapter.NotificationListAdapter.NotificationClickCallback
import com.dewonderstruck.apps.ashx0.viewobject.Noti
import androidx.lifecycle.LiveData
import android.content.Intent
import androidx.lifecycle.ViewModelProvider

class NotificationListFragment constructor() : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent? = FragmentDataBindingComponent(this)
    private var notificationListViewModel: NotificationsViewModel? = null
    var nvAdapter: NotificationListAdapter? = null

    @androidx.annotation.VisibleForTesting
    private var binding: AutoClearedValue<com.dewonderstruck.apps.ashx0.databinding.FragmentNotificationListBinding?>? = null
    private var adapter: AutoClearedValue<NotificationListAdapter?>? = null
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): android.view.View? {
        var dataBinding: com.dewonderstruck.apps.ashx0.databinding.FragmentNotificationListBinding? = DataBindingUtil.inflate(inflater, com.dewonderstruck.apps.ashx0.R.layout.fragment_notification_list, container, false, dataBindingComponent)
        binding = AutoClearedValue<com.dewonderstruck.apps.ashx0.databinding.FragmentNotificationListBinding?>(this, dataBinding)
        binding!!.get()!!.setLoadingMore(connectivity.isConnected())
        return binding!!.get()!!.getRoot()
    }

    public override fun onDispatched(): kotlin.Unit {
        if (notificationListViewModel!!.loadingDirection == com.dewonderstruck.apps.ashx0.utils.Utils.LoadingDirection.top) {
            if (binding!!.get()!!.notificationList != null) {
                var layoutManager: LinearLayoutManager? = binding!!.get()!!.notificationList.getLayoutManager() as LinearLayoutManager?
                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0)
                }
            }
        }
    }

    protected override fun initUIAndActions(): kotlin.Unit {
        if (com.dewonderstruck.apps.Config.SHOW_ADMOB && connectivity.isConnected()) {
            var adRequest: com.google.android.gms.ads.AdRequest? = com.google.android.gms.ads.AdRequest.Builder()
                    .build()
            binding!!.get()!!.adView.loadAd(adRequest)
        } else {
            binding!!.get()!!.adView.setVisibility(android.view.View.GONE)
        }
        binding!!.get()!!.notificationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            public override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int): kotlin.Unit {
                var layoutManager: LinearLayoutManager? = recyclerView.getLayoutManager() as LinearLayoutManager?
                if (layoutManager != null) {
                    var lastPosition: Int = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get()!!.getItemCount() - 1) {
                        if (!binding!!.get()!!.getLoadingMore() && !notificationListViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected()) {
                                notificationListViewModel!!.loadingDirection = com.dewonderstruck.apps.ashx0.utils.Utils.LoadingDirection.bottom
                                var limit: Int = com.dewonderstruck.apps.Config.NOTI_LIST_COUNT
                                notificationListViewModel!!.offset = notificationListViewModel!!.offset + limit
                                var deviceToken: kotlin.String? = pref.getString(com.dewonderstruck.apps.ashx0.utils.Constants.NOTI_TOKEN, "")
                                notificationListViewModel!!.setNextPageLoadingStateObj(loginUserId, deviceToken, com.dewonderstruck.apps.Config.NOTI_LIST_COUNT.toString(), notificationListViewModel!!.offset.toString())
                            }
                        }
                    }
                }
            }
        })
        binding!!.get()!!.swipeRefresh.setColorSchemeColors(getResources().getColor(com.dewonderstruck.apps.ashx0.R.color.view__primary_line))
        binding!!.get()!!.swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(com.dewonderstruck.apps.ashx0.R.color.global__primary))
        binding!!.get()!!.swipeRefresh.setOnRefreshListener(OnRefreshListener({
            notificationListViewModel!!.loadingDirection = com.dewonderstruck.apps.ashx0.utils.Utils.LoadingDirection.top

            // reset productViewModel.offset
            notificationListViewModel!!.offset = 0

            // reset productViewModel.forceEndLoading
            notificationListViewModel!!.forceEndLoading = false
            var deviceToken: kotlin.String? = pref.getString(com.dewonderstruck.apps.ashx0.utils.Constants.NOTI_TOKEN, "")

            // update live data
            notificationListViewModel!!.setNotificationListObj(loginUserId, deviceToken, com.dewonderstruck.apps.Config.NOTI_LIST_COUNT.toString(), notificationListViewModel!!.offset.toString())
        }))
    }

    protected override fun initViewModels(): kotlin.Unit {
        notificationListViewModel = ViewModelProvider(this, viewModelFactory).get<NotificationsViewModel>(NotificationsViewModel::class.java)
    }

    protected override fun initAdapters(): kotlin.Unit {
        nvAdapter = NotificationListAdapter(this!!.dataBindingComponent!!, object:  NotificationClickCallback{
            override fun onClick(notification: Noti?) {
                navigationController.navigateToNotificationDetail(getActivity(), notification, notificationListViewModel!!.token)
            }

        }, this)
        this.adapter = AutoClearedValue<NotificationListAdapter?>(this, nvAdapter)
        binding!!.get()!!.notificationList.setAdapter(nvAdapter)
    }

    protected override fun initData(): kotlin.Unit {
        LoadData()
        try {
            com.dewonderstruck.apps.ashx0.utils.Utils.psLog(">>>> On initData.")
            notificationListViewModel!!.token = pref.getString(com.dewonderstruck.apps.ashx0.utils.Constants.NOTI_TOKEN, "")
        } catch (ne: java.lang.NullPointerException) {
            com.dewonderstruck.apps.ashx0.utils.Utils.psErrorLog("Null Pointer Exception.", ne)
        } catch (e: java.lang.Exception) {
            com.dewonderstruck.apps.ashx0.utils.Utils.psErrorLog("Error in getting notification flag data.", e)
        }
    }

    private fun LoadData(): kotlin.Unit {
        var deviceToken: kotlin.String? = pref.getString(com.dewonderstruck.apps.ashx0.utils.Constants.NOTI_TOKEN, "")
        notificationListViewModel!!.setNotificationListObj(loginUserId, deviceToken, com.dewonderstruck.apps.Config.NOTI_LIST_COUNT.toString(), notificationListViewModel!!.offset.toString())
        var news: LiveData<com.dewonderstruck.apps.ashx0.viewobject.common.Resource<kotlin.collections.MutableList<Noti?>?>?>? = notificationListViewModel!!.getNotificationListData()
        if (news != null) {
            news.observe(this, androidx.lifecycle.Observer<com.dewonderstruck.apps.ashx0.viewobject.common.Resource<kotlin.collections.MutableList<Noti?>?>?> { listResource: com.dewonderstruck.apps.ashx0.viewobject.common.Resource<kotlin.collections.MutableList<Noti?>?>? ->
                if (listResource != null) {
                    when (listResource.status) {
                        com.dewonderstruck.apps.ashx0.viewobject.common.Status.LOADING ->                             // Loading State
                            // Data are from Local DB
                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding!!.get()!!.getRoot())

                                // Update the data
                                replaceData(listResource.data)
                            }
                        com.dewonderstruck.apps.ashx0.viewobject.common.Status.SUCCESS -> {
                            // Success State
                            // Data are from Server
                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data)
                            }
                            notificationListViewModel!!.setLoadingState(false)
                        }
                        com.dewonderstruck.apps.ashx0.viewobject.common.Status.ERROR ->                             // Error State
                            notificationListViewModel!!.setLoadingState(false)
                        else -> {
                        }
                    }
                } else {

                    // Init Object or Empty Data
                    com.dewonderstruck.apps.ashx0.utils.Utils.psLog("Empty Data")
                    if (notificationListViewModel!!.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        notificationListViewModel!!.forceEndLoading = true
                    }
                }
            })
        }
        notificationListViewModel!!.getNextPageLoadingStateData().observe(this, androidx.lifecycle.Observer<com.dewonderstruck.apps.ashx0.viewobject.common.Resource<kotlin.Boolean?>?> { state: com.dewonderstruck.apps.ashx0.viewobject.common.Resource<kotlin.Boolean?>? ->
            if (state != null) {
                if (state.status == com.dewonderstruck.apps.ashx0.viewobject.common.Status.ERROR) {
                    com.dewonderstruck.apps.ashx0.utils.Utils.psLog("Next Page State : " + state.data)
                    notificationListViewModel!!!!.setLoadingState(false) //hide
                    notificationListViewModel!!.forceEndLoading = true //stop
                }
            }
        })
        notificationListViewModel!!.getLoadingState().observe(this, androidx.lifecycle.Observer<kotlin.Boolean?> { loadingState: kotlin.Boolean? ->
            binding!!.get()!!.setLoadingMore(notificationListViewModel!!!!!!.isLoading)
            if (loadingState != null && !loadingState) {
                binding!!.get()!!.swipeRefresh.setRefreshing(false)
            }
        })
    }

    private fun replaceData(notificationList: kotlin.collections.MutableList<Noti?>?): kotlin.Unit {
        adapter!!.get()!!.replace(notificationList)
        binding!!.get()!!.executePendingBindings()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): kotlin.Unit {
        super.onActivityResult(requestCode, resultCode, data)
        com.dewonderstruck.apps.ashx0.utils.Utils.psLog("Request code " + requestCode)
        com.dewonderstruck.apps.ashx0.utils.Utils.psLog("Result code " + resultCode)
        if ((requestCode == com.dewonderstruck.apps.ashx0.utils.Constants.REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT
                        && resultCode == com.dewonderstruck.apps.ashx0.utils.Constants.RESULT_CODE__REFRESH_NOTIFICATION)) {
            notificationListViewModel!!.notiId = data!!.getStringExtra(com.dewonderstruck.apps.ashx0.utils.Constants.NOTI_HEADER_ID)
        }
    }
}