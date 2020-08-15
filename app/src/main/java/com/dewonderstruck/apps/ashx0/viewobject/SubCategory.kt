package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class SubCategory(@field:SerializedName("id") val id: String, @field:SerializedName("cat_id") val catId: String, @field:SerializedName("name") val name: String, @field:SerializedName("status") val status: String, @field:SerializedName("ordering") val ordering: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("updated_flag") val updatedFlag: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("default_photo") @field:Embedded val defaultPhoto: Image, @field:SerializedName("default_icon") @field:Embedded(prefix = "icon_") val defaultIcon: Image)