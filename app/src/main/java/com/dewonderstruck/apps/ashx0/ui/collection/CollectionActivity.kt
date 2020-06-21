package com.dewonderstruck.apps.ashx0.ui.collection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityCollectionBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

class CollectionActivity : PSAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityFilteringBinding: ActivityCollectionBinding = DataBindingUtil.setContentView(this, R.layout.activity_collection)
        initUI(activityFilteringBinding)
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun initUI(binding: ActivityCollectionBinding) {
        val title = intent.getStringExtra(Constants.COLLECTION_NAME)
        if (title != null) {
            initToolbar(binding.toolbar, title)
        } else {
            initToolbar(binding.toolbar, resources.getString(R.string.product_list_title))
        }
        setupFragment(CollectionFragment())
    }
}