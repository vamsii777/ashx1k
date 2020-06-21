package com.dewonderstruck.apps.ashx0.db.common;

import androidx.room.TypeConverter;
import java.util.Date;

/**
 * Created by Vamsi Madduluri on 12/27/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
