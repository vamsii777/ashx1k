package com.dewonderstruck.apps.ashx0.ui.danceoholics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityAppLoadingBinding
import com.dewonderstruck.apps.ashx0.databinding.ActivityDanceoholicsBinding
import com.dewonderstruck.apps.ashx0.ui.apploading.AppLoadingFragment
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper
import com.dewonderstruck.apps.ashx0.utils.Utils


class DanceoholicsActivity : PSAppCompactActivity() {
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDanceoholicsBinding = DataBindingUtil.setContentView(this, R.layout.activity_danceoholics)
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
    private fun initUI(binding: ActivityDanceoholicsBinding ) {

        // setup Fragment
        setupFragment(DanceoholicsFragment())
    }
}