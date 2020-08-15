package com.dewonderstruck.apps.ashx0.viewobject

import com.google.gson.annotations.SerializedName

class ShippingCostContainer(@field:SerializedName("country_id") val countryId: String, @field:SerializedName("city_id") val cityId: String, @field:SerializedName("shop_id") val shopId: String, @field:SerializedName("products") val shippingProductContainerList: List<ShippingProductContainer>)