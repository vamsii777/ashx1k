package com.dewonderstruck.apps.ashx0.ui.setting

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.DiskCache
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentSettingBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.GetSizeTaskForGlide
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.facebook.login.LoginManager
import com.google.ads.consent.ConsentForm
import com.google.ads.consent.ConsentFormListener
import com.google.ads.consent.ConsentStatus
import java.io.File
import java.net.MalformedURLException
import java.net.URL

class SettingFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentSettingBinding>? = null
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var form: ConsentForm? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get().notificationSettingTextView.text = binding!!.get().notificationSettingTextView.text.toString()
        binding!!.get().editProfileTextView.text = binding!!.get().editProfileTextView.text.toString()
        binding!!.get().logOutTextView.text = binding!!.get().logOutTextView.text.toString()
        binding!!.get().appInfoTextView.text = binding!!.get().appInfoTextView.text.toString()
        binding!!.get().notificationSettingTextView.setOnClickListener { view: View? -> navigationController.navigateToNotificationSettingActivity(activity) }
        binding!!.get().changeLanguageImageView.setOnClickListener { view: View? -> navigationController.navigateToNotificationSettingActivity(activity) }
        binding!!.get().editProfileTextView.setOnClickListener { view: View? -> navigationController.navigateToEditProfileActivity(activity) }
        binding!!.get().editProfileImageView.setOnClickListener { view: View? -> navigationController.navigateToEditProfileActivity(activity) }
        binding!!.get().appInfoTextView.setOnClickListener { view: View? -> navigationController.navigateToAppInfoActivity(activity) }
        binding!!.get().appInfoImageView.setOnClickListener { view: View? -> navigationController.navigateToAppInfoActivity(activity) }
        binding!!.get().logOutTextView.setOnClickListener { v: View? ->
            psDialogMsg!!.showConfirmDialog(getString(R.string.edit_setting__logout_question), getString(R.string.app__ok), getString(R.string.app__cancel))
            psDialogMsg!!.show()
            psDialogMsg!!.okButton.setOnClickListener { view: View? ->
                psDialogMsg!!.cancel()
                logout()
            }
            psDialogMsg!!.cancelButton.setOnClickListener { view: View? -> psDialogMsg!!.cancel() }
        }
        if (loginUserId == "" || !userIdToVerify.isEmpty()) {
            hideUIForLogout()
        }
        if (context != null) {
            GetSizeTaskForGlide(binding!!.get().cacheValueTextViewDesc).execute(File(requireContext().cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR))
        }
        binding!!.get().clearCacheTextView.setOnClickListener { v: View? ->
            psDialogMsg!!.showConfirmDialog(getString(R.string.setting__clear_cache_confirm), getString(R.string.app__ok), getString(R.string.message__cancel_close))
            psDialogMsg!!.show()
            psDialogMsg!!.okButton.setOnClickListener { v12: View? ->
                ClearCacheAsync().execute()
                if (activity != null) {
                    Glide.get(requireActivity()).clearMemory()
                }
                psDialogMsg!!.cancel()
            }
            psDialogMsg!!.cancelButton.setOnClickListener { v1: View? -> psDialogMsg!!.cancel() }
        }
        binding!!.get().gdprTextView.setOnClickListener { v: View? -> collectConsent() }
        binding!!.get().appVersionTextView.text = String.format("%s%s%s", getString(R.string.setting__version_no), " ", Config.APP_VERSION)
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        val consentStatusIsReady = pref.getBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false)
        if (consentStatusIsReady) {
            binding!!.get().gdprTextView.visibility = View.VISIBLE
        } else {
            binding!!.get().gdprTextView.visibility = View.GONE
        }
        userViewModel!!.setLoginUser()
        userViewModel!!.loginUser.observe(this, Observer { data: List<UserLogin>? ->
            if (data != null) {
                if (data.size > 0) {
                    userViewModel!!.user = data[0].user
                }
            }
        })
    }

    private fun hideUIForLogout() {
        binding!!.get().editProfileTextView.visibility = View.GONE
        binding!!.get().editProfileImageView.visibility = View.GONE
        binding!!.get().view16.visibility = View.GONE
        binding!!.get().logOutTextView.visibility = View.GONE
    }

    private fun logout() {
        userViewModel!!.deleteUserLogin(userViewModel!!.user).observe(this, Observer { status: Resource<Boolean?>? ->
            if (status != null) {
                LoginManager.getInstance().logOut()
                hideUIForLogout()
                navigationController.navigateBackToProfileFragment(this.activity)
                if (activity != null) {
                    if (activity !is MainActivity) {
                        requireActivity().finish()
                    }
                }
            }
        })
    }

    internal inner class ClearCacheAsync : AsyncTask<Void?, Void?, Boolean>() {
        protected override fun doInBackground(vararg p0: Void?): Boolean? {
            if (activity != null) {
                val glide = Glide.get(requireActivity().applicationContext)
                glide.clearDiskCache()
            }
            return true
        }

        override fun onPostExecute(aBoolean: Boolean) {
            super.onPostExecute(aBoolean)
            if (context != null) {
                GetSizeTaskForGlide(binding!!.get().cacheValueTextViewDesc).execute(File(context!!.cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR))
            }
            Toast.makeText(activity, getString(R.string.success), Toast.LENGTH_SHORT).show()
        }
    }

    private fun collectConsent() {
        var privacyUrl: URL? = null
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = URL(Config.POLICY_URL)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            // Handle error.
        }
        form = ConsentForm.Builder(this.context, privacyUrl)
                .withListener(object : ConsentFormListener() {
                    override fun onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Utils.psLog("Form loaded")
                        if (form != null) {
                            form!!.show()
                        }
                    }

                    override fun onConsentFormOpened() {
                        // Consent form was displayed.
                        Utils.psLog("Form Opened")
                    }

                    override fun onConsentFormClosed(
                            consentStatus: ConsentStatus, userPrefersAdFree: Boolean) {
                        // Consent form was closed.
                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name).apply()
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply()
                        Utils.psLog("Form Closed")
                    }

                    override fun onConsentFormError(errorDescription: String) {
                        // Consent form error.
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply()
                        Utils.psLog("Form Error $errorDescription")
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build()
        form!!.load()
    }
}