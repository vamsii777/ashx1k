package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class ProductAttributeDetail(@field:SerializedName("id") val id: String, @field:SerializedName("shop_id") val shopId: String, @field:SerializedName("header_id") val headerId: String, @field:SerializedName("product_id") val productId: String, @field:SerializedName("name") val name: String, @field:SerializedName("additional_price") var additionalPrice: String, @field:SerializedName("added_date") var addedDate: String, @field:SerializedName("added_user_id") var addedUserId: String, @field:SerializedName("updated_date") var updatedDate: String, @field:SerializedName("updated_user_id") var updatedUserId: String, @field:SerializedName("updated_flag") var updatedFlag: String, @field:SerializedName("is_empty_object") var isEmptyObject: String) {

    @kotlin.jvm.JvmField
    @Ignore
    var additionalPriceWithCurrency = ""

}