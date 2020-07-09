package com.dewonderstruck.apps.ashx0.ui.notification.setting

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentNotificationSettingBinding
import com.dewonderstruck.apps.ashx0.ui.common.BackgroundTaskHandler.LoadingState
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.NotificationViewModel

/**
 * NotificationSettingFragment
 */
class NotificationSettingFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentNotificationSettingBinding>? = null
    private var notificationViewModel: NotificationViewModel? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentNotificationSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_setting, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)

        //fadeIn Animation
        fadeIn(binding!!.get().root)
        binding!!.get().notiSwitch.isChecked = notificationViewModel!!.pushNotificationSetting
        binding!!.get().notiSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            Utils.psLog("Checked : $b")
            if (connectivity.isConnected) {
                if (!notificationViewModel!!.isLoading) {
                    updateNotificationSetting(b)
                } else {
                    psDialogMsg!!.showInfoDialog(getString(R.string.update_noti), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                    binding!!.get().notiSwitch.isChecked = !b
                }
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
                binding!!.get().notiSwitch.isChecked = !b
            }
        }
    }

    override fun initViewModels() {
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotificationViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        try {
            if (pref != null) {
                updateNotificationMessage()
            }
        } catch (ne: NullPointerException) {
            Utils.psErrorLog("Null Pointer Exception.", ne)
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting notification flag data.", e)
        }
        notificationViewModel!!.loadingStatus.observe(this, Observer { status: LoadingState? ->
            if (status == null) {
                Utils.psLog("Status is null")
                notificationViewModel!!.isLoading = false
            } else {
                Utils.psLog("Status Update : " + status.isRunning)
                notificationViewModel!!.isLoading = status.isRunning
                val error = status.errorMessageIfNotHandled
                if (error != null) {
                    notificationViewModel!!.isLoading = false
                    Utils.psLog("Error in Status : $error")
                    psDialogMsg!!.showErrorDialog(error, getString(R.string.app__ok))
                    psDialogMsg!!.show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (pref != null) {
            updateNotificationMessage()
        }
    }

    private fun updateNotificationMessage() {
        notificationViewModel!!.pushNotificationSetting = pref.getBoolean(Constants.NOTI_SETTING, false)
        binding!!.get().notiSwitch.isChecked = notificationViewModel!!.pushNotificationSetting
        val message = pref.getString(Constants.NOTI_MSG, "")
        if (message != "") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding!!.get().messageTextView.text = Html.fromHtml(message, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
            } else {
                binding!!.get().messageTextView.text = Html.fromHtml(message)
            }
        }
    }

    @VisibleForTesting
    private fun updateNotificationSetting(setting: Boolean) {
        if (activity != null) {
            if (notificationViewModel!!.pushNotificationSetting != setting) {
                if (setting) {
                    notificationViewModel!!.registerNotification(context, Constants.PLATFORM, "")
                } else {
                    notificationViewModel!!.unregisterNotification(context, Constants.PLATFORM, "")
                }
                notificationViewModel!!.pushNotificationSetting = setting
            }
        }
    } //endregion
}