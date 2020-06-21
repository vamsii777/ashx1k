package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Vamsi Madduluri on 10/27/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity
class ProductCollection(val collectionId: String, val productId: String) {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    val id = 0

}