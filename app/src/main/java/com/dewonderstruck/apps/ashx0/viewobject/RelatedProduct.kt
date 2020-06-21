package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class RelatedProduct(@field:SerializedName("id") @field:PrimaryKey val id: String)