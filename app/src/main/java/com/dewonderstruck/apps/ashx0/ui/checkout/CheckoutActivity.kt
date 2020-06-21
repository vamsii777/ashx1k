package com.dewonderstruck.apps.ashx0.ui.checkout

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityCheckoutBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.holder.TransactionValueHolder

class CheckoutActivity : PSAppCompactActivity() {
    @JvmField
    var number = 1
    private val maxNumber = 4
    var currentUser: User? = null
    var fragment: PSFragment? = null
    @JvmField
    var binding: ActivityCheckoutBinding? = null
    @JvmField
    var progressDialog: ProgressDialog? = null
    private var psDialogMsg: PSDialogMsg? = null
    @JvmField
    var transactionValueHolder: TransactionValueHolder? = null
    @JvmField
    var transactionObject: TransactionObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout)

        // Init all UI
        initUI(binding)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE__MAIN_ACTIVITY
                && resultCode == Constants.RESULT_CODE__RESTART_MAIN_ACTIVITY) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
            fragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true))
    }

    private fun initUI(binding: ActivityCheckoutBinding?) {
        transactionValueHolder = TransactionValueHolder()
        psDialogMsg = PSDialogMsg(this, false)

        // click close image button
        binding!!.closeImageButton.setOnClickListener { view: View? -> finish() }

        // fragment1 default initialize
        navigateFragment(binding, number)
        binding.nextButton.setOnClickListener { view: View? ->
            if (number < maxNumber) {
                number++
                if (number == 3) {
                    navigateFragment(binding, number)
                } else if (number == 2) {
                    if (currentUser!!.city.id.isEmpty()) {
                        psDialogMsg!!.showErrorDialog(getString(R.string.error_message__select_city), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        number--
                    } else if ((fragment as CheckoutFragment1?)!!.checkShippingAddressEditTextIsEmpty()) {
                        psDialogMsg!!.showErrorDialog(getString(R.string.shipping_address_one_error_message), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        number--
                    } else if ((fragment as CheckoutFragment1?)!!.checkBillingAddressEditTextIsEmpty()) {
                        psDialogMsg!!.showErrorDialog(getString(R.string.billing_address_one_error_message), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        number--
                    } else if ((fragment as CheckoutFragment1?)!!.checkUserEmailEditTextIsEmpty()) {
                        psDialogMsg!!.showErrorDialog(getString(R.string.checkout__user_email_empty), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        number--
                    } else {
                        (fragment as CheckoutFragment1?)!!.updateUserProfile()
                    }
                } else if (number == 4) {
                    if ((fragment as CheckoutFragment3?)!!.binding.get().checkBox.isChecked) {
                        number--
                        if ((fragment as CheckoutFragment3?)!!.clicked) {
                            psDialogMsg!!.showConfirmDialog(getString(R.string.confirm_to_Buy), getString(R.string.app__ok), getString(R.string.app__cancel))
                            psDialogMsg!!.show()
                            psDialogMsg!!.okButton.setOnClickListener { v: View? ->
                                psDialogMsg!!.cancel()
                                when ((fragment as CheckoutFragment3?)!!.paymentMethod) {
                                    Constants.PAYMENT_PAYPAL -> (fragment as CheckoutFragment3?)!!.token
                                    Constants.PAYMENT_CASH_ON_DELIVERY -> (fragment as CheckoutFragment3?)!!.sendData()
                                    Constants.PAYMENT_STRIPE -> navigationController.navigateToStripeActivity(this@CheckoutActivity)
                                    Constants.PAYMENT_BANK -> (fragment as CheckoutFragment3?)!!.sendData()
                                }
                            }
                            psDialogMsg!!.cancelButton.setOnClickListener { v: View? -> psDialogMsg!!.cancel() }
                        } else {
                            psDialogMsg!!.showErrorDialog(getString(R.string.checkout__choose_a_method), getString(R.string.app__ok))
                            psDialogMsg!!.show()
                        }
                    } else {
                        number--
                        psDialogMsg!!.showInfoDialog(getString(R.string.not_checked), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                    }
                } else {
                    navigateFragment(binding, number)
                }
            }
        }
        binding.prevButton.setOnClickListener { view: View? ->
            if (number > 1) {
                number--
                binding.shippingImageView.setImageResource(R.drawable.baseline_circle_line_uncheck_24)
                binding.paymentImageView.setImageResource(R.drawable.baseline_circle_black_uncheck_24)
                navigateFragment(binding, number)
            }
        }
        binding.keepShoppingButton.setOnClickListener { v: View? ->
            navigationController.navigateBackToBasketActivity(this@CheckoutActivity)
            finish()
        }
    }

    fun navigateFragment(binding: ActivityCheckoutBinding?, position: Int) {
        updateCheckoutUI(binding)
        if (position == 1) {
            val fragment = CheckoutFragment1()
            this.fragment = fragment
            setupFragment(fragment)
        } else if (position == 2) {
            val fragment = CheckoutFragment2()
            this.fragment = fragment
            setupFragment(fragment)
        } else if (position == 3) {
            val fragment = CheckoutFragment3()
            this.fragment = fragment
            setupFragment(fragment)
        } else if (position == 4) {
            setupFragment(CheckoutStatusFragment())
        }
    }

    private fun updateCheckoutUI(binding: ActivityCheckoutBinding?) {
        if (number == 1) {
            binding!!.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.GONE
            binding.keepShoppingButton.visibility = View.GONE
            binding.step2View.setBackgroundColor(resources.getColor(R.color.md_grey_300))
            binding.step3View.setBackgroundColor(resources.getColor(R.color.md_grey_300))
            binding.nextButton.setText(R.string.checkout__next)
        } else if (number == 2) {
            binding!!.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.VISIBLE
            binding.step2View.setBackgroundColor(resources.getColor(R.color.global__primary))
            binding.step3View.setBackgroundColor(resources.getColor(R.color.md_grey_300))
            binding.keepShoppingButton.visibility = View.GONE
            binding.paymentImageView.setImageResource(R.drawable.baseline_circle_line_uncheck_24)
            binding.shippingImageView.setImageResource(R.drawable.baseline_circle_line_check_24)
            binding.nextButton.setText(R.string.checkout__next)
            binding.prevButton.setText(R.string.back)
        } else if (number == 3) {
            binding!!.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.VISIBLE
            binding.keepShoppingButton.visibility = View.GONE
            binding.step3View.setBackgroundColor(resources.getColor(R.color.global__primary))
            binding.paymentImageView.setImageResource(R.drawable.baseline_circle_line_check_24)
            binding.successImageView.setImageResource(R.drawable.baseline_circle_line_uncheck_24)
            binding.nextButton.setText(R.string.checkout)
            binding.prevButton.setText(R.string.back)
        } else if (number == 4) {
            binding!!.constraintLayout25.visibility = View.GONE
            binding.nextButton.visibility = View.GONE
            binding.prevButton.visibility = View.GONE
            binding.keepShoppingButton.visibility = View.VISIBLE
            binding.paymentImageView.setImageResource(R.drawable.baseline_circle_line_check_24)
            binding.successImageView.setImageResource(R.drawable.baseline_circle_line_check_24)
        }
    }

    fun goToFragment4() {
        navigateFragment(binding, 4)
        number = 4
    }

    override fun onBackPressed() {}
}