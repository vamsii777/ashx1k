package com.dewonderstruck.apps

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.ashx0.BuildConfig
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityMainBinding
import com.dewonderstruck.apps.ashx0.ui.blog.list.BlogListActivity
import com.dewonderstruck.apps.ashx0.ui.common.NavigationController
import com.dewonderstruck.apps.ashx0.ui.common.NavigationController2
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.ui.product.MainFragment
import com.dewonderstruck.apps.ashx0.utils.*
import com.dewonderstruck.apps.ashx0.viewmodel.common.NotificationViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shop.ShopViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import com.facebook.login.LoginManager
import com.google.ads.consent.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

/**
 * MainActivity of Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 *
 * @author Vamsi Madduluri
 * @version 1.0
 * @since 11/15/17.
 */


class MainActivity : PSAppCompactActivity() {
    //region Variables
    //@JvmField

    @set:Inject
    internal var pref: SharedPreferences? = null

    @JvmField
    @Inject
    var appLanguage: AppLanguage? = null
    private var notiSetting = false
    private var token: String? = ""
    private var userViewModel: UserViewModel? = null
    private var shopViewModel: ShopViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private var notificationViewModel: NotificationViewModel? = null
    private var user: User? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var isLogout = false
    private var token1: String? = null
    private var form: ConsentForm? = null
    private var basketNotificationTextView: TextView? = null
    private lateinit var updateListener: InstallStateUpdatedListener

    @set:Inject
    override var viewModelFactory: ViewModelProvider.Factory? = null

    @set:Inject
    override var navigationController: NavigationController2? = null

    @JvmField
    var binding: ActivityMainBinding? = null

    //endregion
    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_PSTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        initUIAndActions()
        initModels()
        initData()
        checkForUpdates()
        checkConsentStatus()
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Constants.REQUEST_CODE__MAIN_ACTIVITY
                        && resultCode == Constants.RESULT_CODE__RESTART_MAIN_ACTIVITY)) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
            fragment?.onActivityResult(requestCode, resultCode, data)
        }
        //1
        if (REQUEST_UPDATE == requestCode) {
            when (resultCode) {
                //2
                Activity.RESULT_OK -> {
                    if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.IMMEDIATE) {
                        Toast.makeText(baseContext, "Update Succeeded!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "Update Started!", Toast.LENGTH_SHORT).show()
                    }
                }
                //3
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(baseContext, "Update Cancelled!", Toast.LENGTH_SHORT).show()
                }
                //4
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    Toast.makeText(baseContext, "Update Failed!", Toast.LENGTH_SHORT).show()
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshBasketCount()
        try {
            if (pref!!.getBoolean(Constants.NOTI_EXISTS_TO_SHOW, false) ||
                    intent.getBooleanExtra(Constants.NOTI_EXISTS_TO_SHOW, false)) {
                val message = pref!!.getString(Constants.NOTI_MSG, "")
                if (message != "") {
                    val editor = pref!!.edit()
                    editor.putBoolean(Constants.NOTI_EXISTS_TO_SHOW, false).apply()

                    //if(!alreadyNotiMsgShow) {
                    showAlertMessage(message)
                    //    alreadyNotiMsgShow = true;
                    //}
                }
            }
        } catch (ne: NullPointerException) {
            Utils.psErrorLog("Null Pointer Exception.", ne)
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting notification flag data.", e)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
            if (fragment != null) {
                if (fragment is MainFragment) {
                    MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_MaterialComponents_R)
                            .setCancelable(true)
                            .setMessage(resources.getString(R.string.message__want_to_quit))
                            .setNegativeButton(resources.getString(R.string.app__cancel)) { dialog, which ->
                                // Respond to negative button press
                                dialog.cancel()
                            }
                            .setPositiveButton("OK") { dialog, which ->
                                // Respond to positive button press
                                dialog.dismiss()
                                finish()
                                System.exit(0)
                            }
                            .show()

                    val message = baseContext.getString(R.string.message__want_to_quit)
                    val okStr = baseContext.getString(R.string.message__ok_close)
                    val cancelStr = baseContext.getString(R.string.message__cancel_close)
                   /* psDialogMsg!!.showConfirmDialog(message, okStr, cancelStr)
                    psDialogMsg!!.show()
                    psDialogMsg!!.okButton.setOnClickListener { view: View? ->
                        psDialogMsg!!.cancel()
                        finish()
                        System.exit(0)
                    }
                    psDialogMsg!!.cancelButton.setOnClickListener({ view: View? -> psDialogMsg!!.cancel() })*/
                } else {
                    setSelectMenu(R.id.nav_home)
                    navigationController!!.navigateToHome(this)
                    setToolbarText(binding!!.toolbar, getString(R.string.app__app_name))
                    binding!!.bottomNavigationView.selectedItemId = R.id.home_menu
                }
            }
        }
        return true
    }
    //endregion
    //region Private Methods
    /**
     * Initialize Models
     */
    private fun initModels() {
        //MobileAds.initialize(this, getResources().getString(R.string.banner__home__footer));
        userViewModel = ViewModelProvider(this, viewModelFactory!!).get(UserViewModel::class.java)
        notificationViewModel = ViewModelProvider(this, viewModelFactory!!).get(NotificationViewModel::class.java)
        shopViewModel = ViewModelProvider(this, viewModelFactory!!).get(ShopViewModel::class.java)
        basketViewModel = ViewModelProvider(this, viewModelFactory!!).get(BasketViewModel::class.java)
    }

    fun refreshBasketCount() {
        basketViewModel!!.setBasketListObj()
    }

    /**
     * Show alert message to user.
     *
     * @param msg Message to show to user
     */
    private fun showAlertMessage(msg: String?) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.ps_dialog, null)
        builder.setView(view)
                .setPositiveButton(getString(R.string.app__ok), null)
        val message = view.findViewById<TextView>(R.id.messageTextView)
        message.text = msg
        builder.create()
        builder.show()
    }

    /**
     * This function will initialize UI and Event Listeners
     */
    private fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(this, false)

        // binding.getRoot().startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        initToolbar(binding!!.toolbar, resources.getString(R.string.app__app_name))
        initDrawerLayout()
        initNavigationView()
        //navigationController!!.navigateToHome(this)

        val fragment = MainFragment()
        this.supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commitAllowingStateLoss()

        showBottomNavigation()
        setSelectMenu(R.id.nav_home)
        val bottomNavigationMenuView = binding!!.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val bTMView = bottomNavigationMenuView.getChildAt(3)
        val itemView = bTMView as BottomNavigationItemView
        val badgeView = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true)
        basketNotificationTextView = badgeView.findViewById(R.id.notifications_badge)
        basketNotificationTextView!!.visibility = View.GONE
        binding!!.bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home_menu -> {
                    navigationController!!.navigateToHome(this)
                    setToolbarText(binding!!.toolbar, getString(R.string.app__app_name))
                }
                R.id.shop_profile_menu -> {
                    navigationController!!.navigateToShopProfile(this)
                    setToolbarText(binding!!.toolbar, getString(R.string.menu__shop_profile))
                }
                R.id.chkout_profile_menu -> {
                    navigationController!!.navigateToShopProfile(this)
                    setToolbarText(binding!!.toolbar, getString(R.string.menu__chkout_profile))
                }
                R.id.me_menu -> Utils.navigateOnUserVerificationFragment(pref, user, navigationController, this)
                R.id.search_menu -> {
                    navigationController!!.navigateToSearch(this)
                    setToolbarText(binding!!.toolbar, getString(R.string.menu__search))
                }
                else -> {
                    navigationController!!.navigateToShopProfile(this)
                    setToolbarText(binding!!.toolbar, getString(R.string.app__app_name))
                }
            }
            true
        }
    }

    private fun initDrawerLayout() {
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, binding!!.drawerLayout, binding!!.toolbar, R.string.app__drawer_open, R.string.app__drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }
        }
        binding!!.drawerLayout.addDrawerListener(drawerToggle)
        binding!!.drawerLayout.post { drawerToggle.syncState() }
    }

    private fun initNavigationView() {

        // Updating Custom Fonts
        val m: Menu = binding!!.navView.menu
        try {
            for (i in 0 until m.size()) {
                val mi = m.getItem(i)

                //for applying a font to subMenu ...
                val subMenu = mi.subMenu
                if (subMenu != null && subMenu.size() > 0) {
                    for (j in 0 until subMenu.size()) {
                        val subMenuItem = subMenu.getItem(j)
                        subMenuItem.title = subMenuItem.title
                        // update font
                        subMenuItem.title = Utils.getSpannableString(baseContext, subMenuItem.title.toString(), Utils.Fonts.ROBOTO)
                    }
                }
                mi.title = mi.title
                // update font
                mi.title = Utils.getSpannableString(baseContext, mi.title.toString(), Utils.Fonts.ROBOTO)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in Setting Custom Font", e)
        }
        binding!!.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            navigationMenuChanged(menuItem)
            true
        }
        if (binding!!.bottomNavigationView != null) {

            // Updating Custom Fonts
            val m = binding!!.bottomNavigationView.menu
            try {
                for (i in 0 until m.size()) {
                    val mi = m.getItem(i)

                    //for applying a font to subMenu ...
                    val subMenu = mi.subMenu
                    if (subMenu != null && subMenu.size() > 0) {
                        for (j in 0 until subMenu.size()) {
                            val subMenuItem = subMenu.getItem(j)
                            subMenuItem.title = subMenuItem.title
                            // update font
                            subMenuItem.title = Utils.getSpannableString(baseContext, subMenuItem.title.toString(), Utils.Fonts.ROBOTO)
                        }
                    }
                    mi.title = mi.title
                    // update font
                    mi.title = Utils.getSpannableString(baseContext, mi.title.toString(), Utils.Fonts.ROBOTO)
                }
            } catch (e: Exception) {
                Utils.psErrorLog("Error in Setting Custom Font", e)
            }
            binding!!.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
                navigationMenuChanged(menuItem)
                true
            }
        }
    }

    private fun checkForUpdates() {
        //1
        var appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        val appUpdateInfo = appUpdateManager.appUpdateInfo
        appUpdateInfo.addOnSuccessListener {
            //2
            handleUpdate(appUpdateManager, appUpdateInfo)
        }

        if (BuildConfig.DEBUG) {
            appUpdateManager = FakeAppUpdateManager(baseContext)
            appUpdateManager.setUpdateAvailable(2)
        } else {
            appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        }
    }

    private fun handleUpdate(manager: AppUpdateManager, info: com.google.android.play.core.tasks.Task<AppUpdateInfo>) {
        if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.IMMEDIATE) {
            handleImmediateUpdate(manager, info)
        } else if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.FLEXIBLE) {
            handleFlexibleUpdate(manager, info)
        }
    }

    private fun handleImmediateUpdate(manager: AppUpdateManager, info: com.google.android.play.core.tasks.Task<AppUpdateInfo>) {
        //1
        if ((info.result?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                        //2
                        info.result?.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) &&
                //3
                info.result!!.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            //4
            manager.startUpdateFlowForResult(info.result, AppUpdateType.IMMEDIATE, this, Companion.REQUEST_UPDATE)
        }

        if (BuildConfig.DEBUG) {
            val fakeAppUpdate = manager as FakeAppUpdateManager
            if (fakeAppUpdate.isImmediateFlowVisible) {
                fakeAppUpdate.userAcceptsUpdate()
                fakeAppUpdate.downloadStarts()
                fakeAppUpdate.downloadCompletes()
                launchRestartDialog(manager)
            }
        }
    }

    private fun handleFlexibleUpdate(manager: AppUpdateManager, info: com.google.android.play.core.tasks.Task<AppUpdateInfo>) {
        if ((info.result?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                        info.result?.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) &&
                info.result!!.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            //btn_update.visibility = View.VISIBLE
            //setUpdateAction(manager, info)
        }
    }

    private fun launchRestartDialog(manager: AppUpdateManager) {
        AlertDialog.Builder(this)
                .setTitle("An Update is available")
                //.setMessage(getString(R.string.update_message))
                .setPositiveButton("Restart") { _, _ ->
                    manager.completeUpdate()
                }
                .create().show()
    }



    private fun hideBottomNavigation() {
        binding!!.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding!!.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun navigationMenuChanged(menuItem: MenuItem) {
        openFragment(menuItem.itemId)
        if (menuItem.itemId != R.id.nav_logout_login) {
            menuItem.isChecked = true
            binding!!.drawerLayout.closeDrawers()
        }
    }

    private fun setSelectMenu(id: Int) {
        binding!!.navView.setCheckedItem(id)
    }

    private var menuId = 0

    /**
     * Open Fragment
     *
     * @param menuId To know which fragment to open.
     */
    private fun openFragment(menuId: Int) {
        this.menuId = menuId
        when (menuId) {
            R.id.nav_home, R.id.nav_home_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__home))
                navigationController?.navigateToHome(this)
                showBottomNavigation()
            }
            R.id.nav_latest_product, R.id.nav_latest_product_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__latest_product))
                navigationController!!.navigateToLatestProducts(this, ProductParameterHolder().latestParameterHolder)
                Utils.psLog("nav_latest_product")
                //hideBottomNavigation()
            }
            R.id.nav_feature_product, R.id.nav_feature_product_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__featured_product))
                navigationController!!.navigateToFeatureProducts(this, ProductParameterHolder().featuredParameterHolder)
                Utils.psLog("nav_feature_product")
                //hideBottomNavigation()
            }
            R.id.nav_discount_product, R.id.nav_discount_product_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__discount))
                navigationController!!.navigateToDiscountProduct(this, ProductParameterHolder().discountParameterHolder)
                Utils.psLog("nav_discount_product")
                //hideBottomNavigation()
            }
            R.id.nav_category, R.id.nav_category_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__category))
                navigationController!!.navigateToCategory(this)
                Utils.psLog("nav_category")
                //hideBottomNavigation()
            }
            R.id.nav_danceoholics -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__danceoholics))
                navigationController!!.navigateToDanceoholics(this)
                Utils.psLog("nav_danceoholics")
                //hideBottomNavigation()
            }
            R.id.nav_profile_danceoholics -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__danceoholics))
                navigationController!!.navigateToDanceoholics(this)
                Utils.psLog("nav_danceoholics")
                //hideBottomNavigation()
            }
            R.id.nav_trending_products, R.id.nav_trending_products_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__trending_products))
                navigationController!!.navigateToTrendingProducts(this, ProductParameterHolder().trendingParameterHolder)
                Utils.psLog("nav_trending_products")
                //hideBottomNavigation()
            }
            R.id.nav_productCollectionHeaderList, R.id.nav_productCollectionHeaderList_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__collections))
                navigationController!!.navigateToCollectionList(this)
                Utils.psLog("nav_productCollectionHeaderList")
                //hideBottomNavigation()
            }
            R.id.nav_profile, R.id.nav_profile_login -> {
                Utils.navigateOnUserVerificationFragment(pref, user, navigationController, this)
                Utils.psLog("nav_profile")
                hideBottomNavigation()
            }
            R.id.nav_favourite_news_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__favourite_items))
                navigationController!!.navigateToFavourite(this)
                Utils.psLog("nav_favourite_news")
                //hideBottomNavigation()
            }
            R.id.nav_user_history_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__user_history))
                navigationController!!.navigateToHistory(this)
                Utils.psLog("nav_history")
               //hideBottomNavigation()
            }
            R.id.nav_logout_login -> {
                psDialogMsg!!.showConfirmDialog(getString(R.string.edit_setting__logout_question), getString(R.string.app__ok), getString(R.string.app__cancel))
                psDialogMsg!!.show()
                psDialogMsg!!.okButton.setOnClickListener { view: View? ->
                    psDialogMsg!!.cancel()
                    //hideBottomNavigation()
                    userViewModel!!.deleteUserLogin(user).observe(this, Observer { status: Resource<Boolean?>? ->
                        if (status != null) {
                            this.menuId = 0
                            setToolbarText(binding!!.toolbar, getString(R.string.app__app_name))
                            isLogout = true

        //                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut()
                            val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build()
                            val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
                            googleSignInClient.signOut().addOnCompleteListener(this) { }
                        }
                    })
                    Utils.psLog("nav_logout_login")
                }
                psDialogMsg!!.cancelButton.setOnClickListener({ view: View? -> psDialogMsg!!.cancel() })
            }
            R.id.nav_setting, R.id.nav_setting_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__setting))
                navigationController!!.navigateToSetting(this)
                Utils.psLog("nav_setting")
                //hideBottomNavigation()
            }
            R.id.nav_language, R.id.nav_language_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__language))
                navigationController!!.navigateToLanguageSetting(this)
                Utils.psLog("nav_language")
                //hideBottomNavigation()
            }
            R.id.nav_rate_this_app, R.id.nav_rate_this_app_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__rate))
                navigationController!!.navigateToPlayStore(this)
                //hideBottomNavigation()
            }
            R.id.nav_contact_us, R.id.nav_contact_us_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.menu__contact_us))
                navigationController!!.navigateToContactUs(this)
                //hideBottomNavigation()
            }
            R.id.nav_privacy_policy, R.id.nav_privacy_policy_login -> {
                setToolbarText(binding!!.toolbar, getString(R.string.terms_and_conditions__title))
                navigationController!!.navigateToPrivacyPolicy(this)
              //  hideBottomNavigation()
            }
        }
    }

/*
    private fun chooseProfileFragment() {
        val fragmentType = pref!!.getString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING)
        if (fragmentType!!.isEmpty()) {
            if (user == null) {
                navigationController!!.navigateToUserLogin(this)
            } else {
                navigationController!!.navigateToUserProfile(this)
            }
        } else {
            navigationController!!.navigateToVerifyEmail(this)
        }
    }*/

    /**
     * Initialize Data
     */
    private fun initData() {
        try {
            notiSetting = pref!!.getBoolean(Constants.NOTI_SETTING, false)
            token = pref!!.getString(Constants.NOTI_TOKEN, "")
        } catch (ne: NullPointerException) {
            Utils.psErrorLog("Null Pointer Exception.", ne)
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting notification flag data.", e)
        }
        userViewModel!!.setLoginUser()
        userViewModel!!.loginUser.observe(this, Observer { data: List<UserLogin>? ->
            if (data != null) {
                if (data.isNotEmpty()) {
                    user = data[0].user
                    pref!!.edit().putString(Constants.USER_ID, user!!.userId).apply()
                    pref!!.edit().putString(Constants.USER_NAME, user!!.userName).apply()
                    pref!!.edit().putString(Constants.USER_EMAIL, user!!.userEmail).apply()
                } else {
                    user = null
                    pref!!.edit().remove(Constants.USER_ID).apply()
                    pref!!.edit().remove(Constants.USER_NAME).apply()
                    pref!!.edit().remove(Constants.USER_EMAIL).apply()
                }
            } else {
                user = null
                pref!!.edit().remove(Constants.USER_ID).apply()
                pref!!.edit().remove(Constants.USER_NAME).apply()
                pref!!.edit().remove(Constants.USER_EMAIL).apply()
            }
            updateMenu()
            if (isLogout) {
                setToolbarText(binding!!.toolbar, getString(R.string.app__app_name))
                //                showBottomNavigation();
                navigationController!!.navigateToHome(this@MainActivity)
                showBottomNavigation()
                isLogout = false
            }
        })
        registerNotificationToken() // Just send "" because don't have token to sent. It will get token itself.
        shopViewModel!!.setShopObj(Config.API_KEY)
        shopViewModel!!.shopData.observe(this, Observer { resource ->
            if (resource != null) {
                Utils.psLog("Got Data" + resource.message + resource.toString())
                when (resource.status) {
                    Status.SUCCESS ->                             // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            pref!!.edit().putString(Constants.SHOP_PHONE_NUMBER, resource.data.aboutPhone1.toString()).apply()
                            pref!!.edit().putString(Constants.SHOP_ID, resource.data.id).apply()
                            pref!!.edit().putString(Constants.SHOP_STANDARD_SHIPPING_ENABLE, resource.data.standardShippingEnable).apply()
                            pref!!.edit().putString(Constants.SHOP_ZONE_SHIPPING_ENABLE, resource.data.zoneShippingEnable).apply()
                            pref!!.edit().putString(Constants.SHOP_NO_SHIPPING_ENABLE, resource.data.noShippingEnable).apply()
                        }
                    Status.ERROR -> {
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.")
            } else {
                Utils.psLog("No Data of About Us.")
            }
        })
        /*basketViewModel!!.allBasketList.observe(this, Observer { resourse: List<Basket>? ->
            if (resourse != null) {
                var total = 0
                for (i in resourse.indices) {
                    total += resourse[i].count
                }
                if (total == 0) {
                    basketNotificationTextView!!.visibility = View.GONE
                } else {
                    basketNotificationTextView!!.visibility = View.VISIBLE
                    basketNotificationTextView!!.text = total.toString()
                }
            }
        })*/
    }

    /**
     * This function will change the menu based on the user is logged in or not.
     */
    private fun updateMenu() {
        if (user == null) {
            binding!!.navView.menu.setGroupVisible(R.id.group_before_login, true)
            binding!!.navView.menu.setGroupVisible(R.id.group_after_login, false)
            setSelectMenu(R.id.nav_home)
        } else {
            binding!!.navView.menu.setGroupVisible(R.id.group_after_login, true)
            binding!!.navView.menu.setGroupVisible(R.id.group_before_login, false)
            when (menuId) {
                R.id.nav_profile -> {
                    setSelectMenu(R.id.nav_profile_login)
                }
                R.id.nav_profile_login -> {
                    setSelectMenu(R.id.nav_profile_login)
                }
                else -> {
                    setSelectMenu(R.id.nav_home_login)
                }
            }
        }
    }

    private fun registerNotificationToken() {
        /*
         * Register Notification
         */

        // Check already submit or not
        // If haven't, submit to server
        if (!notiSetting) {
            if ((token == "")) {
                FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener { task: Task<InstanceIdResult?> ->
                            if (!task.isSuccessful) {
                                return@addOnCompleteListener
                            }

                            // Get new Instance ID token
                            if (task.result != null) {
                                token1 = task.result!!.token
                            }
                            notificationViewModel!!.registerNotification(baseContext, Constants.PLATFORM, token1)
                        }
            }
        } else {
            Utils.psLog("Notification Token is already registered. Notification Setting : true.")
        }
    }

    private fun checkConsentStatus() {

        // For Testing Open this
//        ConsentInformation.getInstance(this).
//                setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        val consentInformation = ConsentInformation.getInstance(this)
        val publisherIds = arrayOf(getString(R.string.adview_publisher_key))
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                // User's consent status successfully updated.
                Utils.psLog(consentStatus.name)
                if (consentStatus.name != pref!!.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS) || (consentStatus.name == Config.CONSENTSTATUS_UNKNOWN)) {
                    collectConsent()
                }
            }

            override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                // User's consent status failed to update.
                Utils.psLog("Failed to update!")
            }
        })
    }

    private fun collectConsent() {
        var privacyUrl: URL? = null
        try {
            privacyUrl = URL(Config.POLICY_URL)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            // Handle error.
        }
        form = ConsentForm.Builder(this, privacyUrl)
                .withListener(object : ConsentFormListener() {
                    override fun onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Utils.psLog("Form loaded")
                        if (form != null) {
                            form!!.show()
                        }
                    }

                    override fun onConsentFormOpened() {
                        // Consent form was displayed.
                        Utils.psLog("Form Opened")
                    }

                    override fun onConsentFormClosed(
                            consentStatus: ConsentStatus, userPrefersAdFree: Boolean) {
                        // Consent form was closed.
                        pref!!.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name).apply()
                        pref!!.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply()
                        Utils.psLog("Form Closed")
                    }

                    override fun onConsentFormError(errorDescription: String) {
                        // Consent form error.
                        pref!!.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply()
                        Utils.psLog("Form Error $errorDescription")
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build()
        form!!.load()
    }

    //endregion
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notification_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_blog -> {
                navigationController!!.navigateToBlogList(this)

            }
            R.id.action_notification -> {
                navigationController!!.navigateToNotificationList(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.IMMEDIATE
        private const val REQUEST_UPDATE = 100
    }
}