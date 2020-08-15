package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["versionNo"])
class PSAppVersion(@field:SerializedName("version_no") var versionNo: String, @field:SerializedName("version_force_update") var versionForceUpdate: String, @field:SerializedName("version_title") var versionTitle: String, @field:SerializedName("version_message") var versionMessage: String, @field:SerializedName("version_need_clear_data") var versionNeedClearData: String)