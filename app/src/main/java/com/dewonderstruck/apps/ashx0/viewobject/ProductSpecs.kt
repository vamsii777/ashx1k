package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class ProductSpecs(@field:SerializedName("id") val id: String, @field:SerializedName("product_id") val productId: String, @field:SerializedName("name") val name: String, @field:SerializedName("description") val description: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("updated_flag") val updatedFlag: String, @field:SerializedName("is_empty_object") val isEmptyObject: String)