package com.dewonderstruck.apps.ashx0.di

import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.ui.apploading.AppLoadingActivity
import com.dewonderstruck.apps.ashx0.ui.apploading.AppLoadingFragment

import com.dewonderstruck.apps.ashx0.ui.blog.detail.BlogDetailActivity
import com.dewonderstruck.apps.ashx0.ui.blog.detail.BlogDetailFragment
import com.dewonderstruck.apps.ashx0.ui.blog.list.BlogListActivity
import com.dewonderstruck.apps.ashx0.ui.blog.list.BlogListFragment
import com.dewonderstruck.apps.ashx0.ui.category.CategoryListActivity
import com.dewonderstruck.apps.ashx0.ui.category.CategoryListFragment
import com.dewonderstruck.apps.ashx0.ui.category.TrendingCategoryFragment

import com.dewonderstruck.apps.ashx0.ui.collection.CollectionActivity
import com.dewonderstruck.apps.ashx0.ui.collection.CollectionFragment
import com.dewonderstruck.apps.ashx0.ui.collection.productCollectionHeader.ProductCollectionHeaderListFragment

import com.dewonderstruck.apps.ashx0.ui.contactus.ContactUsFragment
import com.dewonderstruck.apps.ashx0.ui.danceoholics.DanceholicsFragment
import com.dewonderstruck.apps.ashx0.ui.forceupdate.ForceUpdateActivity
import com.dewonderstruck.apps.ashx0.ui.forceupdate.ForceUpdateFragment
import com.dewonderstruck.apps.ashx0.ui.gallery.GalleryActivity
import com.dewonderstruck.apps.ashx0.ui.gallery.GalleryFragment
import com.dewonderstruck.apps.ashx0.ui.gallery.detail.GalleryDetailActivity
import com.dewonderstruck.apps.ashx0.ui.gallery.detail.GalleryDetailFragment
import com.dewonderstruck.apps.ashx0.ui.language.LanguageFragment
import com.dewonderstruck.apps.ashx0.ui.notification.detail.NotificationActivity
import com.dewonderstruck.apps.ashx0.ui.notification.detail.NotificationFragment
import com.dewonderstruck.apps.ashx0.ui.notification.list.NotificationListActivity
import com.dewonderstruck.apps.ashx0.ui.notification.list.NotificationListFragment
import com.dewonderstruck.apps.ashx0.ui.notification.setting.NotificationSettingFragment
import com.dewonderstruck.apps.ashx0.ui.privacyandpolicy.PrivacyPolicyActivity
import com.dewonderstruck.apps.ashx0.ui.privacyandpolicy.PrivacyPolicyFragment
import com.dewonderstruck.apps.ashx0.ui.product.MainFragment
import com.dewonderstruck.apps.ashx0.ui.product.detail.ProductActivity
import com.dewonderstruck.apps.ashx0.ui.product.detail.ProductDetailFragment
import com.dewonderstruck.apps.ashx0.ui.product.favourite.FavouriteListActivity
import com.dewonderstruck.apps.ashx0.ui.product.favourite.FavouriteListFragment
import com.dewonderstruck.apps.ashx0.ui.product.filtering.CategoryFilterFragment
import com.dewonderstruck.apps.ashx0.ui.product.filtering.FilterFragment
import com.dewonderstruck.apps.ashx0.ui.product.filtering.FilteringActivity
import com.dewonderstruck.apps.ashx0.ui.product.history.HistoryFragment
import com.dewonderstruck.apps.ashx0.ui.product.history.UserHistoryListActivity
import com.dewonderstruck.apps.ashx0.ui.product.list.ProductListActivity
import com.dewonderstruck.apps.ashx0.ui.product.list.ProductListFragment
import com.dewonderstruck.apps.ashx0.ui.product.productbycatId.ProductListByCatIdActivity
import com.dewonderstruck.apps.ashx0.ui.product.productbycatId.ProductListByCatIdFragment
import com.dewonderstruck.apps.ashx0.ui.product.search.*

import com.dewonderstruck.apps.ashx0.ui.setting.*
import com.dewonderstruck.apps.ashx0.ui.shop.ShopProfileFragment

import com.dewonderstruck.apps.ashx0.ui.terms.TermsAndConditionsActivity
import com.dewonderstruck.apps.ashx0.ui.terms.TermsAndConditionsFragment

import com.dewonderstruck.apps.ashx0.ui.user.*
import com.dewonderstruck.apps.ashx0.ui.user.ProfileEditActivity
import com.dewonderstruck.apps.ashx0.ui.user.phonelogin.PhoneLoginActivity
import com.dewonderstruck.apps.ashx0.ui.user.phonelogin.PhoneLoginFragment
import com.dewonderstruck.apps.ashx0.ui.user.verifyemail.VerifyEmailActivity
import com.dewonderstruck.apps.ashx0.ui.user.verifyemail.VerifyEmailFragment
import com.dewonderstruck.apps.ashx0.ui.user.verifyphone.VerifyMobileActivity
import com.dewonderstruck.apps.ashx0.ui.user.verifyphone.VerifyMobileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Vamsi Madduluri on 11/15/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Module
internal abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributeMainActivity(): MainActivity?


    @ContributesAndroidInjector(modules = [FavouriteListModule::class])
    abstract fun contributeFavouriteListActivity(): FavouriteListActivity?

    @ContributesAndroidInjector(modules = [UserHistoryModule::class])
    abstract fun contributeUserHistoryListActivity(): UserHistoryListActivity?

    @ContributesAndroidInjector(modules = [UserRegisterModule::class])
    abstract fun contributeUserRegisterActivity(): UserRegisterActivity?

    @ContributesAndroidInjector(modules = [UserForgotPasswordModule::class])
    abstract fun contributeUserForgotPasswordActivity(): UserForgotPasswordActivity?

    @ContributesAndroidInjector(modules = [UserLoginModule::class])
    abstract fun contributeUserLoginActivity(): UserLoginActivity?

    @ContributesAndroidInjector(modules = [PasswordChangeModule::class])
    abstract fun contributePasswordChangeActivity(): PasswordChangeActivity?

    @ContributesAndroidInjector(modules = [ProductListByCatIdModule::class])
    abstract fun productListByCatIdActivity(): ProductListByCatIdActivity?

    @ContributesAndroidInjector(modules = [FilteringModule::class])
    abstract fun filteringActivity(): FilteringActivity?



    @ContributesAndroidInjector(modules = [DiscountDetailModule::class])
    abstract fun discountDetailActivity(): ProductActivity?

    @ContributesAndroidInjector(modules = [NotificationModule::class])
    abstract fun notificationActivity(): NotificationListActivity?

    @ContributesAndroidInjector(modules = [HomeFilteringActivityModule::class])
    abstract fun contributehomeFilteringActivity(): ProductListActivity?

    @ContributesAndroidInjector(modules = [NotificationDetailModule::class])
    abstract fun notificationDetailActivity(): NotificationActivity?



    @ContributesAndroidInjector(modules = [GalleryDetailActivityModule::class])
    abstract fun galleryDetailActivity(): GalleryDetailActivity?

    @ContributesAndroidInjector(modules = [GalleryActivityModule::class])
    abstract fun galleryActivity(): GalleryActivity?

    @ContributesAndroidInjector(modules = [SearchByCategoryActivityModule::class])
    abstract fun searchByCategoryActivity(): SearchByCategoryActivity?

    @ContributesAndroidInjector(modules = [TermsAndConditionsModule::class])
    abstract fun termsAndConditionsActivity(): TermsAndConditionsActivity?

    @ContributesAndroidInjector(modules = [EditSettingModule::class])
    abstract fun editSettingActivity(): SettingActivity?

    @ContributesAndroidInjector(modules = [LanguageChangeModule::class])
    abstract fun languageChangeActivity(): NotificationSettingActivity?

    @ContributesAndroidInjector(modules = [ProfileEditModule::class])
    abstract fun contributeProfileEditActivity(): ProfileEditActivity?

    @ContributesAndroidInjector(modules = [AppInfoModule::class])
    abstract fun AppInfoActivity(): AppInfoActivity?

    @ContributesAndroidInjector(modules = [CategoryListActivityAppInfoModule::class])
    abstract fun categoryListActivity(): CategoryListActivity?



    @ContributesAndroidInjector(modules = [CollectionModule::class])
    abstract fun collectionActivity(): CollectionActivity?


    @ContributesAndroidInjector(modules = [BlogListActivityModule::class])
    abstract fun BlogListActivity(): BlogListActivity?

    @ContributesAndroidInjector(modules = [BlogDetailModule::class])
    abstract fun blogDetailActivity(): BlogDetailActivity?

    @ContributesAndroidInjector(modules = [forceUpdateModule::class])
    abstract fun forceUpdateActivity(): ForceUpdateActivity?

    @ContributesAndroidInjector(modules = [appLoadingModule::class])
    abstract fun appLoadingActivity(): AppLoadingActivity?

    @ContributesAndroidInjector(modules = [VerifyEmailModule::class])
    abstract fun contributeVerifyEmailActivity(): VerifyEmailActivity?

    @ContributesAndroidInjector(modules = [PrivacyPolicyActivityModule::class])
    abstract fun contributePrivacyPolicyActivity(): PrivacyPolicyActivity?

    @ContributesAndroidInjector(modules = [PhoneLoginActivityModule::class])
    abstract fun contributePhoneLoginActivity(): PhoneLoginActivity?

    @ContributesAndroidInjector(modules = [VerifyMobileModule::class])
    abstract fun contributeVerifyMobileActivity(): VerifyMobileActivity?
}

@Module
internal abstract class CheckoutActivityModule {

    @ContributesAndroidInjector
    abstract fun languageFragment(): LanguageFragment?

}

@Module
internal abstract class MainModule {
    @ContributesAndroidInjector
    abstract fun contributefeaturedProductFragment(): ProductListFragment?

    @ContributesAndroidInjector
    abstract fun contributeSelectedShopFragment(): MainFragment?

    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryListFragment?

    @ContributesAndroidInjector
    abstract fun contributeTypeFilterFragment(): CategoryFilterFragment?

    @ContributesAndroidInjector
    abstract fun contributeProductCollectionHeaderListFragment(): ProductCollectionHeaderListFragment?

    @ContributesAndroidInjector
    abstract fun contributeContactUsFragment(): ContactUsFragment?

    @ContributesAndroidInjector
    abstract fun contributeUserLoginFragment(): UserLoginFragment?

    @ContributesAndroidInjector
    abstract fun contributeUserForgotPasswordFragment(): UserForgotPasswordFragment?

    @ContributesAndroidInjector
    abstract fun contributeUserRegisterFragment(): UserRegisterFragment?

    @ContributesAndroidInjector
    abstract fun contributeNotificationSettingFragment(): NotificationSettingFragment?

    @ContributesAndroidInjector
    abstract fun contributeDanceholicsFragment(): DanceholicsFragment?

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment?

    @ContributesAndroidInjector
    abstract fun contributeLanguageFragment(): LanguageFragment?

    @ContributesAndroidInjector
    abstract fun contributeFavouriteListFragment(): FavouriteListFragment?


    @ContributesAndroidInjector
    abstract fun contributEditSettingFragment(): SettingFragment?

    @ContributesAndroidInjector
    abstract fun historyFragment(): HistoryFragment?

    @ContributesAndroidInjector
    abstract fun contributeAboutUsFragmentFragment(): ShopProfileFragment?



    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment?

    @ContributesAndroidInjector
    abstract fun contributeNotificationFragment(): NotificationListFragment?

    @ContributesAndroidInjector
    abstract fun contributeAppInfoFragment(): AppInfoFragment?

    @ContributesAndroidInjector
    abstract fun contributeVerifyEmailFragment(): VerifyEmailFragment?

    @ContributesAndroidInjector
    abstract fun contributePrivacyPolicyFragment(): PrivacyPolicyFragment?

    @ContributesAndroidInjector
    abstract fun contributePhoneLoginFragment(): PhoneLoginFragment?

    @ContributesAndroidInjector
    abstract fun contributeVerifyMobileFragment(): VerifyMobileFragment?
}

@Module
internal abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract fun contributeProfileEditFragment(): ProfileEditFragment?
}


@Module
internal abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract fun contributeFavouriteFragment(): FavouriteListFragment?
}

@Module
internal abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract fun contributeUserRegisterFragment(): UserRegisterFragment?
}

@Module
internal abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract fun contributeUserForgotPasswordFragment(): UserForgotPasswordFragment?
}

@Module
internal abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract fun contributeUserLoginFragment(): UserLoginFragment?
}

@Module
internal abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract fun contributePasswordChangeFragment(): PasswordChangeFragment?
}



@Module
internal abstract class DiscountDetailModule {
    @ContributesAndroidInjector
    abstract fun discountDetailFragment(): ProductDetailFragment?
}

@Module
internal abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract fun notificationFragment(): NotificationListFragment?
}

@Module
internal abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract fun notificationDetailFragment(): NotificationFragment?
}



@Module
internal abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract fun contributeHistoryFragment(): HistoryFragment?
}

@Module
internal abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract fun contributeAppInfoFragment(): AppInfoFragment?
}

@Module
internal abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryListFragment?

    @ContributesAndroidInjector
    abstract fun contributeTrendingCategoryFragment(): TrendingCategoryFragment?
}

@Module
internal abstract class TermsAndConditionsModule {
    @ContributesAndroidInjector
    abstract fun TermsAndConditionsFragment(): TermsAndConditionsFragment?
}

@Module
internal abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract fun EditSettingFragment(): SettingFragment?
}

@Module
internal abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract fun notificationSettingFragment(): NotificationSettingFragment?
}

@Module
internal abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract fun ProfileFragment(): ProfileFragment?
}

@Module
internal abstract class ProductListByCatIdModule {
    @ContributesAndroidInjector
    abstract fun contributeProductListByCatIdFragment(): ProductListByCatIdFragment?
}

@Module
internal abstract class FilteringModule {
    @ContributesAndroidInjector
    abstract fun contributeTypeFilterFragment(): CategoryFilterFragment?

    @ContributesAndroidInjector
    abstract fun contributeSpecialFilteringFragment(): FilterFragment?
}

@Module
internal abstract class HomeFilteringActivityModule {
    @ContributesAndroidInjector
    abstract fun contributefeaturedProductFragment(): ProductListFragment?

    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryListFragment?

    @ContributesAndroidInjector
    abstract fun contributeTypeFilterFragment(): CategoryFilterFragment?

    @ContributesAndroidInjector
    abstract fun contributeCollectionFragment(): CollectionFragment?
}



@Module
internal abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeGalleryDetailFragment(): GalleryDetailFragment?
}

@Module
internal abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeGalleryFragment(): GalleryFragment?
}

@Module
internal abstract class SearchByCategoryActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSearchCategoryFragment(): SearchCategoryFragment?

    @ContributesAndroidInjector
    abstract fun contributeSearchSubCategoryFragment(): SearchSubCategoryFragment?

    @ContributesAndroidInjector
    abstract fun contributeSearchCityListFragment(): SearchCityListFragment?
}

@Module
internal abstract class CollectionModule {
    @ContributesAndroidInjector
    abstract fun contributeCollectionFragment(): CollectionFragment?
}



@Module
internal abstract class BlogListActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeBlogListFragment(): BlogListFragment?
}

@Module
internal abstract class BlogDetailModule {
    @ContributesAndroidInjector
    abstract fun contributeBlogDetailFragment(): BlogDetailFragment?
}

@Module
internal abstract class forceUpdateModule {
    @ContributesAndroidInjector
    abstract fun contributeForceUpdateFragment(): ForceUpdateFragment?
}

@Module
internal abstract class appLoadingModule {
    @ContributesAndroidInjector
    abstract fun contributeAppLoadingFragment(): AppLoadingFragment?
}

@Module
internal abstract class VerifyEmailModule {
    @ContributesAndroidInjector
    abstract fun contributeVerifyEmailFragment(): VerifyEmailFragment?
}

@Module
internal abstract class PrivacyPolicyActivityModule {
    @ContributesAndroidInjector
    abstract fun contributePrivacyPolicyFragment(): PrivacyPolicyFragment?
}

@Module
internal abstract class PhoneLoginActivityModule {
    @ContributesAndroidInjector
    abstract fun cameraPhoneLoginFragment(): PhoneLoginFragment?
}

@Module
internal abstract class VerifyMobileModule {
    @ContributesAndroidInjector
    abstract fun contributeVerifyMobileFragment(): VerifyMobileFragment?
}