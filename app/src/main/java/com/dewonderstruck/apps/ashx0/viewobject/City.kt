package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class City(@field:SerializedName("id") var id: String, @field:SerializedName("name") var name: String, @field:SerializedName("country_id") var countryId: String, @field:SerializedName("status") var status: String, @field:SerializedName("added_date") var addedDate: String, @field:SerializedName("added_user_id") var addedUserId: String, @field:SerializedName("updated_date") var updatedDate: String, @field:SerializedName("updated_user_id") var updatedUserId: String, @field:SerializedName("updated_flag") var updatedFlag: String, @field:SerializedName("added_date_str") var addedDateStr: String)