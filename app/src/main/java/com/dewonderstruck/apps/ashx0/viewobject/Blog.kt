package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Blog(@field:SerializedName("id") @field:PrimaryKey val id: String, @field:SerializedName("name") val name: String, @field:SerializedName("description") val description: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("status") val status: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("default_photo") @field:Embedded(prefix = "default_photo_") val defaultPhoto: Image) {

    @kotlin.jvm.JvmField
    @Embedded(prefix = "shop_default_")
    @SerializedName("shop")
    var shop: Shop? = null

}