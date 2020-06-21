package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class ProductListByCatId(@field:SerializedName("id") @field:PrimaryKey val productId: String, @field:SerializedName("cat_id") val catId: String)