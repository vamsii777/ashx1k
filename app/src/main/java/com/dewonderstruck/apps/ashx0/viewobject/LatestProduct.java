package com.dewonderstruck.apps.ashx0.viewobject;

import androidx.room.Entity;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Entity(primaryKeys = "id")
public class LatestProduct {

    @SerializedName("id")
    @NonNull
    public final String id;

    public LatestProduct(@NonNull String id) {
        this.id = id;
    }
}
