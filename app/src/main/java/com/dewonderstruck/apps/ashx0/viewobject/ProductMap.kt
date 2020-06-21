package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
class ProductMap(val id: String, val mapKey: String, val productId: String, val sorting: Int, val addedDate: String)