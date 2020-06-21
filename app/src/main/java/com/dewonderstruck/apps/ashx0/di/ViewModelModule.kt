package com.dewonderstruck.apps.ashx0.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dewonderstruck.apps.ashx0.viewmodel.aboutus.AboutUsViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.apploading.AppLoadingViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.blog.BlogViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.category.CategoryViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.city.CityViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.clearalldata.ClearAllDataViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.collection.ProductCollectionProductViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.collection.ProductCollectionViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.comment.CommentDetailListViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.comment.CommentListViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.common.NotificationViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSNewsViewModelFactory
import com.dewonderstruck.apps.ashx0.viewmodel.contactus.ContactUsViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.country.CountryViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.coupondiscount.CouponDiscountViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.homelist.*
import com.dewonderstruck.apps.ashx0.viewmodel.image.ImageViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.notification.NotificationsViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.paypal.PaypalViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.*
import com.dewonderstruck.apps.ashx0.viewmodel.rating.RatingViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shippingmethod.ShippingMethodViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shop.ShopViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.subcategory.SubCategoryViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.transaction.TransactionListViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.transaction.TransactionOrderViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Vamsi Madduluri on 11/16/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Module
internal abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: PSNewsViewModelFactory?): ViewModelProvider.Factory?

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel::class)
    abstract fun bindAboutUsViewModel(aboutUsViewModel: AboutUsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    abstract fun bindImageViewModel(imageViewModel: ImageViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel::class)
    abstract fun bindRatingViewModel(ratingViewModel: RatingViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(notificationViewModel: NotificationViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CountryViewModel::class)
    abstract fun bindCountryViewModel(countryViewModel: CountryViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel::class)
    abstract fun bindCityViewModel(cityViewModel: CityViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel::class)
    abstract fun bindContactUsViewModel(contactUsViewModel: ContactUsViewModel?): ViewModel?

    /*  @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract ViewModel bindProductViewModel(ProductViewModel productViewModel);*/
    @Binds
    @IntoMap
    @ViewModelKey(CommentListViewModel::class)
    abstract fun bindCommentViewModel(commentListViewModel: CommentListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CommentDetailListViewModel::class)
    abstract fun bindCommentDetailViewModel(commentDetailListViewModel: CommentDetailListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailViewModel::class)
    abstract fun bindProductDetailViewModel(productDetailViewModel: ProductDetailViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(TouchCountViewModel::class)
    abstract fun bindTouchCountViewModel(touchCountViewModel: TouchCountViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductColorViewModel::class)
    abstract fun bindProductColorViewModel(productColorViewModel: ProductColorViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductSpecsViewModel::class)
    abstract fun bindProductSpecsViewModel(productSpecsViewModel: ProductSpecsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BasketViewModel::class)
    abstract fun bindBasketViewModel(basketViewModel: BasketViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(HistoryProductViewModel::class)
    abstract fun bindHistoryProductViewModel(historyProductViewModel: HistoryProductViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductAttributeHeaderViewModel::class)
    abstract fun bindProductAttributeHeaderViewModel(productAttributeHeaderViewModel: ProductAttributeHeaderViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductAttributeDetailViewModel::class)
    abstract fun bindProductAttributeDetailViewModel(productAttributeDetailViewModel: ProductAttributeDetailViewModel?): ViewModel?

    /*

    @Binds
    @IntoMap
    @ViewModelKey(DiscountProductViewModel.class)
    abstract ViewModel bindDiscountProductViewModel(DiscountProductViewModel discountProductViewModel);
*/
    @Binds
    @IntoMap
    @ViewModelKey(ProductRelatedViewModel::class)
    abstract fun bindRelatedProductViewModel(productRelatedViewModel: ProductRelatedViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductFavouriteViewModel::class)
    abstract fun bindProductFavouriteViewModel(productFavouriteViewModel: ProductFavouriteViewModel?): ViewModel?

    //    @Binds
    //    @IntoMap
    //    @ViewModelKey(ProductLikedViewModel.class)
    //    abstract ViewModel bindProductLikedViewModel(ProductLikedViewModel productLikedViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(categoryViewModel: CategoryViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SubCategoryViewModel::class)
    abstract fun bindSubCategoryViewModel(subCategoryViewModel: SubCategoryViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductListByCatIdViewModel::class)
    abstract fun bindProductListByCatIdViewModel(productListByCatIdViewModel: ProductListByCatIdViewModel?): ViewModel?

    //    @Binds
    //    @IntoMap
    //    @ViewModelKey(TrendingProductsViewModel.class)
    //    abstract ViewModel bindTrendingProductsViewModel(TrendingProductsViewModel trendingProductsViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(HomeLatestProductViewModel::class)
    abstract fun bindHomeLatestProductViewModel(homeLatestProductViewModel: HomeLatestProductViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(HomeSearchProductViewModel::class)
    abstract fun bindHomeFeaturedProductViewModel(homeSearchProductViewModel: HomeSearchProductViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingProductViewModel::class)
    abstract fun bindHomeTrendingProductViewModel(homeTrendingProductViewModel: HomeTrendingProductViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(HomeFeaturedProductViewModel::class)
    abstract fun bindHomeCategory1ProductViewModel(homeFeaturedProductViewModel: HomeFeaturedProductViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductCollectionViewModel::class)
    abstract fun bindProductCollectionViewModel(productCollectionViewModel: ProductCollectionViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    abstract fun bindNotificationListViewModel(notificationListViewModel: NotificationsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(TransactionListViewModel::class)
    abstract fun bindTransactionListViewModel(transactionListViewModel: TransactionListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(TransactionOrderViewModel::class)
    abstract fun bindTransactionOrderViewModel(transactionOrderViewModel: TransactionOrderViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingCategoryListViewModel::class)
    abstract fun bindHomeTrendingCategoryListViewModel(transactionListViewModel: HomeTrendingCategoryListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductCollectionProductViewModel::class)
    abstract fun bindProductCollectionProductViewModel(productCollectionProductViewModel: ProductCollectionProductViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ShopViewModel::class)
    abstract fun bindShopViewModel(shopViewModel: ShopViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel::class)
    abstract fun bindNewsFeedViewModel(blogViewModel: BlogViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ShippingMethodViewModel::class)
    abstract fun bindShippingMethodViewModel(shippingMethodViewModel: ShippingMethodViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(PaypalViewModel::class)
    abstract fun bindPaypalViewModel(paypalViewModel: PaypalViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CouponDiscountViewModel::class)
    abstract fun bindCouponDiscountViewModel(couponDiscountViewModel: CouponDiscountViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(AppLoadingViewModel::class)
    abstract fun bindPSAppInfoViewModel(psAppInfoViewModel: AppLoadingViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ClearAllDataViewModel::class)
    abstract fun bindClearAllDataViewModel(clearAllDataViewModel: ClearAllDataViewModel?): ViewModel?
}