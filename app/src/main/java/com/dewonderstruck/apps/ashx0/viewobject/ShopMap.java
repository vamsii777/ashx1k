package com.dewonderstruck.apps.ashx0.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * Created by Vamsi Madduluri on 3/19/19.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Entity(primaryKeys = "id")
public class ShopMap {

    @NonNull
    public final String id;

    public final String mapKey;

    public final String productId;

    public final int sorting;

    public final String addedDate;

    public ShopMap(@NonNull String id, String mapKey, String productId, int sorting, String addedDate) {
        this.id = id;
        this.mapKey = mapKey;
        this.productId = productId;
        this.sorting = sorting;
        this.addedDate = addedDate;
    }
}

