package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Vamsi Madduluri on 12/02/2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity(primaryKeys = ["imgId"])
class Image(@field:SerializedName("img_id") val imgId: String, @field:SerializedName("img_parent_id") val imgParentId: String, @field:SerializedName("img_type") val imgType: String, @field:SerializedName("img_path") val imgPath: String, @field:SerializedName("img_width") val imgWidth: String, @field:SerializedName("img_height") val imgHeight: String, @field:SerializedName("img_desc") val imgDesc: String)