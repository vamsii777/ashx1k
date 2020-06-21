package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
class CategoryMap(val id: String, val mapKey: String, val categoryId: String, val sorting: Int, val addedDate: String)