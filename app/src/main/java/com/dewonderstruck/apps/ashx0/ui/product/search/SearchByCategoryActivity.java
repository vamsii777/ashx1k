package com.dewonderstruck.apps.ashx0.ui.product.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.databinding.ActivitySearchByCategoryBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

public class SearchByCategoryActivity extends PSAppCompactActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchByCategoryBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_category);

        initUI(databinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }

    protected void initUI(ActivitySearchByCategoryBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.CATEGORY_FLAG);

        switch (fragName) {
            case Constants.CATEGORY:
                setupFragment(new SearchCategoryFragment());

                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_cat_title));
                break;
            case Constants.SUBCATEGORY:
                setupFragment(new SearchSubCategoryFragment());

                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_sub_cat_title));
                break;

            case Constants.CITY:
                setupFragment(new SearchCityListFragment());

                initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_city_title));
                break;
        }

    }
}
