package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class FavouriteProduct(@field:SerializedName("id") val productId: String, var sorting: Int) {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    val id = 0

}