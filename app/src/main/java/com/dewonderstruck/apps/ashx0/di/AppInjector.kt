package com.dewonderstruck.apps.ashx0.di

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dewonderstruck.apps.App
import com.dewonderstruck.apps.Config
import dagger.android.AndroidInjection
import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Created by Vamsi Madduluri on 11/15/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class AppInjector private constructor() {
    companion object {
        fun init(githubApp: App) {
            DaggerAppComponent.builder().application(githubApp)
                    .build().inject(githubApp)
            githubApp
                    .registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                            handleActivity(activity)
                        }

                        override fun onActivityStarted(activity: Activity) {}
                        override fun onActivityResumed(activity: Activity) {}
                        override fun onActivityPaused(activity: Activity) {}
                        override fun onActivityStopped(activity: Activity) {}
                        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                        override fun onActivityDestroyed(activity: Activity) {}
                    })
        }

        private fun handleActivity(activity: Activity) {
            if (activity is HasSupportFragmentInjector
                    || activity is HasActivityInjector) {
                AndroidInjection.inject(activity)
            }
            if (activity is FragmentActivity) {
                activity.supportFragmentManager
                        .registerFragmentLifecycleCallbacks(
                                object : FragmentManager.FragmentLifecycleCallbacks() {
                                    override fun onFragmentCreated(fm: FragmentManager, f: Fragment,
                                                                   savedInstanceState: Bundle?) {
                                        if (f is Injectable) {
                                            AndroidSupportInjection.inject(f)
                                        }
                                    }
                                }, true)
            }
        }
    }

    init {
        Config
    }
}