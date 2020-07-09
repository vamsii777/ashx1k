package com.dewonderstruck.apps.ashx0.ui.user.phonelogin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentPhoneLoginBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

class PhoneLoginFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentPhoneLoginBinding>? = null
    var prgDialog: AutoClearedValue<ProgressDialog>? = null
    private var callbackManager: CallbackManager? = null
    private var number: String? = null
    private var userName: String? = null

    //Firebase test
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: AuthStateListener? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS)
        FacebookSdk.sdkInitialize(context)
        callbackManager = CallbackManager.Factory.create()

        // Inflate the layout for this fragment
        val dataBinding: FragmentPhoneLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_login, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    //firebase
    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.addAuthStateListener(mAuthListener!!)
        }
    }

    override fun initUIAndActions() {
        dataBindingComponent.fragmentBindingAdapters.bindFullImageDrawbale(binding!!.get().bgImageView, resources.getDrawable(R.drawable.hotel_cell_background))
        if (activity is MainActivity) {
            (activity as MainActivity?)!!.setToolbarText((activity as MainActivity?)!!.binding!!.toolbar, getString(R.string.login__login))
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            (this.activity as MainActivity?)!!.binding!!.toolbar.setBackgroundColor(resources.getColor(R.color.global__primary))
            //            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
//            ((MainActivity) getActivity()).updateMenuIconWhite();
//            ((MainActivity) getActivity()).refreshPSCount();
        }
        psDialogMsg = PSDialogMsg(activity, false)

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
        prgDialog!!.get().setCancelable(false)

        //fadeIn Animation
        fadeIn(binding!!.get().root)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = AuthStateListener { firebaseAuth: FirebaseAuth? -> }
        //end
        binding!!.get().backToLoginTextView.setOnClickListener { v: View? -> Utils.navigateAfterLogin(this@PhoneLoginFragment.activity, navigationController) }
        binding!!.get().addPhoneButton.setOnClickListener { v: View? ->
            if (binding!!.get().nameEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok))
                psDialogMsg!!.show()
            } else if (binding!!.get().phoneEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_phone_no), getString(R.string.app__ok))
                psDialogMsg!!.show()
            } else {
                number = binding!!.get().phoneEditText.text.toString()
                userName = binding!!.get().nameEditText.text.toString()
                validNo(number!!)
                Utils.navigateAfterPhoneVerify(activity, navigationController, number, userName)
                Toast.makeText(this@PhoneLoginFragment.activity, number, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun initViewModels() {}
    override fun initAdapters() {}
    override fun initData() {}
    private fun validNo(no: String) {
        if (no.isEmpty() || no.length < 10) {
            binding!!.get().phoneEditText.error = "Enter a valid mobile"
            binding!!.get().phoneEditText.requestFocus()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }
}