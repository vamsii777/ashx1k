package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class Noti(@field:SerializedName("id") var id: String, @field:SerializedName("message") var message: String, @field:SerializedName("description") var description: String, @field:SerializedName("added_user_id") var addedUserId: String, @field:SerializedName("added_date") var addedDate: String, @field:SerializedName("added_date_str") var addedDateStr: String, @field:SerializedName("is_read") var isRead: String, @field:SerializedName("default_photo") @field:Embedded(prefix = "photo_") var defaultPhoto: Image)