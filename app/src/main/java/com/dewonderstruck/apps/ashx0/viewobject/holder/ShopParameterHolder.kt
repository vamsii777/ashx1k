package com.dewonderstruck.apps.ashx0.viewobject.holder

import com.dewonderstruck.apps.ashx0.utils.Constants
import java.io.Serializable

class ShopParameterHolder : Serializable {
    var isFeature = ""
    var orderBy: String
    var orderType: String
    val latestParameterHolder: ShopParameterHolder
        get() {
            isFeature = ""
            orderBy = Constants.FILTERING_ADDED_DATE
            orderType = Constants.FILTERING_DESC
            return this
        }

    val featuredParameterHolder: ShopParameterHolder
        get() {
            isFeature = Constants.ONE
            orderBy = Constants.FILTERING_FEATURE
            orderType = Constants.FILTERING_DESC
            return this
        }

    val popularParameterHolder: ShopParameterHolder
        get() {
            isFeature = ""
            orderBy = Constants.FILTERING_TRENDING
            orderType = Constants.FILTERING_DESC
            return this
        }

    val shopMapKey: String
        get() {
            val FEATURED = Constants.PRODUCT_FEATURED
            var result = ""
            if (!isFeature.isEmpty()) {
                result += "$FEATURED:"
            }
            if (!orderBy.isEmpty()) {
                result += "$orderBy:"
            }
            if (!orderType.isEmpty()) {
                result += orderType
            }
            return result
        }

    init {
        orderBy = Constants.FILTERING_ADDED_DATE
        orderType = Constants.FILTERING_DESC
    }
}