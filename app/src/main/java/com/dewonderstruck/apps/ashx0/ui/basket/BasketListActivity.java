package com.dewonderstruck.apps.ashx0.ui.basket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.preference.PreferenceManager;

import androidx.fragment.app.Fragment;

import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.databinding.ActivityBasketListBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
public class BasketListActivity extends PSAppCompactActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBasketListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_basket_list);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //region Private Methods

    private void initUI(ActivityBasketListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.basket__list));

        // setup Fragment
        setupFragment(new BasketListFragment());

    }

    //endregion
}
