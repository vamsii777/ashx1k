package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class ShopTag(@field:SerializedName("id") @field:PrimaryKey val id: String, @field:SerializedName("name") val name: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("status") val status: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("default_photo") @field:Embedded(prefix = "default_photo") val defaultPhoto: Image, @field:SerializedName("default_icon") @field:Embedded(prefix = "default_icon") val defaultIcon: Image)