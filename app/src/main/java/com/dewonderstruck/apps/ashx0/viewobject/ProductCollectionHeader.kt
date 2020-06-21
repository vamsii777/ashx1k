package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

/**
 * Created by Vamsi Madduluri on 10/27/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity(primaryKeys = ["id"])
class ProductCollectionHeader(@field:SerializedName("id") val id: String, @field:SerializedName("name") val name: String, @field:SerializedName("status") val status: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("updated_flag") val updatedFlag: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("default_photo") @field:Embedded(prefix = "default_photo_") val defaultPhoto: Image) {

    @kotlin.jvm.JvmField
    @Ignore
    @SerializedName("products")
    var productList: List<Product>? = null

}