package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class CommentDetail(@field:SerializedName("id") val id: String, @field:SerializedName("header_id") val headerId: String, @field:SerializedName("user_id") val userId: String, @field:SerializedName("detail_comment") val detailComment: String, @field:SerializedName("status") val status: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("user") @field:Embedded(prefix = "user_") val user: User)