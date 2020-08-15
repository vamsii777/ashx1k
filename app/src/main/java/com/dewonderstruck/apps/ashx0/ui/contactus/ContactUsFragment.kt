package com.dewonderstruck.apps.ashx0.ui.contactus

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
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentContactUsBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.contactus.ContactUsViewModel
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class ContactUsFragment : DeFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var contactUsViewModel: ContactUsViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentContactUsBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentContactUsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        psDialogMsg = PSDialogMsg(this.activity, false)

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
        prgDialog!!.get().setCancelable(false)

        //fadeIn Animation
        fadeIn(binding!!.get().root)
        binding!!.get().btnSubmit.setOnClickListener { view: View? ->
            if (connectivity.isConnected) {
                val contactName = binding!!.get().contactNameTextInput.text.toString().trim { it <= ' ' }
                val contactEmail = binding!!.get().contactEmailTextInput.text.toString().trim { it <= ' ' }
                val contactDesc = binding!!.get().contactDescEditText.text.toString().trim { it <= ' ' }
                val contactPhone = binding!!.get().contactPhoneTextInput.text.toString().trim { it <= ' ' }
                if (contactName == "") {
                    psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                    return@setOnClickListener
                }
                if (contactEmail == "") {
                    psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                    return@setOnClickListener
                }
                if (contactDesc == "") {
                    psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_contact_message), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                    return@setOnClickListener
                }
                if (!contactUsViewModel!!.isLoading) {
                    contactUsViewModel!!.contactName = contactName
                    contactUsViewModel!!.contactEmail = contactEmail
                    contactUsViewModel!!.contactDesc = contactDesc
                    contactUsViewModel!!.contactPhone = contactPhone
                    doSubmit()
                }
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
    }

    override fun initViewModels() {
        contactUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactUsViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {}

    //endregion
    //region Methods
    private fun doSubmit() {
        prgDialog!!.get().show()
        contactUsViewModel!!.postContactUs(Config.API_KEY, contactUsViewModel!!.contactName, contactUsViewModel!!.contactEmail, contactUsViewModel!!.contactDesc, contactUsViewModel!!.contactPhone)
        contactUsViewModel!!.postContactUsData.observe(this, Observer { state: Resource<Boolean?>? ->
            if (state != null && state.status == Status.SUCCESS) {
                //Success
                binding!!.get().contactNameTextInput.setText("")
                binding!!.get().contactEmailTextInput.setText("")
                binding!!.get().contactPhoneTextInput.setText("")
                binding!!.get().contactDescEditText.setText("")
                psDialogMsg!!.showSuccessDialog(getString(R.string.message_sent), getString(R.string.app__ok))
                psDialogMsg!!.show()
                contactUsViewModel!!.setLoadingState(false)
            }
        })
        contactUsViewModel!!.loadingState.observe(this, Observer { state: Boolean? ->
            if (state != null && state) {
                prgDialog!!.get().show()
            } else {
                prgDialog!!.get().cancel()
            }
        })
    } //endregion
}