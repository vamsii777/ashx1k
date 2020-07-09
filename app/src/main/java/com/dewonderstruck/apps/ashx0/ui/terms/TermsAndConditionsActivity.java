package com.dewonderstruck.apps.ashx0.ui.terms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.dewonderstruck.apps.ashx0.R;
import androidx.databinding.DataBindingUtil;

import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.databinding.ActivityTermsAndConditionBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;

public class TermsAndConditionsActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTermsAndConditionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);

        // Init all UI
        initUI(binding);

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

    private void initUI(ActivityTermsAndConditionBinding binding) {

        // Toolbar
        if(getIntent().getStringExtra(Constants.SHOP_TERMS_FLAG).equals(Constants.SHOP_REFUND)){
            initToolbar(binding.toolbar, getResources().getString(R.string.refund_policy__title));
        }else {
            initToolbar(binding.toolbar, getResources().getString(R.string.terms_and_conditions__title));
        }
        // setup Fragment
        setupFragment(new TermsAndConditionsFragment());

    }

    //endregion


}