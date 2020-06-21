package com.dewonderstruck.apps.ashx0.viewobject

import com.google.gson.annotations.SerializedName

/**
 * Created by Vamsi Madduluri on 11/17/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class ApiStatus(@field:SerializedName("status") val status: String, @field:SerializedName("message") val message: String)