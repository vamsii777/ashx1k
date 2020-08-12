package com.dewonderstruck.apps.ashx0.ui.privacyandpolicy;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;


import com.dewonderstruck.apps.ashx0.R;

import com.dewonderstruck.apps.ashx0.ui.common.DeAppCompactActivity;
import com.dewonderstruck.apps.ashx0.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends DeAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPrivacyPolicyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);

        // Init all UI
        initUI(binding);

    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
//        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
//        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);
//
//        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
//    }
    //endregion


    //region Private Methods

    private void initUI(ActivityPrivacyPolicyBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.terms_and_conditions__title));

        // setup Fragment
        setupFragment(new PrivacyPolicyFragment());

    }
}

//endregion
