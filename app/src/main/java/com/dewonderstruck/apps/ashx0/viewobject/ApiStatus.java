package com.dewonderstruck.apps.ashx0.viewobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vamsi Madduluri on 11/17/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

public class ApiStatus {

    @SerializedName("status")
    public final String status;

    @SerializedName("message")
    public final String message;

    public ApiStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
