package com.dewonderstruck.apps.ashx0.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.CheckoutFragment2Binding
import com.dewonderstruck.apps.ashx0.ui.checkout.adapter.ShippingMethodsAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.coupondiscount.CouponDiscountViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shippingmethod.ShippingMethodViewModel
import com.dewonderstruck.apps.ashx0.viewobject.*
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class CheckoutFragment2 : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var basketViewModel: BasketViewModel? = null
    private var shippingMethodViewModel: ShippingMethodViewModel? = null
    private var couponDiscountViewModel: CouponDiscountViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<CheckoutFragment2Binding>? = null
    private var adapter: AutoClearedValue<ShippingMethodsAdapter>? = null
    private var psDialogMsg: PSDialogMsg? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: CheckoutFragment2Binding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_2, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(this.activity, false)
        if (activity is CheckoutActivity) {
            (activity as CheckoutActivity?)!!.progressDialog!!.setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
            (activity as CheckoutActivity?)!!.progressDialog!!.setCancelable(false)
        }
        binding!!.get().couponDiscountButton.setOnClickListener { v: View? ->
            if (this@CheckoutFragment2.activity != null) {
                (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.couponDiscountText = binding!!.get().couponDiscountValueEditText.text.toString()
            }
            couponDiscountViewModel!!.setCouponDiscountObj(binding!!.get().couponDiscountValueEditText.text.toString())
            if (activity != null && activity is CheckoutActivity) {
                (activity as CheckoutActivity?)!!.progressDialog!!.setMessage(getString(R.string.check_coupon))
                (activity as CheckoutActivity?)!!.progressDialog!!.show()
            }
        }
        if (!overAllTaxLabel.isEmpty()) {
            binding!!.get().overAllTaxTextView.text = getString(R.string.tax, overAllTaxLabel)
        }
        if (!shippingTaxLabel.isEmpty()) {
            binding!!.get().shippingTaxTextView.text = getString(R.string.shipping_tax, shippingTaxLabel)
        }
    }

    override fun initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
        shippingMethodViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShippingMethodViewModel::class.java)
        couponDiscountViewModel = ViewModelProviders.of(this, viewModelFactory).get(CouponDiscountViewModel::class.java)
    }

    override fun initAdapters() {
        if (activity != null) {
            val nvAdapter = ShippingMethodsAdapter(dataBindingComponent, ShippingMethodsAdapter.NewsClickCallback { shippingMethod: ShippingMethod ->
                if (this@CheckoutFragment2.activity != null) {
                    (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost = Utils.round(shippingMethod.price, 2)
                    (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shippingMethodName = shippingMethod.name
                    (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.selectedShippingId = shippingMethod.id
                    calculateTheBalance()
                }
            }, shippingId, (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.selectedShippingId)
            adapter = AutoClearedValue(this, nvAdapter)
            binding!!.get().shippingMethodsRecyclerView.adapter = adapter!!.get()
        }
    }

    override fun initData() {
        if (shopNoShippingEnable == Constants.ONE) {
            binding!!.get().shippingMethodsCardView.visibility = View.GONE
        }
        if (shopStandardShippingEnable == Constants.ONE) {
            binding!!.get().shippingMethodsCardView.visibility = View.VISIBLE
            shippingMethodViewModel!!.setShippingMethodsObj()
        }
        shippingMethodViewModel!!.getshippingCostByCountryAndCityData().observe(this, Observer { result: Resource<ShippingCost?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
//                    progressDialog.get().cancel();
                    if (activity != null && activity is CheckoutActivity) {
                        (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                    }
                    if (result.data != null) {
                        if (this@CheckoutFragment2.activity != null) {
                            (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shippingMethodName = result.data.shippingZone.shippingZonePackageName
                            (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost = result.data.shippingZone.shippingCost.toFloat()
                            calculateTheBalance()
                        }
                    }
                } else if (result.status == Status.ERROR) {
                    if (activity != null && activity is CheckoutActivity) {
                        (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                    }
                }
            }
        })
        shippingMethodViewModel!!.shippingMethodsData.observe(this, Observer { result: Resource<List<ShippingMethod>> ->
            if (result.data != null) {
                when (result.status) {
                    Status.SUCCESS -> {
                        replaceShippingMethods(result.data)
                        for (shippingMethod in result.data) {
                            if (this@CheckoutFragment2.activity != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id == shippingId && (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.selectedShippingId.isEmpty()) {
                                        if (this@CheckoutFragment2.activity != null) {
                                            if (shopNoShippingEnable == Constants.ONE) {
                                                (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost = 0f
                                            } else {
                                                (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost = Utils.round(shippingMethod.price, 2)
                                            }
                                            calculateTheBalance()
                                        }
                                        break
                                    }
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                        replaceShippingMethods(result.data)
                        for (shippingMethod in result.data) {
                            if (this@CheckoutFragment2.activity != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id == shippingId && (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.selectedShippingId.isEmpty()) {
                                        if (this@CheckoutFragment2.activity != null) {
                                            if (shopNoShippingEnable == Constants.ONE) (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost = 0f else {
                                                (this@CheckoutFragment2.activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost = Utils.round(shippingMethod.price, 2)
                                            }
                                            calculateTheBalance()
                                        }
                                        break
                                    }
                                }
                            }
                        }
                    }
                    else -> throw IllegalStateException("Unexpected value: " + result.status)
                }
            }
        })
        couponDiscountViewModel!!.couponDiscountData.observe(this, Observer { result: Resource<CouponDiscount?>? ->
            if (result != null) {
                when (result.status) {
                    Status.ERROR -> {
                        if (activity != null && activity is CheckoutActivity) {
                            (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }
                        psDialogMsg!!.showErrorDialog(getString(R.string.error_coupon), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                    }
                    Status.SUCCESS -> if (result.data != null) {
                        if (activity != null && activity is CheckoutActivity) {
                            (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }
                        psDialogMsg!!.showSuccessDialog(getString(R.string.checkout_detail__claimed_coupon), getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        if (activity != null) {
                            (activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount = Utils.round(result.data.couponAmount.toFloat(), 2)
                            Utils.psLog("coupon5" + (activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount + "")
                        }
                        calculateTheBalance()
                    }
                }
            }
        })
        basketViewModel!!.setBasketListWithProductObj()
        basketViewModel!!.allBasketWithProductList.observe(this, Observer { baskets: List<Basket>? ->
            if (baskets != null && baskets.size > 0) {
                if (activity != null) {
                    (activity as CheckoutActivity?)!!.transactionValueHolder!!.resetValues()
                    for (basket in baskets) {
                        (activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count += basket.count
                        (activity as CheckoutActivity?)!!.transactionValueHolder!!.total += Utils.round(basket.basketOriginalPrice * basket.count, 2)
                        (activity as CheckoutActivity?)!!.transactionValueHolder!!.discount += Utils.round(basket.product.discountAmount * basket.count, 2)
                        (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol = basket.product.currencySymbol
                        val shippingProductContainer = ShippingProductContainer(
                                basket.product.id,
                                basket.count
                        )
                        shippingMethodViewModel!!.shippingProductContainer.add(shippingProductContainer)
                    }
                    if (shopZoneShippingEnable == Constants.ONE) {
                        binding!!.get().shippingMethodsCardView.visibility = View.GONE
                        if (activity != null) {
                            //progressDialog.get().show();
                            if (activity != null && activity is CheckoutActivity) {
                                (activity as CheckoutActivity?)!!.progressDialog!!.show()
                                Utils.psLog((activity as CheckoutActivity?)!!.currentUser!!.country.id + " - " + (activity as CheckoutActivity?)!!.currentUser!!.city.id + " - " + shopId)
                                shippingMethodViewModel!!.setshippingCostByCountryAndCityObj(ShippingCostContainer(
                                        (activity as CheckoutActivity?)!!.currentUser!!.country.id, (activity as CheckoutActivity?)!!.currentUser!!.city.id, shopId,
                                        shippingMethodViewModel!!.shippingProductContainer))
                            }
                        }
                    }
                }
                calculateTheBalance()
            }
        })
        basketViewModel!!.wholeBasketDeleteData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    Utils.psLog("Success")
                } else if (result.status == Status.ERROR) {
                    Utils.psLog("Fail")
                }
            }
        })
    }

    private fun replaceShippingMethods(shippingMethods: List<ShippingMethod>) {
        adapter!!.get().replace(shippingMethods)
        binding!!.get().executePendingBindings()
    }

    private fun calculateTheBalance() {
        if (activity != null) {
            (activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total = Utils.round((activity as CheckoutActivity?)!!.transactionValueHolder!!.total - (activity as CheckoutActivity?)!!.transactionValueHolder!!.discount, 2)
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount > 0) {
                (activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total = Utils.round((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total - (activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount, 2)
            }
            if (!overAllTax.isEmpty()) {
                (activity as CheckoutActivity?)!!.transactionValueHolder!!.tax = Utils.round((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total * overAllTax.toFloat(), 2)
            }
            if (!shippingTax.isEmpty() && (activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost > 0) {
                (activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax = Utils.round((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost * shippingTax.toFloat(), 2)
            }
            (activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total = Utils.round((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total + (activity as CheckoutActivity?)!!.transactionValueHolder!!.tax +
                    (activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax + (activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost, 2)
            updateUI()
        }
    }

    private fun updateUI() {
        if (activity != null) {
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count > 0) {
                binding!!.get().totalItemCountValueTextView.text = (activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count.toString()
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.total > 0) {
                val totalValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.total.toDouble())
                binding!!.get().totalValueTextView.text = totalValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount > 0) {
                val couponDiscountValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount.toDouble())
                binding!!.get().couponDiscountValueTextView.text = couponDiscountValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.discount > 0) {
                val discountValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.discount.toDouble())
                binding!!.get().discountValueTextView.text = discountValueString
            }
            if (!(activity as CheckoutActivity?)!!.transactionValueHolder!!.couponDiscountText.isEmpty()) {
                val couponDiscountValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.couponDiscountText
                binding!!.get().couponDiscountValueEditText.setText(couponDiscountValueString)
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total > 0) {
                val subTotalValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total.toDouble())
                binding!!.get().subtotalValueTextView.text = subTotalValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.tax > 0) {
                val taxValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.tax.toDouble())
                binding!!.get().taxValueTextView.text = taxValueString
            }
            if (shippingTax != "0.0" && shippingTax != Constants.RATING_ZERO) {
                val shippingTaxValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format(Utils.round((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax, 2).toDouble())
                binding!!.get().shippingTaxValueTextView.text = shippingTaxValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total > 0.0) {
                val finalTotalValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total.toDouble())
                binding!!.get().finalTotalValueTextView.text = finalTotalValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost > 0) {
                val shippingCostValueString = (activity as CheckoutActivity?)!!.transactionValueHolder!!.currencySymbol + " " + Utils.format((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost.toDouble())
                binding!!.get().shippingCostValueTextView.text = shippingCostValueString
            }
        }
    }
}