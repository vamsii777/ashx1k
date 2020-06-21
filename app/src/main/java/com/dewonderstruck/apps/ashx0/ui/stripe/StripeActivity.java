package com.dewonderstruck.apps.ashx0.ui.stripe;

import android.os.Bundle;


import com.dewonderstruck.apps.ashx0.R;

import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.databinding.ActivityStripeBinding;

import androidx.databinding.DataBindingUtil;

public class StripeActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityStripeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_stripe);

        initUI(binding);

    }

    private void initUI(ActivityStripeBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.checkout__stripe));

        // setup Fragment
        setupFragment(new StripeFragment());

    }
}

