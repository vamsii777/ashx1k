package com.dewonderstruck.apps.ashx0.di

import android.app.Application
import com.dewonderstruck.apps.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 06/21/2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, MainActivityModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?
        fun build(): AppComponent?
    }

    fun inject(MShop: App?)
}