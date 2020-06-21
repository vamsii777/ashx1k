package com.dewonderstruck.apps.ashx0.ui.apploading

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
import com.dewonderstruck.apps.ashx0.databinding.FragmentAppLoadingBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.apploading.AppLoadingViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.clearalldata.ClearAllDataViewModel
import com.dewonderstruck.apps.ashx0.viewobject.PSAppInfo
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AppLoadingFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var psDialogMsg: PSDialogMsg? = null
    private var startDate = Constants.ZERO
    private var endDate = Constants.ZERO
    private var appLoadingViewModel: AppLoadingViewModel? = null
    private var clearAllDataViewModel: ClearAllDataViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentAppLoadingBinding>? = null

    //endregion Variables
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentAppLoadingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_loading, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)

//        if (force_update) {
//            navigationController.navigateToForceUpdateActivity(this.getActivity(), force_update_title, force_update_msg);
//        }
    }

    override fun initViewModels() {
        appLoadingViewModel = ViewModelProviders.of(this, viewModelFactory).get(AppLoadingViewModel::class.java)
        clearAllDataViewModel = ViewModelProviders.of(this, viewModelFactory).get(ClearAllDataViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        if (connectivity.isConnected) {
            if (startDate == Constants.ZERO) {
                startDate = dateTime
                Utils.setDatesToShared(startDate, endDate, pref)
            }
            endDate = dateTime
            appLoadingViewModel!!.setDeleteHistoryObj(startDate, endDate)
        } else {
            navigationController.navigateToMainActivity(this@AppLoadingFragment.activity)
            if (activity != null) {
                requireActivity().finish()
            }
        }
        appLoadingViewModel!!.deleteHistoryData.observe(this, Observer { result: Resource<PSAppInfo?>? ->
            if (result != null) {
                when (result.status) {
                    Status.SUCCESS -> if (result.data != null) {
                        appLoadingViewModel!!.psAppInfo = result.data
                        checkVersionNumber(result.data)
                        startDate = endDate
                    }
                    Status.ERROR -> {
                    }
                }
            }
        })
        clearAllDataViewModel!!.deleteAllDataData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                when (result.status) {
                    Status.ERROR -> {
                    }
                    Status.SUCCESS -> checkForceUpdate(appLoadingViewModel!!.psAppInfo)
                }
            }
        })
    }

    private fun checkForceUpdate(psAppInfo: PSAppInfo) {
        if (psAppInfo.psAppVersion.versionForceUpdate == Constants.ONE) {
            pref.edit().putString(Constants.APPINFO_PREF_VERSION_NO, psAppInfo.psAppVersion.versionNo).apply()
            pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, true).apply()
            pref.edit().putString(Constants.APPINFO_FORCE_UPDATE_TITLE, psAppInfo.psAppVersion.versionTitle).apply()
            pref.edit().putString(Constants.APPINFO_FORCE_UPDATE_MSG, psAppInfo.psAppVersion.versionMessage).apply()
            navigationController.navigateToForceUpdateActivity(this.activity, psAppInfo.psAppVersion.versionTitle, psAppInfo.psAppVersion.versionMessage)
        } else if (psAppInfo.psAppVersion.versionForceUpdate == Constants.ZERO) {
            pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false).apply()
            psDialogMsg!!.showAppInfoDialog(getString(R.string.update), getString(R.string.app__cancel), psAppInfo.psAppVersion.versionTitle, psAppInfo.psAppVersion.versionMessage)
            psDialogMsg!!.show()
            psDialogMsg!!.okButton.setOnClickListener { v: View? ->
                psDialogMsg!!.cancel()
                navigationController.navigateToMainActivity(this@AppLoadingFragment.activity)
                navigationController.navigateToPlayStore(this@AppLoadingFragment.activity)
                if (activity != null) {
                    requireActivity().finish()
                }
            }
            psDialogMsg!!.cancelButton.setOnClickListener {
                psDialogMsg!!.cancel()
                navigationController.navigateToMainActivity(this@AppLoadingFragment.activity)
                if (this@AppLoadingFragment.activity != null) {
                    this@AppLoadingFragment.requireActivity().finish()
                }
            }
            psDialogMsg!!.dialog.setCancelable(false)
        } else {
            navigationController.navigateToMainActivity(this@AppLoadingFragment.activity)
            if (activity != null) {
                requireActivity().finish()
            }
        }
    }

    private fun checkVersionNumber(psAppInfo: PSAppInfo?) {
        if (Config.APP_VERSION != psAppInfo!!.psAppVersion.versionNo) {
            if (psAppInfo.psAppVersion.versionNeedClearData == Constants.ONE) {
                psDialogMsg!!.cancel()
                clearAllDataViewModel!!.setDeleteAllDataObj()
            } else {
                checkForceUpdate(appLoadingViewModel!!.psAppInfo)
            }
        } else {
            pref.edit().putBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false).apply()
            navigationController.navigateToMainActivity(this@AppLoadingFragment.activity)
            if (activity != null) {
                requireActivity().finish()
            }
        }
    }

    private val dateTime: String
        private get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA)
            val date = Date()
            return dateFormat.format(date)
        }
}