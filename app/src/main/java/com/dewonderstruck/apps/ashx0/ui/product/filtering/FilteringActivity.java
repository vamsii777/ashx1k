package com.dewonderstruck.apps.ashx0.ui.product.filtering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.preference.PreferenceManager;

import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.databinding.ActivityFilteringBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;

public class FilteringActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFilteringBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_filtering);

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

    private void initUI(ActivityFilteringBinding binding) {

        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.FILTERING_FILTER_NAME);

        if (name.equals(Constants.FILTERING_TYPE_FILTER)) {

            // Toolbar
            initToolbar(binding.toolbar, getResources().getString(R.string.typefilter));

            // setup Fragment
            setupFragment(new CategoryFilterFragment());
            // Or you can call like this
            //setupFragment(new NewsListFragment(), R.id.content_frame);
        } else if (name.equals(Constants.FILTERING_SPECIAL_FILTER)) {
            initToolbar(binding.toolbar, getResources().getString(R.string.typefilter));

            setupFragment(new FilterFragment());
        }

    }

    //endregion
}
