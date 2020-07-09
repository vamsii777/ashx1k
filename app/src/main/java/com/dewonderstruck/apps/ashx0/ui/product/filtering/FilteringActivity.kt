package com.dewonderstruck.apps.ashx0.ui.product.filtering

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityFilteringBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

class FilteringActivity : PSAppCompactActivity() {
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_filtering)

        // Init all UI
        initUI(binding)
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    //endregion
    //region Private Methods
    private fun initUI(binding: ActivityFilteringBinding) {
        val intent = intent
        val name = intent.getStringExtra(Constants.FILTERING_FILTER_NAME)
        if (name == Constants.FILTERING_TYPE_FILTER) {

            // Toolbar
            initToolbar(binding.toolbar, resources.getString(R.string.typefilter))

            // setup Fragment
            setupFragment(CategoryFilterFragment())
            // Or you can call like this
            //setupFragment(new NewsListFragment(), R.id.content_frame);
        } else if (name == Constants.FILTERING_SPECIAL_FILTER) {
            initToolbar(binding.toolbar, resources.getString(R.string.typefilter))
            setupFragment(FilterFragment())
        }
    } //endregion
}