package com.dewonderstruck.apps.ashx0.ui.user

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityPasswordChangeBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

class PasswordChangeActivity : DeAppCompactActivity() {
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPasswordChangeBinding = DataBindingUtil.setContentView(this, R.layout.activity_password_change)

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
    private fun initUI(binding: ActivityPasswordChangeBinding) {

        // Toolbar
        initToolbar(binding.toolbar, resources.getString(R.string.password_change__password_change))

        // setup Fragment
        setupFragment(PasswordChangeFragment())
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);
    } //endregion
}