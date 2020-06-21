package com.dewonderstruck.apps.ashx0.db;

import com.dewonderstruck.apps.ashx0.db.common.Converters;
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs;
import com.dewonderstruck.apps.ashx0.viewobject.Basket;
import com.dewonderstruck.apps.ashx0.viewobject.Blog;
import com.dewonderstruck.apps.ashx0.viewobject.Category;
import com.dewonderstruck.apps.ashx0.viewobject.CategoryMap;
import com.dewonderstruck.apps.ashx0.viewobject.City;
import com.dewonderstruck.apps.ashx0.viewobject.Comment;
import com.dewonderstruck.apps.ashx0.viewobject.CommentDetail;
import com.dewonderstruck.apps.ashx0.viewobject.Country;
import com.dewonderstruck.apps.ashx0.viewobject.DeletedObject;
import com.dewonderstruck.apps.ashx0.viewobject.DiscountProduct;
import com.dewonderstruck.apps.ashx0.viewobject.FavouriteProduct;
import com.dewonderstruck.apps.ashx0.viewobject.FeaturedProduct;
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct;
import com.dewonderstruck.apps.ashx0.viewobject.Image;
import com.dewonderstruck.apps.ashx0.viewobject.LatestProduct;
import com.dewonderstruck.apps.ashx0.viewobject.LikedProduct;
import com.dewonderstruck.apps.ashx0.viewobject.Noti;
import com.dewonderstruck.apps.ashx0.viewobject.PSAppInfo;
import com.dewonderstruck.apps.ashx0.viewobject.PSAppVersion;
import com.dewonderstruck.apps.ashx0.viewobject.Product;
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail;
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeHeader;
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollection;
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader;
import com.dewonderstruck.apps.ashx0.viewobject.ProductColor;
import com.dewonderstruck.apps.ashx0.viewobject.ProductListByCatId;
import com.dewonderstruck.apps.ashx0.viewobject.ProductMap;
import com.dewonderstruck.apps.ashx0.viewobject.ProductSpecs;
import com.dewonderstruck.apps.ashx0.viewobject.Rating;
import com.dewonderstruck.apps.ashx0.viewobject.RelatedProduct;
import com.dewonderstruck.apps.ashx0.viewobject.ShippingMethod;
import com.dewonderstruck.apps.ashx0.viewobject.Shop;
import com.dewonderstruck.apps.ashx0.viewobject.ShopByTagId;
import com.dewonderstruck.apps.ashx0.viewobject.ShopMap;
import com.dewonderstruck.apps.ashx0.viewobject.ShopTag;
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory;
import com.dewonderstruck.apps.ashx0.viewobject.TransactionDetail;
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject;
import com.dewonderstruck.apps.ashx0.viewobject.User;
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


/**
 * Created by Vamsi Madduluri on 11/20/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Database(entities = {
        Image.class,
        Category.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        Product.class,
        LatestProduct.class,
        DiscountProduct.class,
        FeaturedProduct.class,
        SubCategory.class,
        ProductListByCatId.class,
        Comment.class,
        CommentDetail.class,
        ProductColor.class,
        ProductSpecs.class,
        RelatedProduct.class,
        FavouriteProduct.class,
        LikedProduct.class,
        ProductAttributeHeader.class,
        ProductAttributeDetail.class,
        Noti.class,
        TransactionObject.class,
        ProductCollectionHeader.class,
        ProductCollection.class,
        TransactionDetail.class,
        Basket.class,
        HistoryProduct.class,
        Shop.class,
        ShopTag.class,
        Blog.class,
        Rating.class,
        ShippingMethod.class,
        ShopByTagId.class,
        ProductMap.class,
        ShopMap.class,
        CategoryMap.class,
        PSAppInfo.class,
        PSAppVersion.class,
        DeletedObject.class,
        Country.class,
        City.class

}, version = 7, exportSchema = false)
//V2.4 = DBV 7
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


@TypeConverters({Converters.class})

public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public ProductColorDao productColorDao();

    abstract public ProductSpecsDao productSpecsDao();

    abstract public ProductAttributeHeaderDao productAttributeHeaderDao();

    abstract public ProductAttributeDetailDao productAttributeDetailDao();

    abstract public BasketDao basketDao();

    abstract public HistoryDao historyDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public CountryDao countryDao();

    abstract public CityDao cityDao();

    abstract public RatingDao ratingDao();

    abstract public CommentDao commentDao();

    abstract public CommentDetailDao commentDetailDao();

    abstract public ProductDao productDao();

    abstract public CategoryDao categoryDao();

    abstract public SubCategoryDao subCategoryDao();

    abstract public NotificationDao notificationDao();

    abstract public ProductCollectionDao productCollectionDao();

    abstract public TransactionDao transactionDao();

    abstract public TransactionOrderDao transactionOrderDao();

    abstract public ShopDao shopDao();

    abstract public BlogDao blogDao();

    abstract public ShippingMethodDao shippingMethodDao();

    abstract public ProductMapDao productMapDao();

    abstract public CategoryMapDao categoryMapDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();


//    /**
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