package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity

/**
 * Created by Vamsi Madduluri on 12/12/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity(primaryKeys = ["userId"])
class UserLogin(val userId: String, val login: Boolean, @field:Embedded(prefix = "user_") val user: User)