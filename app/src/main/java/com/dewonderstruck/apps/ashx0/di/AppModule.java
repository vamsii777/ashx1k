package com.dewonderstruck.apps.ashx0.di;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.PSApiService;
import com.dewonderstruck.apps.ashx0.db.AboutUsDao;
import com.dewonderstruck.apps.ashx0.db.BasketDao;
import com.dewonderstruck.apps.ashx0.db.BlogDao;
import com.dewonderstruck.apps.ashx0.db.CategoryDao;
import com.dewonderstruck.apps.ashx0.db.CategoryMapDao;
import com.dewonderstruck.apps.ashx0.db.CityDao;
import com.dewonderstruck.apps.ashx0.db.CommentDao;
import com.dewonderstruck.apps.ashx0.db.CommentDetailDao;
import com.dewonderstruck.apps.ashx0.db.CountryDao;
import com.dewonderstruck.apps.ashx0.db.DeletedObjectDao;
import com.dewonderstruck.apps.ashx0.db.HistoryDao;
import com.dewonderstruck.apps.ashx0.db.ImageDao;
import com.dewonderstruck.apps.ashx0.db.NotificationDao;
import com.dewonderstruck.apps.ashx0.db.PSAppInfoDao;
import com.dewonderstruck.apps.ashx0.db.PSAppVersionDao;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.db.ProductAttributeDetailDao;
import com.dewonderstruck.apps.ashx0.db.ProductAttributeHeaderDao;
import com.dewonderstruck.apps.ashx0.db.ProductCollectionDao;
import com.dewonderstruck.apps.ashx0.db.ProductColorDao;
import com.dewonderstruck.apps.ashx0.db.ProductDao;
import com.dewonderstruck.apps.ashx0.db.ProductMapDao;
import com.dewonderstruck.apps.ashx0.db.ProductSpecsDao;
import com.dewonderstruck.apps.ashx0.db.RatingDao;
import com.dewonderstruck.apps.ashx0.db.ShippingMethodDao;
import com.dewonderstruck.apps.ashx0.db.ShopDao;
import com.dewonderstruck.apps.ashx0.db.SubCategoryDao;
import com.dewonderstruck.apps.ashx0.db.TransactionDao;
import com.dewonderstruck.apps.ashx0.db.TransactionOrderDao;
import com.dewonderstruck.apps.ashx0.db.UserDao;
import com.dewonderstruck.apps.ashx0.utils.AppLanguage;
import com.dewonderstruck.apps.ashx0.utils.Connectivity;
import com.dewonderstruck.apps.ashx0.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Vamsi Madduluri on 11/15/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    PSApiService providePSApiService() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Config.APP_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(PSApiService.class);

    }

    @Singleton
    @Provides
    PSCoreDb provideDb(Application app) {
        return Room.databaseBuilder(app, PSCoreDb.class, "PSApp.db")
                //.addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    Connectivity provideConnectivity(Application app) {
        return new Connectivity(app);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

    @Singleton
    @Provides
    UserDao provideUserDao(PSCoreDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    AppLanguage provideCurrentLanguage(SharedPreferences sharedPreferences) {
        return new AppLanguage(sharedPreferences);
    }

    @Singleton
    @Provides
    AboutUsDao provideAboutUsDao(PSCoreDb db) {
        return db.aboutUsDao();
    }

    @Singleton
    @Provides
    ImageDao provideImageDao(PSCoreDb db) {
        return db.imageDao();
    }

    @Singleton
    @Provides
    ProductDao provideProductDao(PSCoreDb db) {
        return db.productDao();
    }

    @Singleton
    @Provides
    CountryDao provideCountryDao(PSCoreDb db) {
        return db.countryDao();
    }

    @Singleton
    @Provides
    CityDao provideCityDao(PSCoreDb db) {
        return db.cityDao();
    }

    @Singleton
    @Provides
    ProductColorDao provideProductColorDao(PSCoreDb db) {
        return db.productColorDao();
    }

    @Singleton
    @Provides
    ProductSpecsDao provideProductSpecsDao(PSCoreDb db) {
        return db.productSpecsDao();
    }

    @Singleton
    @Provides
    ProductAttributeHeaderDao provideProductAttributeHeaderDao(PSCoreDb db) {
        return db.productAttributeHeaderDao();
    }

    @Singleton
    @Provides
    ProductAttributeDetailDao provideProductAttributeDetailDao(PSCoreDb db) {
        return db.productAttributeDetailDao();
    }

    @Singleton
    @Provides
    BasketDao provideBasketDao(PSCoreDb db) {
        return db.basketDao();
    }

    @Singleton
    @Provides
    HistoryDao provideHistoryDao(PSCoreDb db) {
        return db.historyDao();
    }

    @Singleton
    @Provides
    CategoryDao provideCategoryDao(PSCoreDb db) {
        return db.categoryDao();
    }

    @Singleton
    @Provides
    RatingDao provideRatingDao(PSCoreDb db) {
        return db.ratingDao();
    }

    @Singleton
    @Provides
    SubCategoryDao provideSubCategoryDao(PSCoreDb db) {
        return db.subCategoryDao();
    }

    @Singleton
    @Provides
    CommentDao provideCommentDao(PSCoreDb db) {
        return db.commentDao();
    }

    @Singleton
    @Provides
    CommentDetailDao provideCommentDetailDao(PSCoreDb db) {
        return db.commentDetailDao();
    }

    @Singleton
    @Provides
    NotificationDao provideNotificationDao(PSCoreDb db){return db.notificationDao();}

    @Singleton
    @Provides
    ProductCollectionDao provideProductCollectionDao(PSCoreDb db){return db.productCollectionDao();}

    @Singleton
    @Provides
    TransactionDao provideTransactionDao(PSCoreDb db){return db.transactionDao();}

    @Singleton
    @Provides
    TransactionOrderDao provideTransactionOrderDao(PSCoreDb db){return db.transactionOrderDao();}

//    @Singleton
//    @Provides
//    TrendingCategoryDao provideTrendingCategoryDao(PSCoreDb db){return db.trendingCategoryDao();}

    @Singleton
    @Provides
    ShopDao provideShopDao(PSCoreDb db){return db.shopDao();}

    @Singleton
    @Provides
    BlogDao provideNewsFeedDao(PSCoreDb db){return db.blogDao();}

    @Singleton
    @Provides
    ShippingMethodDao provideShippingMethodDao(PSCoreDb db){return db.shippingMethodDao();}

    @Singleton
    @Provides
    ProductMapDao provideProductMapDao(PSCoreDb db){return db.productMapDao();}

    @Singleton
    @Provides
    CategoryMapDao provideCategoryMapDao(PSCoreDb db){return db.categoryMapDao();}

    @Singleton
    @Provides
    PSAppInfoDao providePSAppInfoDao(PSCoreDb db){return db.psAppInfoDao();}

    @Singleton
    @Provides
    PSAppVersionDao providePSAppVersionDao(PSCoreDb db){return db.psAppVersionDao();}

    @Singleton
    @Provides
    DeletedObjectDao provideDeletedObjectDao(PSCoreDb db){return db.deletedObjectDao();}
}
