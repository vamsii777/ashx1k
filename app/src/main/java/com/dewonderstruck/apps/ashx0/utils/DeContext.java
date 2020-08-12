package com.dewonderstruck.apps.ashx0.utils;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

/**
 * Created by Vamsi Madduluri on 1/10/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class DeContext {

    public Context context;

    @Inject
    DeContext(Application App) {
        this.context = App.getApplicationContext();
    }
}
