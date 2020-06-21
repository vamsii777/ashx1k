package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
class HistoryProduct(val id: String, var historyName: String, var historyUrl: String, var historyDate: String)