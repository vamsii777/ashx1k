package com.dewonderstruck.apps.ashx0.ui.user

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
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentUserRegisterBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

/**
 * UserRegisterFragment
 */
class UserRegisterFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var checkFlag = false

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentUserRegisterBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentUserRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_register, container, false, dataBindingComponent)
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
        binding!!.get().loginButton.setOnClickListener { view: View? ->
            if (connectivity.isConnected) {
                Utils.navigateToLogin(this@UserRegisterFragment.activity, navigationController)
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
        binding!!.get().policyAndPrivacyCheckBox.setOnClickListener {
            checkFlag = if (binding!!.get().policyAndPrivacyCheckBox.isChecked) {
//                    Toast.makeText(getContext(),"Is Check ",Toast.LENGTH_SHORT).show();
                navigationController.navigateToTermsAndConditionsActivity(activity, Constants.SHOP_TERMS)
                true
            } else {
//                    Toast.makeText(getContext(),"Not Check ",Toast.LENGTH_SHORT).show();
                false
            }
        }
        binding!!.get().registerButton.setOnClickListener {
            if (checkFlag) {
                registerUser()
            } else {
                Toast.makeText(context, "You need to check is on.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        val _email = pref.getString(Constants.USER_EMAIL_TO_VERIFY, "")
        val _password = pref.getString(Constants.USER_PASSWORD_TO_VERIFY, "")
        val _name = pref.getString(Constants.USER_NAME_TO_VERIFY, "")
        binding!!.get().emailEditText.setText(_email)
        binding!!.get().passwordEditText.setText(_password)
        binding!!.get().nameEditText.setText(_name)
        userViewModel!!.registerUser.observe(this, Observer<Resource<User>> { listResource: Resource<User>? ->
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
                                Utils.registerUserLoginData(pref, listResource.data, binding!!.get().passwordEditText.text.toString())
                                Utils.navigateAfterUserRegister(activity, navigationController)
                            } catch (ne: NullPointerException) {
                                Utils.psErrorLog("Null Pointer Exception.", ne)
                            } catch (e: Exception) {
                                Utils.psErrorLog("Error in getting notification flag data.", e)
                            }
                            userViewModel!!.isLoading = false
                            prgDialog!!.get().cancel()
                            updateRegisterBtnStatus()
                        }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showWarningDialog(listResource.message, getString(R.string.app__ok))
                        binding!!.get().registerButton.text = resources.getString(R.string.register__register)
                        psDialogMsg!!.show()
                        userViewModel!!.isLoading = false
                        prgDialog!!.get().cancel()
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null) {
                Utils.psLog("Got Data Of About Us.")
            } else {
                Utils.psLog("No Data of About Us.")
            }
        })
    }

    //endregion
    //region Private Methods
    private fun updateRegisterBtnStatus() {
        if (userViewModel!!.isLoading) {
            binding!!.get().registerButton.text = resources.getString(R.string.message__loading)
        } else {
            binding!!.get().registerButton.text = resources.getString(R.string.register__register)
        }
    }

    private fun registerUser() {
        Utils.hideKeyboard(activity)
        val userName = binding!!.get().nameEditText.text.toString().trim { it <= ' ' }
        if (userName == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        val userEmail = binding!!.get().emailEditText.text.toString().trim { it <= ' ' }
        if (userEmail == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        val userPassword = binding!!.get().passwordEditText.text.toString().trim { it <= ' ' }
        if (userPassword == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        userViewModel!!.isLoading = true
        updateRegisterBtnStatus()

//        userViewModel.setRegisterUser(new User());
        val token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN)
        userViewModel!!.setRegisterUser(User(
                "",
                "",
                "",
                "",
                "",
                userName,
                userEmail,
                "",
                userPassword,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                token!!,
                "",
                "",
                "", null, null))

//
//
//            if (listResource != null) {
//
//                Utils.psLog("Got Data" + listResource.message + listResource.toString());
//
//                switch (listResource.status) {
//                    case LOADING:
//                        // Loading State
//                        // Data are from Local DB
//
//                        prgDialog.get().show();
//
//                        break;
//                    case SUCCESS:
//                        // Success State
//                        // Data are from Server
//
//                        if(listResource.data != null) {
//                            try {
//                                if (getActivity() != null) {
//                                    pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
//                                    pref.edit().putString(Constants.USER_NAME, listResource.data.user.userName).apply();
//                                    pref.edit().putString(Constants.USER_EMAIL, listResource.data.user.userEmail).apply();
//                                }
//
//                            } catch (NullPointerException ne) {
//                                Utils.psErrorLog("Null Pointer Exception.", ne);
//                            } catch (Exception e) {
//                                Utils.psErrorLog("Error in getting notification flag data.", e);
//                            }
//
//                            userViewModel.isLoading = false;
//                            prgDialog.get().cancel();
//                            updateRegisterBtnStatus();
//
//                            if (getActivity() instanceof MainActivity) {
//                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
//                                navigationController.navigateToUserProfile( (MainActivity) getActivity());
//                            } else {
//                                try {
//                                    getActivity().finish();
//                                } catch (Exception e) {
//                                    Utils.psErrorLog("Error in closing parent activity.", e);
//                                }
//                            }
//                        }
//
//                        break;
//                    case ERROR:
//                        // Error State
//
////                        if(listResource.data != null) {
//                            psDialogMsg.showWarningDialog(getString(R.string.error_message__email_exists), getString(R.string.app__ok));
//                            binding.get().registerButton.setText(getResources().getString(R.string.register__register));
//                            psDialogMsg.show();
////                        }else {
////                            psDialogMsg.cancel();
////                        }
//
//                        userViewModel.isLoading = false;
//                        prgDialog.get().cancel();
//
//                        break;
//                    default:
//                        // Default
//                        userViewModel.isLoading = false;
//                        prgDialog.get().cancel();
//                        break;
//                }
//
//            } else {
//
//                // Init Object or Empty Data
//                Utils.psLog("Empty Data");
//            }
//
//        });
    } //endregion
}