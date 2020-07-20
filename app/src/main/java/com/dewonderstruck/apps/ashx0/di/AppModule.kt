package com.dewonderstruck.apps.ashx0.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.PSApiService
import com.dewonderstruck.apps.ashx0.db.*
import com.dewonderstruck.apps.ashx0.utils.AppLanguage
import com.dewonderstruck.apps.ashx0.utils.Connectivity
import com.dewonderstruck.apps.ashx0.utils.LiveDataCallAdapterFactory
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 11/15/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Module(includes = [ViewModelModule::class])
internal class AppModule {
    @Singleton
    @Provides
    fun providePSApiService(): PSApiService {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()
        return Retrofit.Builder()
                .baseUrl(Config.APP_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(PSApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application?): PSCoreDb {
        return Room.databaseBuilder(app!!, PSCoreDb::class.java, "App.db") //.addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideConnectivity(app: Application?): Connectivity {
        return Connectivity(app)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideUserDao(db: PSCoreDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideCurrentLanguage(sharedPreferences: SharedPreferences?): AppLanguage {
        return AppLanguage(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideAboutUsDao(db: PSCoreDb): AboutUsDao {
        return db.aboutUsDao()
    }

    @Singleton
    @Provides
    fun provideImageDao(db: PSCoreDb): ImageDao {
        return db.imageDao()
    }

    @Singleton
    @Provides
    fun provideProductDao(db: PSCoreDb): ProductDao {
        return db.productDao()
    }

    @Singleton
    @Provides
    fun provideCountryDao(db: PSCoreDb): CountryDao {
        return db.countryDao()
    }

    @Singleton
    @Provides
    fun provideCityDao(db: PSCoreDb): CityDao {
        return db.cityDao()
    }

    @Singleton
    @Provides
    fun provideProductColorDao(db: PSCoreDb): ProductColorDao {
        return db.productColorDao()
    }

    @Singleton
    @Provides
    fun provideProductSpecsDao(db: PSCoreDb): ProductSpecsDao {
        return db.productSpecsDao()
    }

    @Singleton
    @Provides
    fun provideProductAttributeHeaderDao(db: PSCoreDb): ProductAttributeHeaderDao {
        return db.productAttributeHeaderDao()
    }

    @Singleton
    @Provides
    fun provideProductAttributeDetailDao(db: PSCoreDb): ProductAttributeDetailDao {
        return db.productAttributeDetailDao()
    }

    @Singleton
    @Provides
    fun provideBasketDao(db: PSCoreDb): BasketDao {
        return db.basketDao()
    }

    @Singleton
    @Provides
    fun provideHistoryDao(db: PSCoreDb): HistoryDao {
        return db.historyDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(db: PSCoreDb): CategoryDao {
        return db.categoryDao()
    }

    @Singleton
    @Provides
    fun provideRatingDao(db: PSCoreDb): RatingDao {
        return db.ratingDao()
    }

    @Singleton
    @Provides
    fun provideSubCategoryDao(db: PSCoreDb): SubCategoryDao {
        return db.subCategoryDao()
    }

    @Singleton
    @Provides
    fun provideCommentDao(db: PSCoreDb): CommentDao {
        return db.commentDao()
    }

    @Singleton
    @Provides
    fun provideCommentDetailDao(db: PSCoreDb): CommentDetailDao {
        return db.commentDetailDao()
    }

    @Singleton
    @Provides
    fun provideNotificationDao(db: PSCoreDb): NotificationDao {
        return db.notificationDao()
    }

    @Singleton
    @Provides
    fun provideProductCollectionDao(db: PSCoreDb): ProductCollectionDao {
        return db.productCollectionDao()
    }

    @Singleton
    @Provides
    fun provideTransactionDao(db: PSCoreDb): TransactionDao {
        return db.transactionDao()
    }

    @Singleton
    @Provides
    fun provideTransactionOrderDao(db: PSCoreDb): TransactionOrderDao {
        return db.transactionOrderDao()
    }

    //    @Singleton
    //    @Provides
    //    TrendingCategoryDao provideTrendingCategoryDao(PSCoreDb db){return db.trendingCategoryDao();}
    @Singleton
    @Provides
    fun provideShopDao(db: PSCoreDb): ShopDao {
        return db.shopDao()
    }

    @Singleton
    @Provides
    fun provideNewsFeedDao(db: PSCoreDb): BlogDao {
        return db.blogDao()
    }

    @Singleton
    @Provides
    fun provideShippingMethodDao(db: PSCoreDb): ShippingMethodDao {
        return db.shippingMethodDao()
    }

    @Singleton
    @Provides
    fun provideProductMapDao(db: PSCoreDb): ProductMapDao {
        return db.productMapDao()
    }

    @Singleton
    @Provides
    fun provideCategoryMapDao(db: PSCoreDb): CategoryMapDao {
        return db.categoryMapDao()
    }

    @Singleton
    @Provides
    fun providePSAppInfoDao(db: PSCoreDb): PSAppInfoDao {
        return db.psAppInfoDao()
    }

    @Singleton
    @Provides
    fun providePSAppVersionDao(db: PSCoreDb): PSAppVersionDao {
        return db.psAppVersionDao()
    }

    @Singleton
    @Provides
    fun provideDeletedObjectDao(db: PSCoreDb): DeletedObjectDao {
        return db.deletedObjectDao()
    }
}