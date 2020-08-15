package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class CouponDiscount(@field:SerializedName("id") var id: String, @field:SerializedName("coupon_name") var couponName: String, @field:SerializedName("coupon_code") var couponCode: String, @field:SerializedName("coupon_amount") var couponAmount: String, @field:SerializedName("is_published") var isPublished: String, @field:SerializedName("added_date") var addedDate: String, @field:SerializedName("updated_date") var updatedDate: String, @field:SerializedName("added_user_id") var addedUserId: String, @field:SerializedName("updated_user_id") var updatedUserId: String, @field:SerializedName("updated_flag") var updatedFlag: String, @field:SerializedName("added_date_str") var addedDateStr: String)