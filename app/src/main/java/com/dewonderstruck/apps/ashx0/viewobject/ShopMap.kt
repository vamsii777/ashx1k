package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity

/**
 * Created by Vamsi Madduluri on 3/19/19.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity(primaryKeys = ["id"])
class ShopMap(val id: String, val mapKey: String, val productId: String, val sorting: Int, val addedDate: String)