package com.dewonderstruck.apps.ashx0.ui.stripe

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityStripeBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity

class StripeActivity : PSAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityStripeBinding = DataBindingUtil.setContentView(this, R.layout.activity_stripe)
        initUI(binding)
    }

    private fun initUI(binding: ActivityStripeBinding) {

        // Toolbar
        initToolbar(binding.toolbar, resources.getString(R.string.checkout__stripe))

        // setup Fragment
        setupFragment(StripeFragment())
    }
}