package com.dewonderstruck.apps.ashx0.ui.user

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentPasswordChangeBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * PasswordChangeFragment
 */

class PasswordChangeFragment : DeFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentPasswordChangeBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentPasswordChangeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_password_change, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.ROBOTO))
        prgDialog!!.get().setCancelable(false)

        //fadeIn Animation
        fadeIn(binding!!.get().root)
        binding!!.get().saveButton.setOnClickListener { view: View? -> passwordUpdate() }
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {}

    //endregion
    //region Private Methods
    private fun updateForgotBtnStatus() {
        if (userViewModel!!.isLoading) {
            binding!!.get().saveButton.text = resources.getString(R.string.message__loading)
        } else {
            binding!!.get().saveButton.text = resources.getString(R.string.password_change__save)
        }
    }

    private fun passwordUpdate() {
        Utils.hideKeyboard(activity)
        if (!connectivity.isConnected) {
            psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        val password = binding!!.get().passwordEditText.text.toString().trim { it <= ' ' }
        val confirmPassword = binding!!.get().confirmPasswordEditText.text.toString().trim { it <= ' ' }
        if (password == "" || confirmPassword == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        if (password != confirmPassword) {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__password_not_equal), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        userViewModel!!.isLoading = true
        userViewModel!!.passwordUpdate(loginUserId, password).observe(this, Observer { listResource: Resource<ApiStatus?>? ->
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
                            prgDialog!!.get().cancel()
                            userViewModel!!.isLoading = false
                            updateForgotBtnStatus()
                        }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        binding!!.get().saveButton.text = resources.getString(R.string.password_change__save)
                        userViewModel!!.isLoading = false
                    }
                    else ->                         // Default
                        userViewModel!!.isLoading = false
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
    } //endregion
}