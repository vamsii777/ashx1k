package com.dewonderstruck.apps.ashx0.ui.privacyandpolicy

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityPrivacyPolicyBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity

class PrivacyPolicyActivity : PSAppCompactActivity() {
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPrivacyPolicyBinding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)

        // Init all UI
        initUI(binding)
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
    private fun initUI(binding: ActivityPrivacyPolicyBinding) {

        // Toolbar
        initToolbar(binding.toolbar, resources.getString(R.string.terms_and_conditions__title))

        // setup Fragment
        setupFragment(PrivacyPolicyFragment())
    }
}