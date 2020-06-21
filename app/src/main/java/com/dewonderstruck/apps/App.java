package com.dewonderstruck.apps;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.dewonderstruck.apps.ashx0.di.AppInjector;
import com.facebook.ads.AudienceNetworkAds;
import com.flurry.android.FlurryAgent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


/**
 * Created by Vamsi Madduluri on 11/15/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class App extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppInjector.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
// Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "BJS8NMHKTTVFF9CGKCQ3");
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        setupLeakCanary();
//        new ContextModule(getApplicationContext());
//        MultiDex.install(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

//    protected RefWatcher setupLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return RefWatcher.DISABLED;
//        }
//
//        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
//                .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
//                .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
//                .instanceField("com.android.internal.policy.PhoneWindow$DecorView", "mContext")
//                .instanceField("android.support.v7.widget.SearchView$SearchAutoComplete", "mContext")
//                .build();
//
//        return LeakCanary.refWatcher(this).excludedRefs(excludedRefs).buildAndInstall();
//                //.install(this);
//    }

}
