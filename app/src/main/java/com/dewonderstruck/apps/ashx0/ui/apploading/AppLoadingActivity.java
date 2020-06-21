package com.dewonderstruck.apps.ashx0.ui.apploading;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.R;

import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;
import com.dewonderstruck.apps.ashx0.databinding.ActivityAppLoadingBinding;

public class AppLoadingActivity extends PSAppCompactActivity {

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAppLoadingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_app_loading);
        // Init all UI
        initUI(binding);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }
    //endregion


    //region Private Methods

    private void initUI(ActivityAppLoadingBinding binding) {

        // setup Fragment

        setupFragment(new AppLoadingFragment());
    }
}
//endregion


