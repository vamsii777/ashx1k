package com.dewonderstruck.apps.ashx0.viewobject.holder

import com.dewonderstruck.apps.ashx0.utils.Constants

class CategoryParameterHolder {
    @JvmField
    var order_by: String
    val trendingCategories: CategoryParameterHolder
        get() {
            order_by = Constants.FILTERING_TRENDING
            return this
        }

    fun changeToMapValue(): String {
        var result = ""
        if (!order_by.isEmpty()) {
            result += order_by
        }
        return result
    }

    init {
        order_by = Constants.FILTERING_ADDED_DATE
    }
}