package com.dewonderstruck.apps.ashx0.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.braintreepayments.api.models.PaymentMethodNonce
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.CheckoutFragment3Binding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.paypal.PaypalViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shop.ShopViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.transaction.TransactionListViewModel
import com.dewonderstruck.apps.ashx0.viewobject.*
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class CheckoutFragment3 : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var paypalViewModel: PaypalViewModel? = null
    var binding: AutoClearedValue<CheckoutFragment3Binding>? = null
    private var basketViewModel: BasketViewModel? = null
    private var transactionListViewModel: TransactionListViewModel? = null
    private var shopViewModel: ShopViewModel? = null
    private var user: User? = null
    private var basketList: List<Basket>? = null
    private val basketProductListToServerContainer = BasketProductListToServerContainer()
    private var clientTokenString: String? = null
    var paymentMethod = Constants.PAYMENT_CASH_ON_DELIVERY
    private var payment_method_nonce: String? = null
    private var oldCardView: CardView? = null
    private var oldTextView: TextView? = null
    private var oldImageView: ImageView? = null

    //    private ProgressDialog progressDialog;
    var clicked = false
    private var psDialogMsg: PSDialogMsg? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: CheckoutFragment3Binding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_3, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {
        binding!!.get().cashImageView.setColorFilter(resources.getColor(R.color.md_grey_500))
        if (activity is CheckoutActivity) {
            (activity as CheckoutActivity?)!!.progressDialog!!.setMessage(Utils.getSpannableString(context, getString(R.string.com_facebook_loading), Utils.Fonts.MM_FONT))
            (activity as CheckoutActivity?)!!.progressDialog!!.setCancelable(false)
        }
        binding!!.get().cashCardView.setOnClickListener { v: View? ->
            if (!clicked) {
                clicked = true
                oldCardView = binding!!.get().cashCardView
                oldImageView = binding!!.get().cashImageView
                oldTextView = binding!!.get().cashTextView
                changeToOrange(oldCardView!!, oldTextView!!, oldImageView!!)
            } else {
                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView!!, oldTextView!!, oldImageView!!)
                    changeToOrange(binding!!.get().cashCardView, binding!!.get().cashTextView, binding!!.get().cashImageView)
                    oldCardView = binding!!.get().cashCardView
                    oldImageView = binding!!.get().cashImageView
                    oldTextView = binding!!.get().cashTextView
                }
            }
            binding!!.get().warningTitleTextView.setText(R.string.checkout__information__COD)
            paymentMethod = Constants.PAYMENT_CASH_ON_DELIVERY
        }
        binding!!.get().cardCardView.setOnClickListener { v: View? ->
            if (!clicked) {
                clicked = true
                oldCardView = binding!!.get().cardCardView
                oldImageView = binding!!.get().cardImageView
                oldTextView = binding!!.get().cardTextView
                changeToOrange(oldCardView!!, oldTextView!!, oldImageView!!)
            } else {
                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView!!, oldTextView!!, oldImageView!!)
                    changeToOrange(binding!!.get().cardCardView, binding!!.get().cardTextView, binding!!.get().cardImageView)
                    oldCardView = binding!!.get().cardCardView
                    oldImageView = binding!!.get().cardImageView
                    oldTextView = binding!!.get().cardTextView
                }
            }
            binding!!.get().warningTitleTextView.setText(R.string.checkout__information__STRIPE)
            paymentMethod = Constants.PAYMENT_STRIPE
        }
        psDialogMsg = PSDialogMsg(this.activity, false)
        binding!!.get().paypalCardView.setOnClickListener { v: View? ->
            if (!clicked) {
                clicked = true
                oldCardView = binding!!.get().paypalCardView
                oldImageView = binding!!.get().paypalImageView
                oldTextView = binding!!.get().paypalTextView
                changeToOrange(oldCardView!!, oldTextView!!, oldImageView!!)
            } else {
                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView!!, oldTextView!!, oldImageView!!)
                    changeToOrange(binding!!.get().paypalCardView, binding!!.get().paypalTextView, binding!!.get().paypalImageView)
                    oldCardView = binding!!.get().paypalCardView
                    oldImageView = binding!!.get().paypalImageView
                    oldTextView = binding!!.get().paypalTextView
                }
            }
            binding!!.get().warningTitleTextView.setText(R.string.checkout__information__PAYPAL)
            paymentMethod = Constants.PAYMENT_PAYPAL
        }
        binding!!.get().bankCardView.setOnClickListener { v: View? ->
            if (!clicked) {
                clicked = true
                oldCardView = binding!!.get().bankCardView
                oldImageView = binding!!.get().bankImageView
                oldTextView = binding!!.get().bankTextView
                changeToOrange(oldCardView!!, oldTextView!!, oldImageView!!)
            } else {
                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView!!, oldTextView!!, oldImageView!!)
                    changeToOrange(binding!!.get().bankCardView, binding!!.get().bankTextView, binding!!.get().bankImageView)
                    oldCardView = binding!!.get().bankCardView
                    oldImageView = binding!!.get().bankImageView
                    oldTextView = binding!!.get().bankTextView
                }
            }
            binding!!.get().warningTitleTextView.setText(R.string.checkout__information__PAYPAL)
            paymentMethod = Constants.PAYMENT_BANK
        }
        if (cod == Constants.ONE) {
            binding!!.get().cashCardView.visibility = View.VISIBLE
            if (binding!!.get().noPaymentTextView.visibility == View.VISIBLE) {
                binding!!.get().noPaymentTextView.visibility = View.GONE
            }
        }
        if (paypal == Constants.ONE) {
            binding!!.get().paypalCardView.visibility = View.VISIBLE
            if (binding!!.get().noPaymentTextView.visibility == View.VISIBLE) {
                binding!!.get().noPaymentTextView.visibility = View.GONE
            }
        }
        if (stripe == Constants.ONE) {
            binding!!.get().cardCardView.visibility = View.VISIBLE
            if (binding!!.get().noPaymentTextView.visibility == View.VISIBLE) {
                binding!!.get().noPaymentTextView.visibility = View.GONE
            }
        }
    }

    override fun initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel::class.java)
        paypalViewModel = ViewModelProviders.of(this, viewModelFactory).get(PaypalViewModel::class.java)
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel::class.java)
    }

    override fun initAdapters() {}
    fun sendData() {
        if (activity != null) {
            user = (this@CheckoutFragment3.activity as CheckoutActivity?)!!.currentUser
            if (user!!.city!!.id.isEmpty()) {
                psDialogMsg!!.showErrorDialog(getString(R.string.error_message__select_city), getString(R.string.app__ok))
                psDialogMsg!!.show()
                return
            } else if (user!!.shippingAddress1.isEmpty()) {
                psDialogMsg!!.showErrorDialog(getString(R.string.shipping_address_one_error_message), getString(R.string.app__ok))
                psDialogMsg!!.show()
                return
            } else if (user!!.billingAddress1.isEmpty()) {
                psDialogMsg!!.showErrorDialog(getString(R.string.billing_address_one_error_message), getString(R.string.app__ok))
                psDialogMsg!!.show()
                return
            }
        }
        if (basketList != null) {
            if (basketList!!.size > 0) {
                if (activity != null) {
                    when (paymentMethod) {
                        Constants.PAYMENT_PAYPAL -> transactionListViewModel!!.setSendTransactionDetailDataObj(TransactionHeaderUpload(
                                user!!.userId,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total),
                                user!!.userName,
                                user!!.userPhone,
                                Constants.RATING_ZERO,
                                Constants.RATING_ONE,
                                Constants.RATING_ZERO,
                                Constants.RATING_ZERO,
                                payment_method_nonce!!,
                                Constants.RATING_ONE,
                                basketList!![0].product!!.currencySymbol,
                                basketList!![0].product!!.currencyShortForm,
                                user!!.billingFirstName,
                                user!!.billingLastName,
                                user!!.billingCompany,
                                user!!.billingAddress1,
                                user!!.billingAddress2,
                                user!!.billingCountry,
                                user!!.billingState,
                                user!!.billingCity,
                                user!!.billingPostalCode,
                                user!!.billingEmail,
                                user!!.billingPhone,
                                user!!.shippingFirstName,
                                user!!.shippingLastName,
                                user!!.shippingCompany,
                                user!!.shippingAddress1,
                                user!!.shippingAddress2,
                                user!!.shippingCountry,
                                user!!.shippingState,
                                user!!.shippingCity,
                                user!!.shippingPostalCode,
                                user!!.shippingEmail,
                                user!!.shippingPhone,
                                shippingTax!!,
                                overAllTax!!,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shippingMethodName),
                                binding!!.get().memoEditText.text.toString(),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count),
                                shopZoneShippingEnable!!,
                                basketProductListToServerContainer.productList
                        )
                        )
                        Constants.PAYMENT_STRIPE -> transactionListViewModel!!.setSendTransactionDetailDataObj(TransactionHeaderUpload(
                                user!!.userId,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total),
                                user!!.userName,
                                user!!.userPhone,
                                Constants.RATING_ZERO,
                                Constants.RATING_ZERO,
                                Constants.RATING_ONE,
                                Constants.RATING_ZERO,
                                payment_method_nonce!!,
                                Constants.RATING_ONE,
                                basketList!![0].product!!.currencySymbol,
                                basketList!![0].product!!.currencyShortForm,
                                user!!.billingFirstName,
                                user!!.billingLastName,
                                user!!.billingCompany,
                                user!!.billingAddress1,
                                user!!.billingAddress2,
                                user!!.billingCountry,
                                user!!.billingState,
                                user!!.billingCity,
                                user!!.billingPostalCode,
                                user!!.billingEmail,
                                user!!.billingPhone,
                                user!!.shippingFirstName,
                                user!!.shippingLastName,
                                user!!.shippingCompany,
                                user!!.shippingAddress1,
                                user!!.shippingAddress2,
                                user!!.shippingCountry,
                                user!!.shippingState,
                                user!!.shippingCity,
                                user!!.shippingPostalCode,
                                user!!.shippingEmail,
                                user!!.shippingPhone,
                                shippingTax!!,
                                overAllTax!!,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shippingMethodName),
                                binding!!.get().memoEditText.text.toString(),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count),
                                shopZoneShippingEnable!!,
                                basketProductListToServerContainer.productList
                        )
                        )
                        Constants.PAYMENT_CASH_ON_DELIVERY -> transactionListViewModel!!.setSendTransactionDetailDataObj(TransactionHeaderUpload(
                                user!!.userId,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total),
                                user!!.userName,
                                user!!.userPhone,
                                Constants.RATING_ONE,
                                Constants.RATING_ZERO,
                                Constants.RATING_ZERO,
                                Constants.RATING_ZERO,
                                payment_method_nonce!!,
                                Constants.RATING_ONE,
                                basketList!![0].product!!.currencySymbol,
                                basketList!![0].product!!.currencyShortForm,
                                user!!.billingFirstName,
                                user!!.billingLastName,
                                user!!.billingCompany,
                                user!!.billingAddress1,
                                user!!.billingAddress2,
                                user!!.billingCountry,
                                user!!.billingState,
                                user!!.billingCity,
                                user!!.billingPostalCode,
                                user!!.billingEmail,
                                user!!.billingPhone,
                                user!!.shippingFirstName,
                                user!!.shippingLastName,
                                user!!.shippingCompany,
                                user!!.shippingAddress1,
                                user!!.shippingAddress2,
                                user!!.shippingCountry,
                                user!!.shippingState,
                                user!!.shippingCity,
                                user!!.shippingPostalCode,
                                user!!.shippingEmail,
                                user!!.shippingPhone,
                                shippingTax!!,
                                overAllTax!!,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shippingMethodName),
                                binding!!.get().memoEditText.text.toString(),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count),
                                shopZoneShippingEnable!!,
                                basketProductListToServerContainer.productList
                        )
                        )
                        Constants.PAYMENT_BANK -> transactionListViewModel!!.setSendTransactionDetailDataObj(TransactionHeaderUpload(
                                user!!.userId,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.sub_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.coupon_discount),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_tax),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.final_total),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total),
                                user!!.userName,
                                user!!.userPhone,
                                Constants.RATING_ZERO,
                                Constants.RATING_ZERO,
                                Constants.RATING_ZERO,
                                Constants.RATING_ONE,
                                payment_method_nonce!!,
                                Constants.RATING_ONE,
                                basketList!![0].product!!.currencySymbol,
                                basketList!![0].product!!.currencyShortForm,
                                user!!.billingFirstName,
                                user!!.billingLastName,
                                user!!.billingCompany,
                                user!!.billingAddress1,
                                user!!.billingAddress2,
                                user!!.billingCountry,
                                user!!.billingState,
                                user!!.billingCity,
                                user!!.billingPostalCode,
                                user!!.billingEmail,
                                user!!.billingPhone,
                                user!!.shippingFirstName,
                                user!!.shippingLastName,
                                user!!.shippingCompany,
                                user!!.shippingAddress1,
                                user!!.shippingAddress2,
                                user!!.shippingCountry,
                                user!!.shippingState,
                                user!!.shippingCity,
                                user!!.shippingPostalCode,
                                user!!.shippingEmail,
                                user!!.shippingPhone,
                                shippingTax!!,
                                overAllTax!!,
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shipping_cost),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.shippingMethodName),
                                binding!!.get().memoEditText.text.toString(),
                                java.lang.String.valueOf((activity as CheckoutActivity?)!!.transactionValueHolder!!.total_item_count),
                                shopZoneShippingEnable!!,
                                basketProductListToServerContainer.productList
                        )
                        )
                    }
                }
            } else {
                psDialogMsg!!.showErrorDialog(getString(R.string.basket__no_item_desc), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_CODE__PAYPAL) {
            /*if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                if (result.getPaymentMethodNonce() != null) {
                    onPaymentMethodNonceCreated(result.getPaymentMethodNonce());
                }
            }*/
//            else {
//                // handle errors here, an exception may be available in
////                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
//            }
        } else if (requestCode == Constants.REQUEST_CODE__STRIPE_ACTIVITY && resultCode == Constants.RESULT_CODE__STRIPE_ACTIVITY) {
            if (this.activity != null) {
                payment_method_nonce = data!!.getStringExtra(Constants.PAYMENT_TOKEN)

//                progressDialog.show();
                if (activity != null && activity is CheckoutActivity) {
                    (activity as CheckoutActivity?)!!.progressDialog!!.show()
                }
                sendData()
            }
        }
    }

    override fun initData() {
        shopViewModel!!.setShopObj(Config.API_KEY)
        shopViewModel!!.shopData.observe(this, Observer<Resource<Shop?>> { result: Resource<Shop?>? ->
            if (result != null) {
                when (result.status) {
                    Status.SUCCESS -> if (result.data != null) {
                        if (result.data.paypalEnabled == Constants.ONE) {
                            binding!!.get().paypalCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().paypalCardView.visibility = View.GONE
                        }
                        if (result.data.codEnabled == Constants.ONE) {
                            binding!!.get().cashCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().cashCardView.visibility = View.GONE
                        }
                        if (result.data.stripeEnabled == Constants.ONE) {
                            binding!!.get().cardCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().cardCardView.visibility = View.GONE
                        }
                        if (result.data.banktransferEnabled == Constants.ONE) {
                            binding!!.get().bankCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().bankCardView.visibility = View.GONE
                        }
                    }
                    Status.LOADING -> if (result.data != null) {
                        if (result.data.paypalEnabled == Constants.ONE) {
                            binding!!.get().paypalCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().paypalCardView.visibility = View.GONE
                        }
                        if (result.data.codEnabled == Constants.ONE) {
                            binding!!.get().cashCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().cashCardView.visibility = View.GONE
                        }
                        if (result.data.stripeEnabled == Constants.ONE) {
                            binding!!.get().cardCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().cardCardView.visibility = View.GONE
                        }
                        if (result.data.banktransferEnabled == Constants.ONE) {
                            binding!!.get().bankCardView.visibility = View.VISIBLE
                        } else {
                            binding!!.get().bankCardView.visibility = View.GONE
                        }
                    }
                    Status.ERROR -> {
                    }
                }
            }
        })
        paypalViewModel!!.paypalTokenData.observe(this, Observer<Resource<Boolean?>> { result: Resource<Boolean?>? ->
            if (result != null) {
                when (result.status) {
                    Status.SUCCESS -> {
                        clientTokenString = result.message
                        onBraintreeSubmit()
                        if (activity != null && activity is CheckoutActivity) {
                            (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        if (activity != null && activity is CheckoutActivity) {
                            (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }
                    }
                }
            }
        })
        basketViewModel!!.setBasketListWithProductObj()
        basketViewModel!!.allBasketWithProductList.observe(this, Observer { baskets: List<Basket>? ->
            if (baskets != null && baskets.size > 0) {
                basketList = baskets

//                String currency = "";
                for (basket in baskets) {
                    //                    currency = basket.product.currencyShortForm;
                    val map = Gson().fromJson<HashMap<String, String?>>(basket.selectedAttributes, object : TypeToken<HashMap<String?, String?>?>() {}.type)
                    val keyIterator: Iterator<String> = map.keys.iterator()
                    val keyStr = StringBuilder()
                    val nameStr = StringBuilder()
                    while (keyIterator.hasNext()) {
                        val key = keyIterator.next()
                        if (key != "-1") {
                            if (map.containsKey(key)) {
                                if (keyStr.toString() != "") {
                                    keyStr.append(Config.ATTRIBUTE_SEPARATOR)
                                    nameStr.append(Config.ATTRIBUTE_SEPARATOR)
                                }
                                keyStr.append(key)
                                nameStr.append(map[key])
                            }
                        }
                    }
                    val priceMap = Gson().fromJson<HashMap<String, String?>>(basket.selectedAttributesPrice, object : TypeToken<HashMap<String?, String?>?>() {}.type)
                    val priceKeyIterator: Iterator<String> = priceMap.keys.iterator()
                    val priceStr = StringBuilder()
                    while (priceKeyIterator.hasNext()) {
                        val key = priceKeyIterator.next()
                        if (key != "-1") {
                            if (priceMap.containsKey(key)) {
                                if (priceStr.toString() != "") {
                                    priceStr.append(Config.ATTRIBUTE_SEPARATOR)
                                }
                                priceStr.append(priceMap[key])
                            }
                        }
                    }
                    Utils.psLog("Data for map" + basket.selectedAttributes)
                    val basketProductToServer = BasketProductToServer(
                            "",
                            basket.productId,
                            basket.product!!.name,
                            keyStr.toString(),
                            nameStr.toString(),
                            priceStr.toString(),
                            basket.selectedColorId,
                            basket.selectedColorValue,
                            basket.product!!.productUnit,
                            basket.product!!.productMeasurement,
                            basket.product!!.shippingCost, basket.product!!.unitPrice.toString(), basket.basketOriginalPrice.toString(), (basket.basketOriginalPrice - basket.product!!.unitPrice).toString(), basket.product!!.discountAmount.toString(), basket.count.toString(), basket.product!!.discountValue.toString(), basket.product!!.discountPercent.toString(),
                            basket.product!!.currencyShortForm,
                            basket.product!!.currencySymbol)
                    basketProductListToServerContainer.productList.add(basketProductToServer)
                }
            }
        })
        transactionListViewModel!!.sendTransactionDetailData.observe(this, Observer<Resource<TransactionObject?>> { result: Resource<TransactionObject?>? ->
            if (result != null) {
                if (result.status === Status.SUCCESS) {
                    if (this@CheckoutFragment3.activity != null) {
                        (this@CheckoutFragment3.activity as CheckoutActivity?)!!.number = 4
                        (this@CheckoutFragment3.activity as CheckoutActivity?)!!.transactionObject = result.data
                        (this@CheckoutFragment3.activity as CheckoutActivity?)!!.goToFragment4()
                        basketViewModel!!.setWholeBasketDeleteStateObj()
                        if (activity != null && activity is CheckoutActivity) {
                            (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }

//                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).progressDialog.cancel();
                    }
                } else if (result.status === Status.ERROR) {
                    if (this@CheckoutFragment3.activity != null) {
                        if (activity != null && activity is CheckoutActivity) {
                            (activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }

//                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).progressDialog.cancel();
                        psDialogMsg!!.showErrorDialog(result.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                    }
                }
            }
        })
        basketViewModel!!.wholeBasketDeleteData.observe(this, Observer<Resource<Boolean?>> { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status === Status.SUCCESS) {
                    Utils.psLog("Success")
                } else if (result.status === Status.ERROR) {
                    Utils.psLog("Fail")
                }
            }
        })
    }

    //        progressDialog.show();
    val token: Unit
        get() {
            paypalViewModel!!.setPaypalTokenObj()
            //        progressDialog.show();
            if (activity != null && activity is CheckoutActivity) {
                (activity as CheckoutActivity?)!!.progressDialog!!.show()
            }
        }

    private fun onBraintreeSubmit() {
        /* if (getActivity() != null) {
            DropInRequest dropInRequest = new DropInRequest()
                    .clientToken(clientTokenString);
            this.getActivity().startActivityForResult(dropInRequest.getIntent(this.getActivity()), Constants.REQUEST_CODE__PAYPAL);
        }*/
    }

    private fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
//        if (paymentMethodNonce instanceof PayPalAccountNonce) {
//            PayPalAccountNonce paypalAccountNonce = (PayPalAccountNonce) paymentMethodNonce;

//            PayPalCreditFinancing creditFinancing = paypalAccountNonce.getCreditFinancing();
//            if (creditFinancing != null) {
//                // PayPal Credit was accepted
//            }

        // Access additional information
        if (activity != null && activity is CheckoutActivity) {
            (activity as CheckoutActivity?)!!.progressDialog!!.show()
        }
        payment_method_nonce = paymentMethodNonce.nonce
        sendData()

//        }
        /*else {
            // Send nonce to server
            String nonce = paymentMethodNonce.getNonce();
        }*/
    }

    private fun changeToOrange(cardView: CardView, textView: TextView, imageView: ImageView) {
        cardView.setCardBackgroundColor(resources.getColor(R.color.global__primary))
        imageView.setColorFilter(resources.getColor(R.color.md_white_1000))
        textView.setTextColor(resources.getColor(R.color.md_white_1000))
    }

    private fun changeToWhite(cardView: CardView, textView: TextView, imageView: ImageView) {
        cardView.setCardBackgroundColor(resources.getColor(R.color.md_white_1000))
        imageView.setColorFilter(resources.getColor(R.color.md_grey_500))
        textView.setTextColor(resources.getColor(R.color.md_grey_700))
    }
}