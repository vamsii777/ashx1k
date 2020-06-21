package com.dewonderstruck.apps.ashx0.api

import androidx.lifecycle.LiveData
import com.dewonderstruck.apps.ashx0.viewobject.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * REST API access points
 */
interface ApiService {
    //region Products
    //region Get Product Collection
    @GET("rest/collections/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    fun getProductCollectionHeader(@Path("API_KEY") apiKey: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<ProductCollectionHeader?>?>?>?

    //endregion
    //region Get product detail related product list
    @GET("rest/products/related_product_trending/api_key/{API_KEY}/id/{id}/cat_id/{cat_id}")
    fun getProductDetailRelatedList(@Path("API_KEY") apiKey: String?, @Path("id") id: String?, @Path("cat_id") catId: String?): LiveData<ApiResponse<List<Product?>?>?>?

    //endregion
    //region Get favourite product list
    @GET("rest/products/get_favourite/api_key/{API_KEY}/login_user_id/{login_user_id}/limit/{limit}/offset/{offset}")
    fun getFavouriteList(@Path("API_KEY") apiKey: String?, @Path("login_user_id") userId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<Product?>?>?>?

    //endregion
    //region Get Product by Category Id
    @GET("rest/products/get/api_key/{API_KEY}/login_user_id/{login_user_id}/limit/{limit}/offset/{offset}/cat_id/{cat_id}")
    fun getProductListByCatId(@Path("API_KEY") apiKey: String?, @Path("login_user_id") loginUserId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?, @Path("cat_id") catId: String?): LiveData<ApiResponse<List<Product?>?>?>?

    //end region
    //endregion
    //region Get Collection Product List
    @GET("rest/products/all_collection_products/api_key/{API_KEY}/limit/{limit}/offset/{offset}/id/{id}")
    fun getCollectionProducts(@Path("API_KEY") apiKey: String?, @Path("limit") limit: String?, @Path("offset") offset: String?, @Path("id") id: String?): LiveData<ApiResponse<List<Product?>?>?>?

    //endregion
    //region Post Favourite Product
    @FormUrlEncoded
    @POST("rest/favourites/press/api_key/{API_KEY}")
    fun setPostFavourite(
            @Path("API_KEY") api_key: String?,
            @Field("product_id") productId: String?,
            @Field("user_id") userId: String?): Call<Product?>?

    //endregion
    //region Search Product
    @FormUrlEncoded
    @POST("rest/products/search/api_key/{API_KEY}/limit/{limit}/offset/{offset}/login_user_id/{login_user_id}")
    fun searchProduct(
            @Path("API_KEY") api_key: String?,
            @Path("login_user_id") login_user_id: String?,
            @Path("limit") limit: String?,
            @Path("offset") offset: String?,
            @Field("searchterm") searchterm: String?,
            @Field("cat_id") catId: String?,
            @Field("sub_cat_id") subCatId: String?,
            @Field("is_featured") isFeatured: String?,
            @Field("is_discount") isDiscount: String?,
            @Field("is_available") isAvailable: String?,
            @Field("max_price") max_price: String?,
            @Field("min_price") min_price: String?,
            @Field("rating_value") rating_value: String?,
            @Field("order_by") order_by: String?,
            @Field("order_type") order_type: String?): LiveData<ApiResponse<List<Product?>?>?>?

    //endregion
    //region Get Product Detail
    @GET("rest/products/get/api_key/{API_KEY}/id/{id}/login_user_id/{login_user_id}")
    fun getProductDetail(@Path("API_KEY") apiKey: String?, @Path("id") Id: String?, @Path("login_user_id") login_user_id: String?): LiveData<ApiResponse<Product?>?>?

    //endregion
    //endregion
    //region Get Image List
    @GET("rest/images/get/api_key/{API_KEY}/img_parent_id/{img_parent_id}/img_type/{img_type}")
    fun getImageList(@Path("API_KEY") apiKey: String?, @Path("img_parent_id") img_parent_id: String?, @Path("img_type") imageType: String?): LiveData<ApiResponse<List<Image?>?>?>?

    //endregion
    //region Comments
    //region Get commentlist
    @GET("rest/commentheaders/get/api_key/{API_KEY}/product_id/{product_id}/limit/{limit}/offset/{offset}")
    fun getCommentList(@Path("API_KEY") apiKey: String?, @Path("product_id") productId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<Comment?>?>?>?

    //endregion
    //region Get comment detail list
    @GET("rest/commentdetails/get/api_key/{API_KEY}/header_id/{header_id}/limit/{limit}/offset/{offset}")
    fun getCommentDetailList(@Path("API_KEY") apiKey: String?, @Path("header_id") headerId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<CommentDetail?>?>?>?

    //endregion
    //region Get comment detail count
    @GET("rest/commentheaders/get/api_key/{API_KEY}/id/{id}")
    fun getRawCommentDetailCount(@Path("API_KEY") apiKey: String?, @Path("id") id: String?): Call<Comment?>?

    //endregion
    //region Post comment header
    @FormUrlEncoded
    @POST("rest/commentheaders/press/api_key/{API_KEY}")
    fun rawCommentHeaderPost(
            @Path("API_KEY") apiKey: String?,
            @Field("product_id") productId: String?,
            @Field("user_id") userId: String?,
            @Field("header_comment") headerComment: String?): Call<List<Comment?>?>?

    //endregion
    //region Post comment detail
    @FormUrlEncoded
    @POST("rest/commentdetails/press/api_key/{API_KEY}")
    fun rawCommentDetailPost(
            @Path("API_KEY") apiKey: String?,
            @Field("header_id") headerId: String?,
            @Field("user_id") userId: String?,
            @Field("detail_comment") detailComment: String?): Call<List<CommentDetail?>?>?

    //endregion
    //endregion
    //region Notification
    //region Submit Notification Token
    @FormUrlEncoded
    @POST("rest/notis/register/api_key/{API_KEY}")
    fun rawRegisterNotiToken(@Path("API_KEY") apiKey: String?, @Field("platform_name") platform: String?, @Field("device_id") deviceId: String?): Call<ApiStatus?>?

    @FormUrlEncoded
    @POST("rest/notis/unregister/api_key/{API_KEY}")
    fun rawUnregisterNotiToken(@Path("API_KEY") apiKey: String?, @Field("platform_name") platform: String?, @Field("device_id") deviceId: String?): Call<ApiStatus?>?

    //endregion
    //region Get Notification List
    @FormUrlEncoded
    @POST("rest/notis/all_notis/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    fun getNotificationList(@Path("API_KEY") apiKey: String?,
                            @Path("limit") limit: String?,
                            @Path("offset") offset: String?,
                            @Field("user_id") userId: String?,
                            @Field("device_token") deviceToken: String?): LiveData<ApiResponse<List<Noti?>?>?>?

    //endregion
    //region Get Notification detail
    @GET("rest/notis/get/api_key/{API_KEY}/id/{id}")
    fun getNotificationDetail(@Path("API_KEY") apiKey: String?, @Path("id") id: String?): LiveData<ApiResponse<Noti?>?>?

    //endregion
    //region Is Read Notificaiton
    @FormUrlEncoded
    @POST("rest/notis/is_read/api_key/{API_KEY}")
    fun isReadNoti(
            @Path("API_KEY") apiKey: String?,
            @Field("noti_id") noti_id: String?,
            @Field("user_id") userId: String?,
            @Field("device_token") device_token: String?): Call<Noti?>?

    //endregion
    //endregion
    //region Transactions
    //region Get transaction detail
    @GET("rest/transactionheaders/get/api_key/{API_KEY}/user_id/{user_id}/id/{id}")
    fun getTransactionDetail(@Path("API_KEY") apiKey: String?, @Path("user_id") user_id: String?, @Path("id") id: String?): LiveData<ApiResponse<TransactionObject?>?>?

    //endregion
    //region Get transaction list
    @GET("rest/transactionheaders/get/api_key/{API_KEY}/user_id/{user_id}/limit/{limit}/offset/{offset}")
    fun getTransList(@Path("API_KEY") apiKey: String?, @Path("user_id") userId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<TransactionObject?>?>?>?

    //endregion
    //region Get transaction order list
    @GET("rest/transactiondetails/get/api_key/{API_KEY}/transactions_header_id/{transactions_header_id}/limit/{limit}/offset/{offset}")
    fun getTransactionOrderList(@Path("API_KEY") apiKey: String?, @Path("transactions_header_id") transactionsHeadersId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<TransactionDetail?>?>?>?

    //endregion
    //region Upload Transaction
    @Headers("Content-Type: application/json")
    @POST("rest/transactionheaders/submit/api_key/{API_KEY}")
    fun uploadTransactionHeader(@Body items: TransactionHeaderUpload?, @Path("API_KEY") API_KEY: String?): Call<TransactionObject?>?

    //endregion
    //endregion
    //region Category
    //region Get category list
    @FormUrlEncoded
    @POST("rest/categories/search/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    fun getSearchCategory(@Path("API_KEY") apiKey: String?, @Path("limit") limit: String?,
                          @Path("offset") offset: String?, @Field("order_by") order_by: String?): LiveData<ApiResponse<List<Category?>?>?>?

    //endregion
    //endregion
    //region User Related
    //region GET User
    @GET("rest/users/get/api_key/{API_KEY}/user_id/{user_id}")
    fun getUser(@Path("API_KEY") apiKey: String?, @Path("user_id") userId: String?): LiveData<ApiResponse<List<User?>?>?>?

    //endregion
    //region POST Upload Image
    @Multipart
    @POST("rest/images/upload/api_key/{API_KEY}")
    fun doUploadImage(@Path("API_KEY") apiKey: String?, @Part("user_id") userId: RequestBody?, @Part("file") name: RequestBody?, @Part file: MultipartBody.Part?, @Part("platform_name") platformName: RequestBody?): LiveData<ApiResponse<User?>?>?

    //endregion
    //region POST User for Login
    @FormUrlEncoded
    @POST("rest/users/login/api_key/{API_KEY}")
    fun postUserLogin(@Path("API_KEY") apiKey: String?, @Field("user_email") userEmail: String?, @Field("user_password") userPassword: String?): LiveData<ApiResponse<User?>?>?

    //endregion
    //region POST User for Register
    @FormUrlEncoded
    @POST("rest/users/register/api_key/{API_KEY}")
    fun postFBUser(@Path("API_KEY") apiKey: String?, @Field("facebook_id") facebookId: String?, @Field("user_name") userName: String?, @Field("user_email") userEmail: String?, @Field("profile_photo_url") profilePhotoUrl: String?): Call<User?>?

    @FormUrlEncoded
    @POST("rest/users/add/api_key/{API_KEY}")
    fun postUser(@Path("API_KEY") apiKey: String?, @Field("user_id") userId: String?, @Field("user_name") userName: String?, @Field("user_email") userEmail: String?, @Field("user_password") userPassword: String?, @Field("user_phone") userPhone: String?, @Field("device_token") deviceToken: String?): Call<User?>?

    //endregion
    //region POST Forgot Password
    @FormUrlEncoded
    @POST("rest/users/reset/api_key/{API_KEY}")
    fun postForgotPassword(@Path("API_KEY") apiKey: String?, @Field("user_email") userEmail: String?): LiveData<ApiResponse<ApiStatus?>?>?

    //endregion
    //region PUT User for User Update
    @FormUrlEncoded
    @POST("rest/users/profile_update/api_key/{API_KEY}")
    fun putUser(@Path("API_KEY") apiKey: String?,
                @Field("user_id") loginUserId: String?,
                @Field("user_name") userName: String?,
                @Field("user_email") userEmail: String?,
                @Field("user_phone") userPhone: String?,
                @Field("user_about_me") userAboutMe: String?,
                @Field("billing_first_name") billingFirstName: String?,
                @Field("billing_last_name") billingLastName: String?,
                @Field("billing_company") billingCompany: String?,
                @Field("billing_address_1") billingAddress1: String?,
                @Field("billing_address_2") billingAddress2: String?,
                @Field("billing_country") billingCountry: String?,
                @Field("billing_state") billingState: String?,
                @Field("billing_city") billingCity: String?,
                @Field("billing_postal_code") billingPostalCode: String?,
                @Field("billing_email") billingEmail: String?,
                @Field("billing_phone") billingPhone: String?,
                @Field("shipping_first_name") shippingFirstName: String?,
                @Field("shipping_last_name") shippingLastName: String?,
                @Field("shipping_company") shippingCompany: String?,
                @Field("shipping_address_1") shippingAddress1: String?,
                @Field("shipping_address_2") shippingAddress2: String?,
                @Field("shipping_country") shippingCountry: String?,
                @Field("shipping_state") shippingState: String?,
                @Field("shipping_city") shippingCity: String?,
                @Field("shipping_postal_code") shippingPostalCode: String?,
                @Field("shipping_email") shippingEmail: String?,
                @Field("shipping_phone") shippingPhone: String?,
                @Field("country_id") countryId: String?,
                @Field("city_id") cityId: String?
    ): LiveData<ApiResponse<User?>?>?

    //endregion
    //region PUT for Password Update
    @FormUrlEncoded
    @POST("rest/users/password_update/api_key/{API_KEY}")
    fun postPasswordUpdate(@Path("API_KEY") apiKey: String?, @Field("user_id") loginUserId: String?, @Field("user_password") password: String?): LiveData<ApiResponse<ApiStatus?>?>?

    //endregion
    //endregion
    //region About Us
    @GET("rest/abouts/get/api_key/{API_KEY}")
    fun getAboutUs(@Path("API_KEY") apiKey: String?): LiveData<ApiResponse<List<AboutUs?>?>?>?

    //endregion
    //region Contact Us
    @FormUrlEncoded
    @POST("rest/contacts/add/api_key/{API_KEY}")
    fun rawPostContact(@Path("API_KEY") apiKey: String?, @Field("name") contactName: String?, @Field("email") contactEmail: String?, @Field("message") contactMessage: String?, @Field("phone") contactPhone: String?): Call<ApiStatus?>?

    //endregion
    //region GET SubCategory List
    @GET("rest/subcategories/get/api_key/{API_KEY}")
    fun getAllSubCategoryList(@Path("API_KEY") apiKey: String?): LiveData<ApiResponse<List<SubCategory?>?>?>?

    @GET("rest/subcategories/get/api_key/{API_KEY}/login_user_id/{login_user_id}/cat_id/{cat_id}/limit/{limit}/offset/{offset}")
    fun getSubCategoryListWithCatId(@Path("API_KEY") apiKey: String?, @Path("login_user_id") loginUserId: String?, @Path("cat_id") catId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<SubCategory?>?>?>?

    //endregion
    //country list
    @FormUrlEncoded
    @POST("rest/shipping_zones/get_shipping_country/api_key/{API_KEY}/shop_id/{shop_id}/limit/{limit}/offset/{offset}")
    fun getCountryListWithShopId(@Path("API_KEY") apiKey: String?, @Field("shop_id") shopId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<Country?>?>?>?

    //endregion
    //country list
    @Headers("Content-Type: application/json")
    @POST("rest/shipping_zones/get_shipping_cost/api_key/{API_KEY}")
    fun postShippingByCountryAndCity(@Path("API_KEY") apiKey: String?, @Body productUpload: ShippingCostContainer?): Call<ShippingCost?>?

    //endregion
    //city list
    @FormUrlEncoded
    @POST("rest/shipping_zones/get_shipping_city/api_key/{API_KEY}/shop_id/{shop_id}/country_id/{country_id}/limit/{limit}/offset/{offset}")
    fun getCityListWithCountryId(@Path("API_KEY") apiKey: String?, @Field("shop_id") shopId: String?, @Field("country_id") country_id: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<City?>?>?>?

    //endregion
    //region Delete item list by date
    @FormUrlEncoded
    @POST("rest/appinfo/get_delete_history/api_key/{API_KEY}")
    fun getDeletedHistory(
            @Path("API_KEY") apiKey: String?,
            @Field("start_date") start_date: String?,
            @Field("end_date") end_date: String?): Call<PSAppInfo?>?

    //endregion
    //region Get Shop by Id
    @GET("rest/shops/get_shop_info/api_key/{API_KEY}")
    fun getShopById(@Path("API_KEY") api_key: String?): LiveData<ApiResponse<Shop?>?>?

    //endregion
    //region Get All Rating List
    @GET("rest/rates/get/api_key/{API_KEY}/product_id/{product_id}/limit/{limit}/offset/{offset}")
    fun getAllRatingList(@Path("API_KEY") apiKey: String?, @Path("product_id") productId: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<Rating?>?>?>?

    //endregion
    //region Post Rating
    @FormUrlEncoded
    @POST("rest/rates/add_rating/api_key/{API_KEY}")
    fun postRating(
            @Path("API_KEY") api_key: String?,
            @Field("title") title: String?,
            @Field("description") description: String?,
            @Field("rating") rating: String?,
            @Field("user_id") userId: String?,
            @Field("product_id") productId: String?): Call<Rating?>?

    //endregion
    //endregion
    //region Touch Count
    @FormUrlEncoded
    @POST("rest/touches/add_touch/api_key/{API_KEY}")
    fun setrawPostTouchCount(
            @Path("API_KEY") apiKey: String?,
            @Field("type_id") typeId: String?,
            @Field("type_name") typeName: String?,
            @Field("user_id") userId: String?): Call<ApiStatus?>?

    //endregion
    //region News|Blog
    @GET("rest/feeds/get/api_key/{API_KEY}/limit/{limit}/offset/{offset}")
    fun getAllNewsFeed(@Path("API_KEY") api_key: String?, @Path("limit") limit: String?, @Path("offset") offset: String?): LiveData<ApiResponse<List<Blog?>?>?>?

    @GET("rest/feeds/get/api_key/{API_KEY}/id/{id}")
    fun getNewsById(@Path("API_KEY") api_key: String?, @Path("id") id: String?): LiveData<ApiResponse<Blog?>?>?

    //endregion
    //region Shipping Methods
    @GET("rest/shippings/get/api_key/{API_KEY}")
    fun getShipping(@Path("API_KEY") api_key: String?): LiveData<ApiResponse<List<ShippingMethod?>?>?>?

    //endregion
    //region Paypal
    @GET("rest/paypal/get_token/api_key/{API_KEY}")
    fun getPaypalToken(@Path("API_KEY") apiKey: String?): Call<ApiStatus?>?

    //endregion
    //region Check Coupon Discount
    @FormUrlEncoded
    @POST("rest/coupons/check/api_key/{API_KEY}/")
    fun checkCouponDiscount(@Path("API_KEY") apiKey: String?,
                            @Field("coupon_code") couponCode: String?): Call<CouponDiscount?>?

    //User Verify Email
    @FormUrlEncoded
    @POST("rest/users/verify/api_key/{API_KEY}")
    fun verifyEmail(
            @Path("API_KEY") API_KEY: String?,
            @Field("user_id") userId: String?,
            @Field("code") code: String?): Call<User?>?

    //Resend Verify Code again
    @FormUrlEncoded
    @POST("rest/users/request_code/api_key/{API_KEY}")
    fun resentCodeAgain(
            @Path("API_KEY") API_KEY: String?,
            @Field("user_email") user_email: String?
    ): Call<ApiStatus?>?

    @FormUrlEncoded
    @POST("rest/users/google_register/api_key/{API_KEY}")
    fun postGoogleLogin(
            @Path("API_KEY") API_KEY: String?,
            @Field("google_id") googleId: String?,
            @Field("user_name") userName: String?,
            @Field("user_email") userEmail: String?,
            @Field("profile_photo_url") profilePhotoUrl: String?,
            @Field("device_token") deviceToken: String?
    ): Call<User?>?

    @FormUrlEncoded
    @POST("rest/users/phone_register/api_key/{API_KEY}")
    fun postPhoneLogin(
            @Path("API_KEY") API_KEY: String?,
            @Field("phone_id") phoneId: String?,
            @Field("user_name") userName: String?,
            @Field("user_phone") userPhone: String?,
            @Field("device_token") deviceToken: String?
    ): Call<User?>? //endregion
}