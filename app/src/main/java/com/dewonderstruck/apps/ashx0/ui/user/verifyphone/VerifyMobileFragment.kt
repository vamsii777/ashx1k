package com.dewonderstruck.apps.ashx0.ui.user.verifyphone

import android.app.ProgressDialog
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
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentVerifyMobileBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class VerifyMobileFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var userPhoneNo: String? = null
    private var userName: String? = null
    private var phoneUserId: String? = null
    private var token: String? = null
    private var mAuth: FirebaseAuth? = null
    private var mVerificationId: String? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentVerifyMobileBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val dataBinding: FragmentVerifyMobileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_mobile, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        if (activity != null && activity!!.intent != null) {
            userPhoneNo = activity!!.intent.getStringExtra(Constants.USER_PHONE)
            userName = activity!!.intent.getStringExtra(Constants.USER_NAME)
            if (arguments != null) {
                userPhoneNo = arguments!!.getString(Constants.USER_PHONE)
                userName = arguments!!.getString(Constants.USER_NAME)
            }
        }
        sendVerificationCode(userPhoneNo)
        return binding!!.get().root
    }

    private fun sendVerificationCode(no: String?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                no!!,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks)
    }

    private val mCallbacks: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            //Getting the code sent by SMS
            val code = phoneAuthCredential.smsCode

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                if (binding!!.get().enterCodeEditText != null) {
                    binding!!.get().enterCodeEditText.setText(code)
                }
                //verifying the code
                verifyVerificationCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)

            //storing the verification id that is sent to the user
            mVerificationId = s
        }
    }

    override fun initUIAndActions() {
        if (activity is MainActivity) {
            (activity as MainActivity?)!!.setToolbarText((activity as MainActivity?)!!.binding!!.toolbar, getString(R.string.verify_phone__title))
            (this.activity as MainActivity?)!!.binding!!.toolbar.setBackgroundColor(resources.getColor(R.color.global__primary))
            //            ((MainActivity) getActivity()).updateMenuIconWhite();
//            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        psDialogMsg = PSDialogMsg(activity, false)
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
        prgDialog!!.get().setCancelable(false)
        binding!!.get().phoneProfile.text = userPhoneNo
        binding!!.get().submitButton.setOnClickListener { v: View? ->
            val code = binding!!.get().enterCodeEditText.text.toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                binding!!.get().enterCodeEditText.error = "Enter valid code"
                binding!!.get().enterCodeEditText.requestFocus()
                return@setOnClickListener
            }

//                verifying the code entered manually
            verifyVerificationCode(code)
        }
    }

    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        if (activity != null) {
            mAuth!!.signInWithCredential(credential)
                    .addOnCompleteListener(activity!!) { task: Task<AuthResult?> ->
                        if (task.isSuccessful) {
                            try {
                                //verification successful we will start the profile activity
                                if (task.result != null && task.result!!.user != null) {
                                    phoneUserId = task.result!!.user!!.uid
                                    userViewModel!!.setPhoneLoginUser(phoneUserId, userName, userPhoneNo, token)
                                }
                            } catch (e1: Exception) {
                                // Error occurred while creating the File
                                Toast.makeText(context, e1.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN)
        userViewModel!!.phoneLoginData.observe(this, Observer { listResource: Resource<UserLogin?>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        prgDialog!!.get().show()
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            try {
                                Utils.updateUserLoginData(pref, listResource.data.user)
                                Utils.navigateAfterUserLogin(activity, navigationController)
                            } catch (ne: NullPointerException) {
                                Utils.psErrorLog("Null Pointer Exception.", ne)
                            } catch (e: Exception) {
                                Utils.psErrorLog("Error in getting notification flag data.", e)
                            }
                            userViewModel!!.isLoading = false
                            prgDialog!!.get().cancel()
                        }
                    Status.ERROR -> {
                        // Error State
                        userViewModel!!.isLoading = false
                        prgDialog!!.get().cancel()
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
    }
}