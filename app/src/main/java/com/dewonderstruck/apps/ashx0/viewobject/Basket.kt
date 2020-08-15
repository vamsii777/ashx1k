package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Basket(val productId: String, var count: Int, var selectedAttributes: String, var selectedColorId: String, var selectedColorValue: String, var selectedAttributeTotalPrice: String, var basketPrice: Float, var basketOriginalPrice: Float, var shopId: String, var selectedAttributesPrice: String) {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @kotlin.jvm.JvmField
    @Ignore
    var product: Product? = null

}