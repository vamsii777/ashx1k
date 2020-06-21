package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class TransactionHeaderUpload(@field:SerializedName("user_id") val userId: String, @field:SerializedName("sub_total_amount") val sub_total_amount: String, @field:SerializedName("discount_amount") val discountAmount: String, @field:SerializedName("coupon_discount_amount") val coupon_discount_amount: String, @field:SerializedName("tax_amount") val tax_amount: String, @field:SerializedName("shipping_amount") val shipping_amount: String, @field:SerializedName("balance_amount") val balance_amount: String, @field:SerializedName("total_item_amount") val total_item_amount: String, @field:SerializedName("contact_name") val contact_name: String, @field:SerializedName("contact_phone") val contact_phone: String, @field:SerializedName("is_cod") val is_cod: String, @field:SerializedName("is_paypal") val is_paypal: String, @field:SerializedName("is_stripe") val is_stripe: String, @field:SerializedName("is_bank") val is_bank: String, @field:SerializedName("payment_method_nonce") val payment_method_nonce: String, @field:SerializedName("trans_status_id") val trans_status_id: String, @field:SerializedName("currency_symbol") val currencySymbol: String, @field:SerializedName("currency_short_form") val currencyShortForm: String, @field:SerializedName("billing_first_name") val billing_first_name: String, @field:SerializedName("billing_last_name") val billing_last_name: String, @field:SerializedName("billing_company") val billing_company: String, @field:SerializedName("billing_address_1") val billing_address_1: String, @field:SerializedName("billing_address_2") val billing_address_2: String, @field:SerializedName("billing_country") val billing_country: String, @field:SerializedName("billing_state") val billing_state: String, @field:SerializedName("billing_city") val billing_city: String, @field:SerializedName("billing_postal_code") val billing_postal_code: String, @field:SerializedName("billing_email") val billing_email: String, @field:SerializedName("billing_phone") val billing_phone: String, @field:SerializedName("shipping_first_name") val shipping_first_name: String, @field:SerializedName("shipping_last_name") val shipping_last_name: String, @field:SerializedName("shipping_company") val shipping_company: String, @field:SerializedName("shipping_address_1") val shipping_address_1: String, @field:SerializedName("shipping_address_2") val shipping_address_2: String, @field:SerializedName("shipping_country") val shipping_country: String, @field:SerializedName("shipping_state") val shipping_state: String, @field:SerializedName("shipping_city") val shipping_city: String, @field:SerializedName("shipping_postal_code") val shipping_postal_code: String, @field:SerializedName("shipping_email") val shipping_email: String, @field:SerializedName("shipping_phone") val shipping_phone: String, @field:SerializedName("shipping_tax_percent") val shipping_tax_percent: String, @field:SerializedName("tax_percent") val tax_percent: String, @field:SerializedName("shipping_method_amount") val shipping_method_amount: String, @field:SerializedName("shipping_method_name") val shipping_method_name: String, @field:SerializedName("memo") val memo: String, @field:SerializedName("total_item_count") val totalItemCount: String, @field:SerializedName("is_zone_shipping") val zoneShippingEnable: String, @field:SerializedName("details") val details: List<BasketProductToServer>)