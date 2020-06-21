package com.dewonderstruck.apps.ashx0.ui.common

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.di.Injectable
import com.dewonderstruck.apps.ashx0.utils.Connectivity
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import javax.inject.Inject

/**
 * Parent class for all fragment in this project.
 * Created by Vamsi Madduluri on 12/2/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
abstract class PSFragment : Fragment(), Injectable {
    //region Variables
    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @JvmField
    @Inject
    var navigationController: NavigationController? = null

    @JvmField
    @Inject
    var connectivity: Connectivity? = null


    @Inject
    open var pref: SharedPreferences? = null
    @JvmField
    protected var loginUserId: String? = null
    protected var shippingId: String? = null
    @JvmField
    protected var shippingTax: String? = null
    @JvmField
    protected var overAllTax: String? = null
    @JvmField
    protected var shippingTaxLabel: String? = null
    @JvmField
    protected var overAllTaxLabel: String? = null
    protected var versionNo: String? = null
    protected var force_update = false
    protected var force_update_msg: String? = null
    protected var force_update_title: String? = null
    private var isFadeIn = false
    @JvmField
    protected var cod: String? = null
    @JvmField
    protected var paypal: String? = null
    @JvmField
    protected var stripe: String? = null
    @JvmField
    protected var messenger: String? = null
    @JvmField
    protected var whatsappNo: String? = null
    protected var consent_status: String? = null
    protected var userEmailToVerify: String? = null
    protected var userPasswordToVerify: String? = null
    protected var userNameToVerify: String? = null
    @JvmField
    protected var userIdToVerify: String? = null
    @JvmField
    protected var shopId: String? = null
    @JvmField
    protected var shopPhoneNumber: String? = null
    protected var shopStandardShippingEnable: String? = null
    protected var shopNoShippingEnable: String? = null
    @JvmField
    protected var shopZoneShippingEnable: String? = null

    //endregion
    //region Override Methods
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadLoginUserId()
        initViewModels()
        initUIAndActions()
        initAdapters()
        initData()
    }

    //endregion
    //region Methods
    protected fun loadLoginUserId() {
        try {
            if (activity != null && requireActivity().baseContext != null) {
                loginUserId = pref!!.getString(Constants.USER_ID, Constants.EMPTY_STRING)
                shippingId = pref!!.getString(Constants.SHIPPING_ID, "")
                shippingTax = pref!!.getString(Constants.PAYMENT_SHIPPING_TAX, "")
                versionNo = pref!!.getString(Constants.APPINFO_PREF_VERSION_NO, "")
                force_update = pref!!.getBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false)
                force_update_msg = pref!!.getString(Constants.APPINFO_FORCE_UPDATE_MSG, "")
                force_update_title = pref!!.getString(Constants.APPINFO_FORCE_UPDATE_TITLE, "")
                overAllTax = pref!!.getString(Constants.PAYMENT_OVER_ALL_TAX, Constants.EMPTY_STRING)
                overAllTaxLabel = pref!!.getString(Constants.PAYMENT_OVER_ALL_TAX_LABEL, Constants.EMPTY_STRING)
                shippingTaxLabel = pref!!.getString(Constants.PAYMENT_SHIPPING_TAX_LABEL, Constants.EMPTY_STRING)
                cod = pref!!.getString(Constants.PAYMENT_CASH_ON_DELIVERY, Constants.ZERO)
                paypal = pref!!.getString(Constants.PAYMENT_PAYPAL, Constants.ZERO)
                stripe = pref!!.getString(Constants.PAYMENT_STRIPE, Constants.ZERO)
                messenger = pref!!.getString(Constants.MESSENGER, Constants.ZERO)
                whatsappNo = pref!!.getString(Constants.WHATSAPP, Constants.ZERO)
                consent_status = pref!!.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS)
                userEmailToVerify = pref!!.getString(Constants.USER_EMAIL_TO_VERIFY, Constants.EMPTY_STRING)
                userPasswordToVerify = pref!!.getString(Constants.USER_PASSWORD_TO_VERIFY, Constants.EMPTY_STRING)
                userNameToVerify = pref!!.getString(Constants.USER_NAME_TO_VERIFY, Constants.EMPTY_STRING)
                userIdToVerify = pref!!.getString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING)
                shopId = pref!!.getString(Constants.SHOP_ID, Constants.EMPTY_STRING)
                shopPhoneNumber = pref!!.getString(Constants.SHOP_PHONE_NUMBER, Constants.EMPTY_STRING)
                shopNoShippingEnable = pref!!.getString(Constants.SHOP_NO_SHIPPING_ENABLE, Constants.EMPTY_STRING)
                shopZoneShippingEnable = pref!!.getString(Constants.SHOP_ZONE_SHIPPING_ENABLE, Constants.EMPTY_STRING)
                shopStandardShippingEnable = pref!!.getString(Constants.SHOP_STANDARD_SHIPPING_ENABLE, Constants.EMPTY_STRING)
            }
        } catch (ne: NullPointerException) {
            Utils.psErrorLog("Null Pointer Exception.", ne)
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting notification flag data.", e)
        }
    }

    protected abstract fun initUIAndActions()
    protected abstract fun initViewModels()
    protected abstract fun initAdapters()
    protected abstract fun initData()
    protected fun fadeIn(view: View) {
        if (!isFadeIn) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
            isFadeIn = true // Fade in will do only one time.
        }
    } //endregion
}