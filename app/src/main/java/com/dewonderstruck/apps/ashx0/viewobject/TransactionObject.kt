package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class TransactionObject(@field:SerializedName("id") var id: String, @field:SerializedName("user_id") var userId: String, @field:SerializedName("sub_total_amount") var subTotalAmount: String, @field:SerializedName("discount_amount") var discountAmount: String, @field:SerializedName("coupon_discount_amount") var couponDiscountAmount: String, @field:SerializedName("tax_amount") var taxAmount: String, @field:SerializedName("tax_percent") var taxPercent: String, @field:SerializedName("shipping_amount") var shippingAmount: String, @field:SerializedName("shipping_tax_percent") var shippingTaxPercent: String, @field:SerializedName("shipping_method_amount") var shippingMethodAmount: String, @field:SerializedName("shipping_method_name") var shippingMethodName: String, @field:SerializedName("balance_amount") var balanceAmount: String, @field:SerializedName("total_item_amount") var totalItemAmount: String, @field:SerializedName("total_item_count") var totalItemCount: String, @field:SerializedName("contact_name") var contactName: String, @field:SerializedName("contact_phone") var contactPhone: String, @field:SerializedName("payment_method") var paymentMethod: String, @field:SerializedName("added_date") var addedDate: String, @field:SerializedName("added_user_id") var addedUserId: String, @field:SerializedName("updated_date") var updatedDate: String, @field:SerializedName("updated_user_id") var updatedUserId: String, @field:SerializedName("updated_flag") var updatedFlag: String, @field:SerializedName("trans_status_id") var transStatusId: String, @field:SerializedName("currency_symbol") var currencySymbol: String, @field:SerializedName("currency_short_form") var currencyShortForm: String, @field:SerializedName("trans_code") var transCode: String, @field:SerializedName("billing_first_name") var billingFirstName: String, @field:SerializedName("billing_last_name") var billingLastName: String, @field:SerializedName("billing_company") var billingCompany: String, @field:SerializedName("billing_address_1") var billingAddress1: String, @field:SerializedName("billing_address_2") var billingAddress2: String, @field:SerializedName("billing_country") var billingCountry: String, @field:SerializedName("billing_state") var billingState: String, @field:SerializedName("billing_city") var billingCity: String, @field:SerializedName("billing_postal_code") var billingPostalCode: String, @field:SerializedName("billing_email") var billingEmail: String, @field:SerializedName("billing_phone") var billingPhone: String, @field:SerializedName("shipping_first_name") var shippingFirstName: String, @field:SerializedName("shipping_last_name") var shippingLastName: String, @field:SerializedName("shipping_company") var shippingCompany: String, @field:SerializedName("shipping_address_1") var shippingAddress1: String, @field:SerializedName("shipping_address_2") var shippingAddress2: String, @field:SerializedName("shipping_country") var shippingCountry: String, @field:SerializedName("shipping_state") var shippingState: String, @field:SerializedName("shipping_city") var shippingCity: String, @field:SerializedName("shipping_postal_code") var shippingPostalCode: String, @field:SerializedName("shipping_email") var shippingEmail: String, @field:SerializedName("shipping_phone") var shippingPhone: String, @field:SerializedName("product_unit") var productUnit: String, @field:SerializedName("product_measurement") var productMeasurement: String, @field:SerializedName("memo") var memo: String, @field:SerializedName("is_zone_shipping") var isZoneShipping: String, @field:SerializedName("added_date_str") var addedDateStr: String, @field:SerializedName("trans_status_title") var transStatusTitle: String)