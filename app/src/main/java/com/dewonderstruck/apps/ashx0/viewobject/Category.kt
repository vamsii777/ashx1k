package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Vamsi Madduluri on 12/02/2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity(primaryKeys = ["id"])
class Category(@field:SerializedName("id") var id: String, @field:SerializedName("name") var name: String, @field:SerializedName("status") var status: String, @field:SerializedName("added_date") var addedDate: String, @field:SerializedName("added_user_id") var addedUserId: String, @field:SerializedName("updated_date") var updatedDate: String, @field:SerializedName("updated_user_id") var updatedUserId: String, @field:SerializedName("updated_flag") var updatedFlag: String, @field:SerializedName("added_date_str") var addedDateStr: String, @field:SerializedName("touch_count") var touchCount: String, @field:SerializedName("default_photo") @field:Embedded(prefix = "default_photo") var defaultPhoto: Image, @field:SerializedName("default_icon") @field:Embedded(prefix = "default_icon") var defaultIcon: Image)