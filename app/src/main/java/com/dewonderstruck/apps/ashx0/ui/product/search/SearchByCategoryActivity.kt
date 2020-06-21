package com.dewonderstruck.apps.ashx0.ui.product.search

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivitySearchByCategoryBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

class SearchByCategoryActivity : PSAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databinding: ActivitySearchByCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_category)
        initUI(databinding)
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    protected fun initUI(binding: ActivitySearchByCategoryBinding) {
        val intent = intent
        val fragName = intent.getStringExtra(Constants.CATEGORY_FLAG)
        when (fragName) {
            Constants.CATEGORY -> {
                setupFragment(SearchCategoryFragment())
                initToolbar(binding.toolbar, resources.getString(R.string.Feature_UI__search_alert_cat_title))
            }
            Constants.SUBCATEGORY -> {
                setupFragment(SearchSubCategoryFragment())
                initToolbar(binding.toolbar, resources.getString(R.string.Feature_UI__search_alert_sub_cat_title))
            }
            Constants.COUNTRY -> {
                setupFragment(SearchCountryListFragment())
                initToolbar(binding.toolbar, resources.getString(R.string.Feature_UI__search_alert_country_title))
            }
            Constants.CITY -> {
                setupFragment(SearchCityListFragment())
                initToolbar(binding.toolbar, resources.getString(R.string.Feature_UI__search_alert_city_title))
            }
        }
    }
}