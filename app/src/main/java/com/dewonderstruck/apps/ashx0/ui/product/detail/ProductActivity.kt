package com.dewonderstruck.apps.ashx0.ui.product.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityProductBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

class ProductActivity : PSAppCompactActivity() {
    val KEY = Constants.PRODUCT_NAME
    var productName: String? = null
    private var productDetailFragment: ProductDetailFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityProductBinding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        if (this.intent != null) {
            productName = intent.getStringExtra(KEY)
        }
        initUI(binding)
    }

    //region Private Methods
    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    var isShow = false
    var scrollRange = -1

    //endregion
    private fun initUI(binding: ActivityProductBinding) {
        initToolbar(binding.toolbar, productName!!)
        binding.toolbar.setNavigationIcon(R.drawable.baseline_back_white_with_grey_bg_24)
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

        // setup Fragment
        productDetailFragment = ProductDetailFragment()
        setupFragment(productDetailFragment)
        productDetailFragment!!.imageViewPager = AutoClearedValue(productDetailFragment, binding.imageViewPager)
        productDetailFragment!!.pageIndicatorLayout = AutoClearedValue(productDetailFragment, binding.pagerIndicator)

        /*productDetailFragment.addToCartButton = new AutoClearedValue<>(productDetailFragment, binding.addToBasketButton);*/productDetailFragment!!.buyNowButton = AutoClearedValue(productDetailFragment, binding.buyNowButton)
        binding.mainAppBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout, verticalOffset: Int ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset == 0) {
                //collapse map
                isShow = true
                binding.toolbar.setNavigationIcon(R.drawable.baseline_back_white_24)
                Utils.psLog("ABC : True")
            } else if (isShow) {
                //expanded map
                isShow = false
                binding.toolbar.setNavigationIcon(R.drawable.baseline_back_white_with_grey_bg_24)
                Utils.psLog("ABC : False")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            productDetailFragment!!.refreshBasketData()
            finish()
        }
        if (item.itemId == R.id.action_basket) {
            try {
                productDetailFragment!!.callBasket()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}