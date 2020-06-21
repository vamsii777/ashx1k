package com.dewonderstruck.apps.ashx0.utils;

import android.content.Intent;

/**
 * Created by Vamsi Madduluri on 8/11/16.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
public class FirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {

        super.onNewToken(token);
        Utils.psLog("token : " + token);

        Intent in = new Intent();
        in.putExtra("message",token);
        in.setAction("NOW");

    }

}
