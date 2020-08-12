package com.dewonderstruck.apps.ashx0.ui.terms

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityTermsAndConditionBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

class TermsAndConditionsActivity : DeAppCompactActivity() {
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTermsAndConditionBinding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition)

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
    private fun initUI(binding: ActivityTermsAndConditionBinding) {

        // Toolbar
        if (intent.getStringExtra(Constants.SHOP_TERMS_FLAG) == Constants.SHOP_REFUND) {
            initToolbar(binding.toolbar, resources.getString(R.string.refund_policy__title))
        } else {
            initToolbar(binding.toolbar, resources.getString(R.string.terms_and_conditions__title))
        }
        // setup Fragment
        setupFragment(TermsAndConditionsFragment())
    } //endregion
}