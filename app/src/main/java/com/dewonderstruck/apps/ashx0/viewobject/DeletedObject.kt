package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class DeletedObject(@field:SerializedName("id") var id: String, @field:SerializedName("type_id") var typeId: String, @field:SerializedName("type_name") var typeName: String, @field:SerializedName("deleted_date") var deletedDate: String)