package com.dewonderstruck.apps.ashx0.viewmodel.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import com.dewonderstruck.apps.ashx0.repository.aboutus.AboutUsRepository;
import com.dewonderstruck.apps.ashx0.ui.common.BackgroundTaskHandler;
import com.dewonderstruck.apps.ashx0.ui.common.NotificationTaskHandler;
import com.dewonderstruck.apps.ashx0.utils.Utils;

import javax.inject.Inject;

/**
 * Created by Vamsi Madduluri on 1/4/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

public class NotificationViewModel extends ViewModel {

    private final NotificationTaskHandler backgroundTaskHandler;

    public boolean pushNotificationSetting = false;
    public boolean isLoading = false;

    @Inject
    NotificationViewModel(AboutUsRepository repository) {
        Utils.psLog("Inside NewsViewModel");

        backgroundTaskHandler = new NotificationTaskHandler(repository);
    }

    public void registerNotification(Context context, String platform, String token) {

        if(token == null || platform == null) return;

        if(platform.equals("")) return;

        backgroundTaskHandler.registerNotification(context, platform, token);
    }

    public void unregisterNotification(Context context, String platform, String token) {

        if(token == null || platform == null) return;

        if(platform.equals("")) return;

        backgroundTaskHandler.unregisterNotification(context, platform, token);
    }

    public LiveData<BackgroundTaskHandler.LoadingState> getLoadingStatus() {
        return backgroundTaskHandler.getLoadingState();
    }



}
