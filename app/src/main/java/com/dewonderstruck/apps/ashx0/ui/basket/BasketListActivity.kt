package com.dewonderstruck.apps.ashx0.ui.basket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityBasketListBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class BasketListActivity : PSAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBasketListBinding = DataBindingUtil.setContentView(this, R.layout.activity_basket_list)

        // Init all UI
        initUI(binding)
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

    //region Private Methods
    private fun initUI(binding: ActivityBasketListBinding) {

        // Toolbar
        initToolbar(binding.toolbar, resources.getString(R.string.basket__list))

        // setup Fragment
        setupFragment(BasketListFragment())
    } //endregion
}