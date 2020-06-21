package com.dewonderstruck.apps.ashx0.viewobject.holder

import com.dewonderstruck.apps.ashx0.viewobject.BasketProductListToServerContainer

class TransactionValueHolder {
    var currencySymbol = ""
    @JvmField
    var shippingMethodName = ""
    var selectedShippingId = ""
    @JvmField
    var total = 0f
    @JvmField
    var discount = 0f
    @JvmField
    var coupon_discount = 0f
    @JvmField
    var sub_total = 0f
    @JvmField
    var tax = 0f
    @JvmField
    var shipping_cost = 0f
    @JvmField
    var shipping_tax = 0f
    @JvmField
    var final_total = 0f
    @JvmField
    var total_item_count = 0
    var couponDiscountText = ""
    var basketProductListToServerContainer: BasketProductListToServerContainer
    fun resetValues() {
        currencySymbol = ""
        shippingMethodName = ""
        total = 0f
        discount = 0f
        tax = 0f
        shipping_tax = 0f
        final_total = 0f
        total_item_count = 0
    }

    init {
        basketProductListToServerContainer = BasketProductListToServerContainer()
    }
}