package com.dewonderstruck.apps.ashx0.ui.terms

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
import com.dewonderstruck.apps.ashx0.databinding.FragmentTermsAndConditionsBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.shop.ShopViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Shop
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class TermsAndConditionsFragment : DeFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var shopViewModel: ShopViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentTermsAndConditionsBinding>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentTermsAndConditionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_and_conditions, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {}
    override fun initViewModels() {
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        intentData
        shopViewModel!!.setShopObj(Config.API_KEY)
        shopViewModel!!.shopData.observe(this, Observer { resource: Resource<Shop?>? ->
            if (resource != null) {
                Utils.psLog("Got Data" + resource.message + resource.toString())
                when (resource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {
                            fadeIn(binding!!.get().root)
                            binding!!.get().shop = resource.data
                            setAboutUsData(resource.data)
                        }
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            binding!!.get().shop = resource.data
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

    private val intentData: Unit
        private get() {
            try {
                if (activity != null) {
                    if (activity!!.intent.extras != null) {
                        shopViewModel!!.flag = activity!!.intent.extras!!.getString(Constants.SHOP_TERMS_FLAG)
                    }
                }
            } catch (e: Exception) {
                Utils.psErrorLog("", e)
            }
        }

    private fun setAboutUsData(shop: Shop?) {
        binding!!.get().shop = shop
        //        shopViewModel.aboutId = shop.id;
        intentData
        if (shopViewModel!!.flag == Constants.SHOP_TERMS) {
            binding!!.get().termsAndConditionTextView.text = shop!!.terms
        } else {
            binding!!.get().termsAndConditionTextView.text = shop!!.refundPolicy
        }
    }
}