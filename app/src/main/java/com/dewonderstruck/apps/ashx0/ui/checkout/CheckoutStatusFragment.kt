package com.dewonderstruck.apps.ashx0.ui.checkout

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.CheckoutFragmentStatusBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import java.util.*

class CheckoutStatusFragment : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var binding: AutoClearedValue<CheckoutFragmentStatusBinding>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: CheckoutFragmentStatusBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_status, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {
        if (activity != null) {
            val currencySymbol = (activity as CheckoutActivity?)!!.transactionObject!!.currencySymbol
            binding!!.get().reviewCardView.setOnClickListener { view: View? -> navigationController.navigateToTransactionDetail(activity, (activity as CheckoutActivity?)!!.transactionObject) }
            binding!!.get().transactionNumberTextView.text = (Objects.requireNonNull(activity) as CheckoutActivity).transactionObject!!.transCode
            binding!!.get().imageView24.setOnClickListener { v: View? ->
                val clipboard = Objects.requireNonNull(activity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(Constants.TRANSACTION_NUMBER, (Objects.requireNonNull(activity) as CheckoutActivity).transactionObject!!.transCode)
                if (clipboard != null && clip != null) {
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(activity, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                }
            }
            if ((Objects.requireNonNull(activity) as CheckoutActivity).transactionObject!!.totalItemCount != Constants.ZERO) {
                binding!!.get().totalItemCountValueTextView.text = (activity as CheckoutActivity?)!!.transactionObject!!.totalItemCount
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.totalItemAmount != Constants.ZERO) {
                val totalValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.totalItemAmount.toDouble())
                binding!!.get().totalValueTextView.text = totalValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.discountAmount != Constants.ZERO) {
                val discountValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.discountAmount.toDouble())
                binding!!.get().discountValueTextView.text = discountValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.subTotalAmount != Constants.ZERO) {
                val subTotalValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.subTotalAmount.toDouble())
                binding!!.get().subtotalValueTextView.text = subTotalValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.taxAmount != Constants.ZERO) {
                val taxValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.taxAmount.toDouble())
                binding!!.get().taxValueTextView.text = taxValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.shippingAmount != Constants.ZERO) {
                val shippingTaxValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.shippingAmount.toDouble())
                binding!!.get().shippingTaxValueTextView.text = shippingTaxValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.balanceAmount != Constants.ZERO) {
                val finalTotalValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.balanceAmount.toDouble())
                binding!!.get().finalTotalValueTextView.text = finalTotalValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.shippingMethodAmount != Constants.ZERO) {
                val shippingCostValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.shippingMethodAmount.toDouble())
                binding!!.get().shippingCostValueTextView.text = shippingCostValueString
            }
            if ((activity as CheckoutActivity?)!!.transactionObject!!.couponDiscountAmount != Constants.ZERO) {
                val couponDiscountValueString = currencySymbol + Constants.SPACE_STRING + Utils.format((activity as CheckoutActivity?)!!.transactionObject!!.couponDiscountAmount.toDouble())
                binding!!.get().couponDiscountValueTextView.text = couponDiscountValueString
            }
            if (overAllTaxLabel != Constants.ZERO) {
                binding!!.get().textView22.text = getString(R.string.tax, overAllTaxLabel)
            }
            if (shippingTaxLabel != Constants.ZERO) {
                binding!!.get().textView24.text = getString(R.string.shipping_tax, shippingTaxLabel)
            }
        }
    }

    override fun initViewModels() {}
    override fun initAdapters() {}
    override fun initData() {
        if (this.activity != null) {
            val user = (this.activity as CheckoutActivity?)!!.currentUser
            val text = getString(R.string.checkout_status__thank_you) + Constants.SPACE_STRING + user!!.userName
            binding!!.get().nameTitleTextView.text = text
            binding!!.get().shippingPhoneValueTextView.text = user.shippingPhone
            binding!!.get().shippingEmailValueTextView.text = user.shippingEmail
            binding!!.get().shippingAddressValueTextView.text = user.shippingAddress1
            binding!!.get().billingphoneValueTextView.text = user.billingPhone
            binding!!.get().billingEmailValueTextView.text = user.billingEmail
            binding!!.get().billingAddressValueTextView.text = user.billingAddress1
        }
    }
}