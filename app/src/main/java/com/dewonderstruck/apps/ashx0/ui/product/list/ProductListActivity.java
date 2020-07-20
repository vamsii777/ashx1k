package com.dewonderstruck.apps.ashx0.ui.product.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.databinding.ActivityProductListBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class ProductListActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProductListBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_list);

        initUI(activityFilteringBinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initUI(ActivityProductListBinding binding) {

        String title = getIntent().getStringExtra(Constants.SHOP_TITLE);

        if (title != null) {
            initToolbar(binding.toolbar, title);
        } else {
            initToolbar(binding.toolbar, getString(R.string.product_list_title));
        }

        setupFragment(new ProductListFragment());

        // setup Fragment

        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

}
