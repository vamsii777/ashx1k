package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class Comment(@field:SerializedName("id") val id: String, @field:SerializedName("product_id") val productId: String, @field:SerializedName("user_id") val userId: String, @field:SerializedName("header_comment") val headerComment: String, @field:SerializedName("status") val status: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("comment_reply_count") val commentReplyCount: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("user") @field:Embedded(prefix = "user_") val user: User)