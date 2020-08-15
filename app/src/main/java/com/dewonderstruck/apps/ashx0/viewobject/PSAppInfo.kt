package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class PSAppInfo(var id: String, @field:SerializedName("version") @field:Embedded(prefix = "version_") var psAppVersion: PSAppVersion) {

    @kotlin.jvm.JvmField
    @SerializedName("delete_history")
    @Ignore
    var deletedObjects: List<DeletedObject>? = null

}