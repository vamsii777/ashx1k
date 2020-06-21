package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class ShippingMethod(@field:SerializedName("id") @field:PrimaryKey val id: String, @field:SerializedName("price") val price: Float, @field:SerializedName("name") val name: String, @field:SerializedName("days") val days: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("updated_flag") val updatedFlag: String, @field:SerializedName("is_published") val isPublished: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("currency_short_form") val currencyShortForm: String, @field:SerializedName("currency_symbol") val currencySymbol: String)