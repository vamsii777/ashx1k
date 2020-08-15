package com.dewonderstruck.apps.ashx0.ui.user.verifyemail

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
import com.dewonderstruck.apps.ashx0.databinding.FragmentVerifyEmailBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class VerifyEmailFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentVerifyEmailBinding>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentVerifyEmailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)

//        Utils.setExpandedToolbar(getActivity());
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {

//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity) getActivity()).updateMenuIconWhite();
//            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
//
//        }
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get().emailTextView.text = userEmailToVerify
        binding!!.get().submitButton.setOnClickListener { v: View? ->
            if (validateInput()) {
                binding!!.get().submitButton.isEnabled = false
                binding!!.get().submitButton.text = resources.getString(R.string.message__loading)
                userViewModel!!.setEmailVerificationUser(Utils.checkUserId(userIdToVerify), binding!!.get().enterCodeEditText.text.toString())
            } else {
                Toast.makeText(context, getString(R.string.verify_email__enter_code), Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.get().resentCodeButton.setOnClickListener { v: View? -> userViewModel!!.setResentVerifyCodeObj(userEmailToVerify) }
        binding!!.get().changeEmailButton.setOnClickListener { v: View? ->
            if (activity is MainActivity) {
                navigationController.navigateToUserRegister((activity as MainActivity?)!!)
            } else {
                if (activity != null) {
                    navigationController.navigateToUserRegisterActivity(requireActivity())
                    requireActivity().finish()
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        return !binding!!.get().enterCodeEditText.text.toString().isEmpty()
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        val itemList = userViewModel!!.emailVerificationUser
        itemList?.observe(this, Observer { listResource: Resource<UserLogin?>? ->
            if (listResource != null) {
                binding!!.get().submitButton.isEnabled = true
                binding!!.get().submitButton.text = resources.getString(R.string.verify_email__submit)
                when (listResource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS ->                             // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            try {
                                if (activity != null) {
                                    Utils.updateUserLoginData(pref!!, listResource.data.user)
                                    Utils.navigateAfterUserLogin(activity, navigationController)
                                }
                            } catch (ne: NullPointerException) {
                                Utils.psErrorLog("Null Pointer Exception.", ne)
                            } catch (e: Exception) {
                                Utils.psErrorLog("Error in getting notification flag data.", e)
                            }
                        }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        //                            psDialogMsg.showErrorDialog(getString(R.string.error_message__code_not_verify), getString(R.string.app__ok));
                        psDialogMsg!!.show()
                    }
                    else -> {
                    }
                }
            }
        })


        //For resent code
        userViewModel!!.resentVerifyCodeData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                when (result.status) {
                    Status.SUCCESS ->
                        //add offer text
                        Toast.makeText(context, "Success Success", Toast.LENGTH_SHORT).show()
                    Status.ERROR -> Toast.makeText(context, "Fail resent code again", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}