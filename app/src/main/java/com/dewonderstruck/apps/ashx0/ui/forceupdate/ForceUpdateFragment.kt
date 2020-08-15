package com.dewonderstruck.apps.ashx0.ui.forceupdate

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentForceUpdateBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants

class ForceUpdateFragment : DeFragment() {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentForceUpdateBinding>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentForceUpdateBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_force_update, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initUIAndActions() {
        if (activity != null) {
            if (activity!!.intent.hasExtra(Constants.APPINFO_FORCE_UPDATE_MSG)) {
                binding!!.get().descriptionTextView.text = activity!!.intent.getStringExtra(Constants.APPINFO_FORCE_UPDATE_MSG)
            }
            if (activity!!.intent.hasExtra(Constants.APPINFO_FORCE_UPDATE_TITLE)) {
                binding!!.get().titleTextView.text = activity!!.intent.getStringExtra(Constants.APPINFO_FORCE_UPDATE_TITLE)
            }
        }
        binding!!.get().button4.setOnClickListener { v: View? -> navigationController.navigateToPlayStore(this!!.activity!!) }
        binding!!.get().descriptionTextView.movementMethod = ScrollingMovementMethod()
    }

    override fun initViewModels() {}
    override fun initAdapters() {}
    override fun initData() {}
}