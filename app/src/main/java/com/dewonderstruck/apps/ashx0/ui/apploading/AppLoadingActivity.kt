package com.dewonderstruck.apps.ashx0.ui.apploading

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityAppLoadingBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

class AppLoadingActivity : PSAppCompactActivity() {
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAppLoadingBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_loading)
        // Init all UI
        initUI(binding)

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    //endregion
    //region Private Methods
    private fun initUI(binding: ActivityAppLoadingBinding) {

        // setup Fragment
        setupFragment(AppLoadingFragment())
    }
}