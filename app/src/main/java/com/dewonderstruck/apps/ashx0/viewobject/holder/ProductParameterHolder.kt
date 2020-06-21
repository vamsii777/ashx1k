package com.dewonderstruck.apps.ashx0.viewobject.holder

import com.dewonderstruck.apps.ashx0.utils.Constants
import java.io.Serializable

class ProductParameterHolder : Serializable {
    @JvmField
    var search_term = ""
    @JvmField
    var catId = ""
    @JvmField
    var subCatId = ""
    @JvmField
    var isFeatured = ""
    @JvmField
    var isDiscount = ""
    @JvmField
    var isAvailable = ""
    @JvmField
    var max_price = ""
    @JvmField
    var min_price = ""
    @JvmField
    var overall_rating = ""
    @JvmField
    var rating_value_one = ""
    @JvmField
    var rating_value_two = ""
    @JvmField
    var rating_value_three = ""
    @JvmField
    var rating_value_four = ""
    @JvmField
    var rating_value_five = ""
    @JvmField
    var order_by: String
    @JvmField
    var order_type: String
    val latestParameterHolder: ProductParameterHolder
        get() {
            search_term = ""
            catId = ""
            subCatId = ""
            isFeatured = ""
            isDiscount = ""
            isAvailable = ""
            max_price = ""
            overall_rating = ""
            min_price = ""
            rating_value_one = ""
            rating_value_two = ""
            rating_value_three = ""
            rating_value_four = ""
            rating_value_five = ""
            order_by = Constants.FILTERING_ADDED_DATE
            order_type = Constants.FILTERING_DESC
            return this
        }

    val featuredParameterHolder: ProductParameterHolder
        get() {
            search_term = ""
            catId = ""
            subCatId = ""
            isFeatured = Constants.ONE
            isDiscount = ""
            isAvailable = ""
            max_price = ""
            overall_rating = ""
            min_price = ""
            rating_value_one = ""
            rating_value_two = ""
            rating_value_three = ""
            rating_value_four = ""
            rating_value_five = ""
            order_by = Constants.FILTERING_FEATURE
            order_type = Constants.FILTERING_DESC
            return this
        }

    val discountParameterHolder: ProductParameterHolder
        get() {
            search_term = ""
            catId = ""
            subCatId = ""
            isFeatured = ""
            isDiscount = Constants.ONE
            isAvailable = ""
            max_price = ""
            overall_rating = ""
            min_price = ""
            rating_value_one = ""
            rating_value_two = ""
            rating_value_three = ""
            rating_value_four = ""
            rating_value_five = ""
            order_by = Constants.FILTERING_ADDED_DATE
            order_type = Constants.FILTERING_DESC
            return this
        }

    val trendingParameterHolder: ProductParameterHolder
        get() {
            search_term = ""
            catId = ""
            subCatId = ""
            isFeatured = ""
            isDiscount = ""
            isAvailable = ""
            max_price = ""
            overall_rating = ""
            min_price = ""
            rating_value_one = ""
            rating_value_two = ""
            rating_value_three = ""
            rating_value_four = ""
            rating_value_five = ""
            order_by = Constants.FILTERING_TRENDING
            order_type = Constants.FILTERING_DESC
            return this
        }

    fun resetTheHolder() {
        search_term = ""
        catId = ""
        subCatId = ""
        isFeatured = ""
        isDiscount = ""
        isAvailable = ""
        max_price = ""
        overall_rating = ""
        min_price = ""
        rating_value_one = ""
        rating_value_two = ""
        rating_value_three = ""
        rating_value_four = ""
        rating_value_five = ""
        order_by = Constants.FILTERING_ADDED_DATE
        order_type = Constants.FILTERING_DESC
    }

    /*if(!min_price.isEmpty())
        {
            result += min_price + ":";
        }*/
    val keyForProductMap: String
        get() {
            val discount = "discount"
            val featured = "featured"
            val available = "available"
            val rating_one = "rating_one"
            val rating_two = "rating_two"
            val rating_three = "rating_three"
            val rating_four = "rating_four"
            val rating_five = "rating_five"
            var result = ""
            if (!search_term.isEmpty()) {
                result += "$search_term:"
            }
            if (!catId.isEmpty()) {
                result += "$catId:"
            }
            if (!subCatId.isEmpty()) {
                result += "$subCatId:"
            }
            if (!isFeatured.isEmpty()) {
                result += "$featured:"
            }
            if (!isDiscount.isEmpty()) {
                result += "$discount:"
            }
            if (!isAvailable.isEmpty()) {
                result += "$available:"
            }
            if (!max_price.isEmpty()) {
                result += "$max_price:"
            }
            if (!overall_rating.isEmpty()) {
                result += "$overall_rating:"
            }

            /*if(!min_price.isEmpty())
        {
            result += min_price + ":";
        }*/if (!rating_value_one.isEmpty()) {
                result += "$rating_one:"
            }
            if (!rating_value_two.isEmpty()) {
                result += "$rating_two:"
            }
            if (!rating_value_three.isEmpty()) {
                result += "$rating_three:"
            }
            if (!rating_value_four.isEmpty()) {
                result += "$rating_four:"
            }
            if (!rating_value_five.isEmpty()) {
                result += "$rating_five:"
            }
            if (!order_by.isEmpty()) {
                result += "$order_by:"
            }
            if (!order_type.isEmpty()) {
                result += order_type
            }
            return result
        }

    init {
        order_by = Constants.FILTERING_ADDED_DATE
        order_type = Constants.FILTERING_DESC
    }
}