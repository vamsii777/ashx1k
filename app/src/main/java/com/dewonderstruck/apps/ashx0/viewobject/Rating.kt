package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Rating(@field:PrimaryKey @field:SerializedName("id") val id: String, @field:SerializedName("shop_id") val shopId: String, @field:SerializedName("user_id") val userId: String, @field:SerializedName("product_id") val productId: String, @field:SerializedName("rating") val rating: String, @field:SerializedName("title") val title: String, @field:SerializedName("description") val description: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:Embedded(prefix = "user") @field:SerializedName("user") val user: User)