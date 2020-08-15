package com.dewonderstruck.apps.ashx0.ui.user

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentUserForgotPasswordBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

/**
 * UserForgotPasswordFragment
 */
class UserForgotPasswordFragment : DeFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentUserForgotPasswordBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentUserForgotPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_forgot_password, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        dataBindingComponent.fragmentBindingAdapters.bindFullImageDrawbale(binding!!.get().bgImageView, resources.getDrawable(R.drawable.hotel_cell_background))
        psDialogMsg = PSDialogMsg(activity, false)

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
        prgDialog!!.get().setCancelable(false)


        //fadeIn Animation
        fadeIn(binding!!.get().root)
        binding!!.get().loginButton.setOnClickListener { view: View? -> Utils.navigateToLogin(this@UserForgotPasswordFragment.activity, navigationController) }
        binding!!.get().forgotPasswordButton.setOnClickListener { view: View? ->
            if (connectivity.isConnected) {
                forgotPassword()
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
    }

    override fun initViewModels() {
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {}

    //endregion
    //region Private Methods
    @SuppressLint("SetTextI18n")
    private fun updateForgotBtnStatus() {
        if (userViewModel!!.isLoading) {
            binding!!.get().forgotPasswordButton.text = resources.getString(R.string.message__loading)
        } else {
            binding!!.get().forgotPasswordButton.text = "Forgot Password"
        }
    }

    private fun forgotPassword() {
        Utils.hideKeyboard(activity)
        val email = binding!!.get().emailEditText.text.toString().trim { it <= ' ' }
        if (email == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        userViewModel!!.isLoading = true
        userViewModel!!.forgotPassword(email).observe(this, Observer { listResource: Resource<ApiStatus?>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING -> {
                        // Loading State
                        // Data are from Local DB
                        prgDialog!!.get().show()
                        updateForgotBtnStatus()
                    }
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            psDialogMsg!!.showSuccessDialog(listResource.data.message, getString(R.string.app__ok))
                            psDialogMsg!!.show()
                            userViewModel!!.isLoading = false
                            prgDialog!!.get().cancel()
                            updateForgotBtnStatus()
                        }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        binding!!.get().forgotPasswordButton.text = resources.getString(R.string.forgot_password__title)
                        userViewModel!!.isLoading = false
                        prgDialog!!.get().cancel()
                    }
                    else -> {
                        // Default
                        userViewModel!!.isLoading = false
                        prgDialog!!.get().cancel()
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
    } //endregion
}