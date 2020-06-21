package com.dewonderstruck.apps.ashx0.di;

import android.app.Application;

import com.dewonderstruck.apps.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Vamsi Madduluri on 11/15/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class
})

public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        AppComponent build();
    }
    void inject(App MShop);

}
