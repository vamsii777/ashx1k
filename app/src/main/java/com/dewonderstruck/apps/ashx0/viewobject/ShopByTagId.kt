package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class ShopByTagId(@field:SerializedName("id") var id: String, var sorting: Int, var tagId: String)