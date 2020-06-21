package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class BasketProductToServer(@field:SerializedName("shop_id") var shopId: String, @field:SerializedName("product_id") var productId: String, @field:SerializedName("product_name") var productName: String, @field:SerializedName("product_attribute_id") var productAttributeId: String, @field:SerializedName("product_attribute_name") var productAttributeName: String, @field:SerializedName("product_attribute_price") var product_attribute_price: String, @field:SerializedName("product_color_id") var product_color_id: String, @field:SerializedName("product_color_code") var product_color_code: String, @field:SerializedName("product_unit") var product_unit: String, @field:SerializedName("product_measurement") var productMeasurement: String, @field:SerializedName("shipping_cost") var shippingCost: String, @field:SerializedName("unit_price") var price: String, @field:SerializedName("original_price") var originalPrice: String, @field:SerializedName("discount_price") var discount_price: String, @field:SerializedName("discount_amount") var discountAmount: String, @field:SerializedName("qty") var qty: String, @field:SerializedName("discount_value") var discountValue: String, @field:SerializedName("discount_percent") var discountPercent: String, @field:SerializedName("currency_short_form") var currencyShortForm: String, @field:SerializedName("currency_symbol") var currencySymbol: String)