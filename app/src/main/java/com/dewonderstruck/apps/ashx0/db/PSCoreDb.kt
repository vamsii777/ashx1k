package com.dewonderstruck.apps.ashx0.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dewonderstruck.apps.ashx0.db.common.Converters
import com.dewonderstruck.apps.ashx0.viewobject.*

/**
 * Created by Vamsi Madduluri on 11/20/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Database(entities = [Image::class, Category::class, User::class, UserLogin::class, AboutUs::class, Product::class, LatestProduct::class, DiscountProduct::class, FeaturedProduct::class, SubCategory::class, ProductListByCatId::class, Comment::class, CommentDetail::class, ProductColor::class, ProductSpecs::class, RelatedProduct::class, FavouriteProduct::class, LikedProduct::class, ProductAttributeHeader::class, ProductAttributeDetail::class, Noti::class, TransactionObject::class, ProductCollectionHeader::class, ProductCollection::class, TransactionDetail::class, Basket::class, HistoryProduct::class, Shop::class, ShopTag::class, Blog::class, Rating::class, ShippingMethod::class, ShopByTagId::class, ProductMap::class, ShopMap::class, CategoryMap::class, PSAppInfo::class, PSAppVersion::class, DeletedObject::class, Country::class, City::class], version = 7, exportSchema = false) //V2.4 = DBV 7
//V2.3 = DBV 7
//V2.2 = DBV 7
//V2.1 = DBV 7
//V2.0 = DBV 7
//V1.9 = DBV 7
//V1.8 = DBV 7
//V1.7 = DBV 6
//V1.6 = DBV 5
//V1.5 = DBV 4
//V1.4 = DBV 3
//V1.3 = DBV 2
@TypeConverters(Converters::class)
abstract class PSCoreDb : RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun productColorDao(): ProductColorDao?
    abstract fun productSpecsDao(): ProductSpecsDao?
    abstract fun productAttributeHeaderDao(): ProductAttributeHeaderDao?
    abstract fun productAttributeDetailDao(): ProductAttributeDetailDao?
    abstract fun basketDao(): BasketDao?
    abstract fun historyDao(): HistoryDao?
    abstract fun aboutUsDao(): AboutUsDao?
    abstract fun imageDao(): ImageDao?
    abstract fun countryDao(): CountryDao?
    abstract fun cityDao(): CityDao?
    abstract fun ratingDao(): RatingDao?
    abstract fun commentDao(): CommentDao?
    abstract fun commentDetailDao(): CommentDetailDao?
    abstract fun productDao(): ProductDao?
    abstract fun categoryDao(): CategoryDao?
    abstract fun subCategoryDao(): SubCategoryDao?
    abstract fun notificationDao(): NotificationDao?
    abstract fun productCollectionDao(): ProductCollectionDao?
    abstract fun transactionDao(): TransactionDao?
    abstract fun transactionOrderDao(): TransactionOrderDao?
    abstract fun shopDao(): ShopDao?
    abstract fun blogDao(): BlogDao?
    abstract fun shippingMethodDao(): ShippingMethodDao?
    abstract fun productMapDao(): ProductMapDao?
    abstract fun categoryMapDao(): CategoryMapDao?
    abstract fun psAppInfoDao(): PSAppInfoDao?
    abstract fun psAppVersionDao(): PSAppVersionDao?
    abstract fun deletedObjectDao(): DeletedObjectDao? //    /**
    //     * Migrate from:
    //     * version 1 - using Room
    //     * to
    //     * version 2 - using Room where the {@link } has an extra field: addedDateStr
    //     */
    //    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    //        @Override
    //        public void migrate(@NonNull SupportSQLiteDatabase database) {
    //            database.execSQL("ALTER TABLE news "
    //                    + " ADD COLUMN addedDateStr INTEGER NOT NULL DEFAULT 0");
    //        }
    //    };
    /* More migration write here */
}