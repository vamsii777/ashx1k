package com.dewonderstruck.apps.ashx0.viewobject;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.annotation.NonNull;

/**
 * Created by Vamsi Madduluri on 12/12/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


@Entity(primaryKeys = "userId")
public class UserLogin {

    @NonNull
    public final String userId;

    public final Boolean login;

    @Embedded(prefix = "user_")
    public final User user;

    public UserLogin(@NonNull String userId, Boolean login, User user) {
        this.userId = userId;
        this.login = login;
        this.user = user;
    }
}
