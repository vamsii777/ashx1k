package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class LikedProduct(@field:SerializedName("id") val productId: String) {
    @PrimaryKey(autoGenerate = true)
    val id = 0

}