package com.dewonderstruck.apps.ashx0.utils;

import android.content.SharedPreferences;

import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.ui.danceoholics.data.SliderItem;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Vamsi Madduluri on 10/16/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

//@Singleton
public class AppLanguage {

    private String currentLanguage;
    private SharedPreferences sharedPreferences;

    @Inject
    public AppLanguage(SharedPreferences sharedPreferences) {
        if(sharedPreferences != null) {
            this.sharedPreferences = sharedPreferences;
            try {
                this.currentLanguage = sharedPreferences.getString("Language", Config.DEFAULT_LANGUAGE);
            } catch (Exception e) {
                this.currentLanguage = Config.DEFAULT_LANGUAGE;
            }
        }
    }

    public AppLanguage() {



    }

    public String getLanguage(boolean isLatest) {

        if(isLatest) {
            try {
                this.currentLanguage = sharedPreferences.getString("Language", Config.DEFAULT_LANGUAGE);
            } catch (Exception e) {
                this.currentLanguage = Config.DEFAULT_LANGUAGE;
            }
        }
        return currentLanguage;
    }
}
