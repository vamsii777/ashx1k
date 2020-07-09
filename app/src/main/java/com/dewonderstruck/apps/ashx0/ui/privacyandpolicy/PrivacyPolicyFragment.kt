package com.dewonderstruck.apps.ashx0.ui.privacyandpolicy

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
import com.dewonderstruck.apps.ashx0.databinding.FragmentPrivacyPolicyBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.aboutus.AboutUsViewModel
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class PrivacyPolicyFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var aboutUsViewModel: AboutUsViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentPrivacyPolicyBinding>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentPrivacyPolicyBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_privacy_policy,
                container,
                false,
                dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {

//        if(getActivity() instanceof MainActivity)  {
//            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity)getActivity()).updateMenuIconWhite();
//        }
    }

    override fun initViewModels() {
        aboutUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutUsViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        aboutUsViewModel!!.setAboutUsObj("about us")
        aboutUsViewModel!!.aboutUsData.observe(this, Observer { resource: Resource<AboutUs?>? ->
            if (resource != null) {
                when (resource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {
                            fadeIn(binding!!.get().root)
                        }
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            setAboutUsData(resource.data)
                        }
                    Status.ERROR -> {
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
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.")
            } else {
                Utils.psLog("No Data of About Us.")
            }
        })
    }

    private fun setAboutUsData(aboutUs: AboutUs?) {
        binding!!.get().aboutUs = aboutUs
        binding!!.get().termsAndConditionTextView.text = aboutUs!!.privacyPolicy
    } //endregion
}