package com.dewonderstruck.apps.ashx0.ui.forceupdate

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityForceUpdateBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeAppCompactActivity

class ForceUpdateActivity : DeAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityForceUpdateBinding = DataBindingUtil.setContentView(this, R.layout.activity_force_update)

        // Init all UI
        initUI(binding)
    }

    private fun initUI(binding: ActivityForceUpdateBinding) {

        // Toolbar
//        initToolbar(binding.toolbar, getResources().getString(R.string.comment__title));
        setupFragment(ForceUpdateFragment())
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);
    }

    override fun onBackPressed() {}
}