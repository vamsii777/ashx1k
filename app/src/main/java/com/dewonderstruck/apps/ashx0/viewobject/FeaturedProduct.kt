package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class FeaturedProduct(@field:SerializedName("id") val id: String)