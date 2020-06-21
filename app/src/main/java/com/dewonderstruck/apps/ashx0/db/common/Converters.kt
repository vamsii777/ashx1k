package com.dewonderstruck.apps.ashx0.db.common

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by Vamsi Madduluri on 12/27/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}