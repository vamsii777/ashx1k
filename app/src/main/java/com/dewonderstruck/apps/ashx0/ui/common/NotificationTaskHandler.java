package com.dewonderstruck.apps.ashx0.ui.common;

import android.content.Context;

import com.dewonderstruck.apps.ashx0.repository.aboutus.AboutUsRepository;
import com.dewonderstruck.apps.ashx0.utils.Utils;

/**
 * Created by Vamsi Madduluri on 12/5/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class NotificationTaskHandler extends BackgroundTaskHandler {

    private final AboutUsRepository repository;

    public NotificationTaskHandler(AboutUsRepository repository) {
        super();

        this.repository = repository;
    }

    public void registerNotification(Context context, String platform, String token) {

        if(platform == null) return;

        if(platform.equals("")) return;

        Utils.psLog("Register Notification : Notification Handler");
        holdLiveData = repository.registerNotification(context, platform, token);
        loadingState.setValue(new LoadingState(true, null));

        //noinspection ConstantConditions
        holdLiveData.observeForever(this);

    }

    public void unregisterNotification(Context context, String platform, String token) {

        if(platform == null) return;

        if(platform.equals("")) return;

        Utils.psLog("Unregister Notification : Notification Handler");
        holdLiveData = repository.unregisterNotification(context, platform, token);
        loadingState.setValue(new LoadingState(true, null));

        //noinspection ConstantConditions
        holdLiveData.observeForever(this);

    }

}