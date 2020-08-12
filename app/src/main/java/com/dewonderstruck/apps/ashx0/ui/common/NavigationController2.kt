package com.dewonderstruck.apps.ashx0.ui.common

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.ui.blog.detail.BlogDetailActivity
import com.dewonderstruck.apps.ashx0.ui.blog.list.BlogListActivity
import com.dewonderstruck.apps.ashx0.ui.category.CategoryListActivity
import com.dewonderstruck.apps.ashx0.ui.category.CategoryListFragment
import com.dewonderstruck.apps.ashx0.ui.collection.CollectionBaseActivity
import com.dewonderstruck.apps.ashx0.ui.collection.productCollectionHeader.ProductCollectionHeaderListFragment
import com.dewonderstruck.apps.ashx0.ui.contactus.ContactUsFragment
import com.dewonderstruck.apps.ashx0.ui.danceoholics.DanceoholicsFragment

import com.dewonderstruck.apps.ashx0.ui.forceupdate.ForceUpdateActivity
import com.dewonderstruck.apps.ashx0.ui.gallery.GalleryActivity
import com.dewonderstruck.apps.ashx0.ui.gallery.detail.GalleryDetailActivity
import com.dewonderstruck.apps.ashx0.ui.language.LanguageFragment
import com.dewonderstruck.apps.ashx0.ui.notification.detail.NotificationActivity
import com.dewonderstruck.apps.ashx0.ui.notification.list.NotificationListActivity
import com.dewonderstruck.apps.ashx0.ui.privacyandpolicy.PrivacyPolicyActivity
import com.dewonderstruck.apps.ashx0.ui.privacyandpolicy.PrivacyPolicyFragment
import com.dewonderstruck.apps.ashx0.ui.product.MainFragment
import com.dewonderstruck.apps.ashx0.ui.product.detail.ProductActivity
import com.dewonderstruck.apps.ashx0.ui.product.favourite.FavouriteListActivity
import com.dewonderstruck.apps.ashx0.ui.product.favourite.FavouriteListFragment
import com.dewonderstruck.apps.ashx0.ui.product.filtering.FilteringActivity
import com.dewonderstruck.apps.ashx0.ui.product.history.HistoryFragment
import com.dewonderstruck.apps.ashx0.ui.product.history.UserHistoryListActivity
import com.dewonderstruck.apps.ashx0.ui.product.list.ProductListActivity
import com.dewonderstruck.apps.ashx0.ui.product.list.ProductListFragment
import com.dewonderstruck.apps.ashx0.ui.product.search.SearchByCategoryActivity
import com.dewonderstruck.apps.ashx0.ui.product.search.SearchFragment
import com.dewonderstruck.apps.ashx0.ui.setting.AppInfoActivity
import com.dewonderstruck.apps.ashx0.ui.setting.NotificationSettingActivity
import com.dewonderstruck.apps.ashx0.ui.setting.SettingActivity
import com.dewonderstruck.apps.ashx0.ui.setting.SettingFragment
import com.dewonderstruck.apps.ashx0.ui.shop.ShopProfileFragment
import com.dewonderstruck.apps.ashx0.ui.terms.TermsAndConditionsActivity
import com.dewonderstruck.apps.ashx0.ui.user.*
import com.dewonderstruck.apps.ashx0.ui.user.phonelogin.PhoneLoginActivity
import com.dewonderstruck.apps.ashx0.ui.user.phonelogin.PhoneLoginFragment
import com.dewonderstruck.apps.ashx0.ui.user.verifyemail.VerifyEmailActivity
import com.dewonderstruck.apps.ashx0.ui.user.verifyemail.VerifyEmailFragment
import com.dewonderstruck.apps.ashx0.ui.user.verifyphone.VerifyMobileActivity
import com.dewonderstruck.apps.ashx0.ui.user.verifyphone.VerifyMobileFragment
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct
import com.dewonderstruck.apps.ashx0.viewobject.Noti
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 11/17/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class NavigationController2 @Inject constructor() {
    //region Variables
    private val containerId: Int = R.id.content_frame
    private var currentFragment: RegFragmentS? = null

    //endregion
    //region default navigation
    fun navigateToMainActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToUserLogin(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_USER_LOGIN)) {
            try {
                val fragment = UserLoginFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToUserProfile(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_USER_LOGIN)) {
            try {
                val fragment = ProfileFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToFavourite(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_FAVOURITE)) {
            try {
                val fragment = FavouriteListFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToHistory(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_HISTORY)) {
            try {
                val fragment = HistoryFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToUserRegister(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_USER_REGISTER)) {
            try {
                val fragment = UserRegisterFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToUserForgotPassword(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_USER_FOGOT_PASSWORD)) {
            try {
                val fragment = UserForgotPasswordFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToSetting(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_SETTING)) {
            try {
                val fragment = SettingFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToShopProfile(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_ABOUTUS)) {
            try {
                val fragment = ShopProfileFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToLanguageSetting(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_LANGUAGE_SETTING)) {
            try {
                val fragment = LanguageFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToHome(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_HOME)) {
            try {
                val fragment = MainFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToLatestProducts(mainActivity: MainActivity, productParameterHolder: ProductParameterHolder?) {
        if (checkFragmentChange(RegFragmentS.HOME_LATEST_PRODUCTS)) {
            try {
                val fragment = ProductListFragment()
                val bundle = Bundle()
                bundle.putSerializable(Constants.PRODUCT_PARAM_HOLDER_KEY, productParameterHolder)
                fragment.arguments = bundle
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToDiscountProduct(mainActivity: MainActivity, productParameterHolder: ProductParameterHolder?) {
        if (checkFragmentChange(RegFragmentS.HOME_DISCOUNT)) {
            try {
                val fragment = ProductListFragment()
                val bundle = Bundle()
                bundle.putSerializable(Constants.PRODUCT_PARAM_HOLDER_KEY, productParameterHolder)
                fragment.arguments = bundle
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToFeatureProducts(mainActivity: MainActivity, productParameterHolder: ProductParameterHolder?) {
        if (checkFragmentChange(RegFragmentS.HOME_FEATURED_PRODUCTS)) {
            try {
                val fragment = ProductListFragment()
                val bundle = Bundle()
                bundle.putSerializable(Constants.PRODUCT_PARAM_HOLDER_KEY, productParameterHolder)
                fragment.arguments = bundle
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToTrendingProducts(mainActivity: MainActivity, productParameterHolder: ProductParameterHolder?) {
        if (checkFragmentChange(RegFragmentS.HOME_TRENDINGPRODUCTS)) {
            try {
                val fragment = ProductListFragment()
                val bundle = Bundle()
                bundle.putSerializable(Constants.PRODUCT_PARAM_HOLDER_KEY, productParameterHolder)
                fragment.arguments = bundle
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToCategory(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_CATEGORY)) {
            try {
                val fragment = CategoryListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.CATEGORY_TYPE, Constants.CATEGORY)
                fragment.arguments = bundle
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToDanceoholics(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_DANCEOHOLICS)) {
            try {
                val fragment = DanceoholicsFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: java.lang.Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }


    fun navigateToCollectionList(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_PRODUCT_COLLECTION)) {
            try {
                val fragment = ProductCollectionHeaderListFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToSearch(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_SEARCH)) {
            try {
                val fragment = SearchFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToGalleryActivity(activity: Activity, imgType: String, imgParentId: String) {
        val intent = Intent(activity, GalleryActivity::class.java)
        if (imgType != "") {
            intent.putExtra(Constants.IMAGE_TYPE, imgType)
        }
        if (imgParentId != "") {
            intent.putExtra(Constants.IMAGE_PARENT_ID, imgParentId)
        }
        activity.startActivity(intent)
    }

    fun navigateToDetailGalleryActivity(activity: Activity, imgType: String, newsId: String, imgId: String) {
        val intent = Intent(activity, GalleryDetailActivity::class.java)
        if (imgType != "") {
            intent.putExtra(Constants.IMAGE_TYPE, imgType)
        }
        if (newsId != "") {
            intent.putExtra(Constants.PRODUCT_ID, newsId)
        }
        if (imgId != "") {
            intent.putExtra(Constants.IMAGE_ID, imgId)
        }
        activity.startActivity(intent)
    }

    fun navigateToSettingActivity(activity: Activity) {
        val intent = Intent(activity, SettingActivity::class.java)
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__PROFILE_FRAGMENT)
    }

    fun navigateToNotificationSettingActivity(activity: Activity) {
        val intent = Intent(activity, NotificationSettingActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToEditProfileActivity(activity: Activity) {
        val intent = Intent(activity, ProfileEditActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToAppInfoActivity(activity: Activity) {
        val intent = Intent(activity, AppInfoActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToProfileEditActivity(activity: Activity) {
        val intent = Intent(activity, ProfileEditActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToTermsAndConditionsActivity(activity: Activity, flag: String?) {
        val intent = Intent(activity, TermsAndConditionsActivity::class.java)
        intent.putExtra(Constants.SHOP_TERMS_FLAG, flag)
        activity.startActivity(intent)
    }

    fun navigateToVerifyEmailActivity(activity: Activity) {
        val intent = Intent(activity, VerifyEmailActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToVerifyEmail(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_USER_EMAIL_VERIFY)) {
            try {
                val fragment = VerifyEmailFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToUserLoginActivity(activity: Activity) {
        val intent = Intent(activity, UserLoginActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToUserRegisterActivity(activity: Activity) {
        val intent = Intent(activity, UserRegisterActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToUserForgotPasswordActivity(activity: Activity) {
        val intent = Intent(activity, UserForgotPasswordActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToPasswordChangeActivity(activity: Activity) {
        val intent = Intent(activity, PasswordChangeActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToNotificationList(activity: Activity) {
        val intent = Intent(activity, NotificationListActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToNotificationDetail(activity: Activity, noti: Noti, token: String?) {
        val intent = Intent(activity, NotificationActivity::class.java)
        intent.putExtra(Constants.NOTI_ID, noti.id)
        intent.putExtra(Constants.NOTI_TOKEN, token)
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT)
    }

    fun navigateToDetailActivity(activity: Activity, product: Product) {
        val intent = Intent(activity, ProductActivity::class.java)
        intent.putExtra(Constants.PRODUCT_ID, product.id)
        intent.putExtra(Constants.PRODUCT_NAME, product.name)
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE)
        intent.putExtra(Constants.BASKET_FLAG, Constants.ZERO)
        activity.startActivity(intent)
    }

    fun navigateToProductDetailActivity(activity: Activity, historyProduct: HistoryProduct) {
        val intent = Intent(activity, ProductActivity::class.java)
        intent.putExtra(Constants.PRODUCT_ID, historyProduct.id)
        intent.putExtra(Constants.PRODUCT_NAME, historyProduct.historyName)
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ZERO)
        intent.putExtra(Constants.BASKET_FLAG, Constants.ZERO)
        activity.startActivity(intent)
    }

    fun navigateToProductDetailActivity(activity: Activity, basket: Basket) {
        val intent = Intent(activity, ProductActivity::class.java)
        intent.putExtra(Constants.PRODUCT_ID, basket.productId)
        intent.putExtra(Constants.PRODUCT_NAME, basket.product.name)
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ZERO)
        intent.putExtra(Constants.BASKET_FLAG, Constants.ONE)
        intent.putExtra(Constants.PRODUCT_PRICE, basket.basketPrice.toString())
        intent.putExtra(Constants.PRODUCT_ATTRIBUTE, basket.selectedAttributes)
        intent.putExtra(Constants.PRODUCT_COUNT, basket.count.toString())
        intent.putExtra(Constants.PRODUCT_SELECTED_COLOR, basket.selectedColorId)
        intent.putExtra(Constants.BASKET_ID, basket.id)
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__BASKET_FRAGMENT)
    }

    fun navigateToUserHistoryActivity(activity: Activity) {
        val intent = Intent(activity, UserHistoryListActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToFavouriteActivity(activity: Activity) {
        val intent = Intent(activity, FavouriteListActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToCategoryActivity(activity: Activity, flag: String?) {
        val intent = Intent(activity, CategoryListActivity::class.java)
        intent.putExtra(Constants.CATEGORY_TYPE, flag)
        activity.startActivity(intent)
    }

    fun navigateToTypeFilterFragment(mainActivity: FragmentActivity, catId: String?, subCatId: String?, productParameterHolder: ProductParameterHolder?, name: String) {
        var subCatId = subCatId
        if (name == Constants.FILTERING_TYPE_FILTER) {
            val intent = Intent(mainActivity, FilteringActivity::class.java)
            intent.putExtra(Constants.CATEGORY_ID, catId)
            if (subCatId == null || subCatId == "") {
                subCatId = Constants.ZERO
            }
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId)
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name)
            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__PRODUCT_LIST_FRAGMENT)
        } else if (name == Constants.FILTERING_SPECIAL_FILTER) {
            val intent = Intent(mainActivity, FilteringActivity::class.java)
            intent.putExtra(Constants.FILTERING_HOLDER, productParameterHolder)
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name)
            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__PRODUCT_LIST_FRAGMENT)
        }
    }

    fun navigateBackToCommentListFragment(commentDetailActivity: FragmentActivity, comment_headerId: String?) {
        val intent = Intent()
        intent.putExtra(Constants.COMMENT_HEADER_ID, comment_headerId)
        commentDetailActivity.setResult(Constants.RESULT_CODE__REFRESH_COMMENT_LIST, intent)
    }

    fun navigateBackToProductDetailFragment(productDetailActivity: FragmentActivity, product_id: String?) {
        val intent = Intent()
        intent.putExtra(Constants.PRODUCT_ID, product_id)
        productDetailActivity.setResult(Constants.RESULT_CODE__REFRESH_COMMENT_LIST, intent)
    }

    fun navigateBackToHomeFeaturedFragment(mainActivity: FragmentActivity, catId: String?, subCatId: String?) {
        val intent = Intent()
        intent.putExtra(Constants.CATEGORY_ID, catId)
        intent.putExtra(Constants.SUBCATEGORY_ID, subCatId)
        mainActivity.setResult(Constants.RESULT_CODE__CATEGORY_FILTER, intent)
    }

    fun navigateBackToHomeFeaturedFragmentFromFiltering(mainActivity: FragmentActivity, productParameterHolder: ProductParameterHolder?) {
        val intent = Intent()
        intent.putExtra(Constants.FILTERING_HOLDER, productParameterHolder)
        mainActivity.setResult(Constants.RESULT_CODE__SPECIAL_FILTER, intent)
    }

    fun navigateToCollectionProductList(fragmentActivity: FragmentActivity, id: String?, Name: String?, image_path: String?) {
        val intent = Intent(fragmentActivity, CollectionBaseActivity::class.java)
        intent.putExtra(Constants.COLLECTION_ID, id)
        intent.putExtra(Constants.COLLECTION_NAME, Name)
        intent.putExtra(Constants.COLLECTION_IMAGE, image_path)
        fragmentActivity.startActivity(intent)
    }

    fun navigateBackToCheckoutFragment(activity: Activity, stripeToken: String?) {
        val intent = Intent()
        intent.putExtra(Constants.PAYMENT_TOKEN, stripeToken)
        activity.setResult(Constants.RESULT_CODE__STRIPE_ACTIVITY, intent)
    }

    fun navigateToHomeFilteringActivity(mainActivity: FragmentActivity, productParameterHolder: ProductParameterHolder?, title: String?) {
        val intent = Intent(mainActivity, ProductListActivity::class.java)
        intent.putExtra(Constants.SHOP_TITLE, title)
        intent.putExtra(Constants.PRODUCT_PARAM_HOLDER_KEY, productParameterHolder)
        mainActivity.startActivity(intent)
    }

    fun navigateToSearchActivityCategoryFragment(fragmentActivity: FragmentActivity, fragName: String?, catId: String, subCatId: String, countryId: String, cityId: String) {
        val intent = Intent(fragmentActivity, SearchByCategoryActivity::class.java)
        intent.putExtra(Constants.CATEGORY_FLAG, fragName)
        if (catId != Constants.NO_DATA) {
            intent.putExtra(Constants.CATEGORY_ID, catId)
        }
        if (subCatId != Constants.NO_DATA) {
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId)
        }
        if (countryId != Constants.NO_DATA) {
            intent.putExtra(Constants.COUNTRY_ID, countryId)
        }
        if (cityId != Constants.NO_DATA) {
            intent.putExtra(Constants.CITY_ID, cityId)
        }
        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__SEARCH_FRAGMENT)
    }

    fun navigateBackToSearchFragment(fragmentActivity: FragmentActivity, catId: String?, cat_Name: String?) {
        val intent = Intent()
        intent.putExtra(Constants.CATEGORY_NAME, cat_Name)
        intent.putExtra(Constants.CATEGORY_ID, catId)
        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_CATEGORY, intent)
    }

    fun navigateBackToSearchFragmentFromSubCategory(fragmentActivity: FragmentActivity, sub_id: String?, sub_Name: String?) {
        val intent = Intent()
        intent.putExtra(Constants.SUBCATEGORY_NAME, sub_Name)
        intent.putExtra(Constants.SUBCATEGORY_ID, sub_id)
        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY, intent)
    }

    fun navigateBackToSearchFragmentFromCountry(fragmentActivity: FragmentActivity, countryId: String?, countryName: String?) {
        val intent = Intent()
        intent.putExtra(Constants.COUNTRY_NAME, countryName)
        intent.putExtra(Constants.COUNTRY_ID, countryId)
        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_COUNTRY, intent)
    }

    fun navigateBackToSearchFragmentFromCity(fragmentActivity: FragmentActivity, cityId: String?, cityName: String?) {
        val intent = Intent()
        intent.putExtra(Constants.CITY_NAME, cityName)
        intent.putExtra(Constants.CITY_ID, cityId)
        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_CITY, intent)
    }

    fun navigateBackToProfileFragment(fragmentActivity: FragmentActivity) {
        val intent = Intent()
        fragmentActivity.setResult(Constants.RESULT_CODE__LOGOUT_ACTIVATED, intent)
    }

    fun navigateToBlogList(fragmentActivity: FragmentActivity) {
        val intent = Intent(fragmentActivity, BlogListActivity::class.java)
        fragmentActivity.startActivity(intent)
    }

    fun navigateToBlogDetailActivity(fragmentActivity: FragmentActivity, blogId: String?) {
        val intent = Intent(fragmentActivity, BlogDetailActivity::class.java)
        intent.putExtra(Constants.BLOG_ID, blogId)
        fragmentActivity.startActivity(intent)
    }

    fun navigateToForceUpdateActivity(fragmentActivity: FragmentActivity, title: String?, msg: String?) {
        val intent = Intent(fragmentActivity, ForceUpdateActivity::class.java)
        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_MSG, msg)
        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_TITLE, title)
        fragmentActivity.startActivity(intent)
    }

    fun navigateToPlayStore(fragmentActivity: FragmentActivity) {
//        try {
//            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_MARKET_URL)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_HTTP_URL)));
//        }
        val uri = Uri.parse(Config.PLAY_STORE_MARKET_URL_FIX + fragmentActivity.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            fragmentActivity.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            fragmentActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAY_STORE_HTTP_URL_FIX + fragmentActivity.packageName)))
        }
    }

    fun navigateToContactUs(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_CONTACTUS)) {
            try {
                val fragment = ContactUsFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToPrivacyPolicy(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_PRIVACY_POLICY
                )) {
            try {
                val fragment = PrivacyPolicyFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    //region Private methods
    private fun checkFragmentChange(regFragments: RegFragmentS): Boolean {
        if (currentFragment != regFragments) {
            currentFragment = regFragments
            return true
        }
        return false
    }

    fun navigateToPrivacyPolicyActivity(activity: Activity) {
        val intent = Intent(activity, PrivacyPolicyActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToPhoneVerifyFragment(mainActivity: MainActivity, number: String?, userName: String?) {
        if (checkFragmentChange(RegFragmentS.HOME_PHONE_VERIFY)) {
            try {
                val fragment = VerifyMobileFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
                val args = Bundle()
                args.putString(Constants.USER_PHONE, number)
                args.putString(Constants.USER_NAME, userName)
                fragment.arguments = args
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToPhoneVerifyActivity(activity: Activity, number: String?, userName: String?) {
        val intent = Intent(activity, VerifyMobileActivity::class.java)
        intent.putExtra(Constants.USER_PHONE, number)
        intent.putExtra(Constants.USER_NAME, userName)
        activity.startActivity(intent)
    }

    fun navigateToPhoneLoginFragment(mainActivity: MainActivity) {
        if (checkFragmentChange(RegFragmentS.HOME_PHONE_LOGIN)) {
            try {
                val fragment = PhoneLoginFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss()
            } catch (e: Exception) {
                Utils.psErrorLog("Error! Can't replace fragment.", e)
            }
        }
    }

    fun navigateToPhoneLoginActivity(activity: Activity) {
        val intent = Intent(activity, PhoneLoginActivity::class.java)
        activity.startActivity(intent)
    }

    /**
     * Remark : This enum is only for MainActivity,
     * For the other fragments, no need to register here
     */
    private enum class RegFragmentS {
        HOME_FRAGMENT, HOME_USER_LOGIN, HOME_FB_USER_REGISTER, HOME_BASKET, HOME_USER_REGISTER, HOME_USER_FOGOT_PASSWORD, HOME_ABOUTUS, HOME_CONTACTUS, HOME_NOTI_SETTING, HOME_APP_INFO, HOME_LANGUAGE_SETTING, HOME_LATEST_PRODUCTS, HOME_DISCOUNT, HOME_FEATURED_PRODUCTS, HOME_CATEGORY, HOME_SUBCATEGORY, HOME_HOME, HOME_TRENDINGPRODUCTS, HOME_COMMENTLISTS, HOME_SEARCH, HOME_NOTIFICATION, HOME_PRODUCT_COLLECTION, HOME_TRANSACTION, HOME_HISTORY, HOME_SETTING, HOME_DANCEOHOLICS, HOME_FAVOURITE, HOME_SHOP_LIST, HOME_SHOP_MENU, HOME_USER_EMAIL_VERIFY, HOME_PRIVACY_POLICY, HOME_PHONE_VERIFY, HOME_PHONE_LOGIN
    } //endregion

    //endregion
    //region Constructor
    init {

        // This setup is for MainActivity
    }
}