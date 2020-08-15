package com.dewonderstruck.apps.ashx0.repository.product;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.dewonderstruck.apps.AppExecutors;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.ApiResponse;
import com.dewonderstruck.apps.ashx0.api.PSApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.db.ProductDao;
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource2;
import com.dewonderstruck.apps.ashx0.repository.common.DeRepository;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus;
import com.dewonderstruck.apps.ashx0.viewobject.FavouriteProduct;
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct;
import com.dewonderstruck.apps.ashx0.viewobject.Product;
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail;
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeHeader;
import com.dewonderstruck.apps.ashx0.viewobject.ProductColor;
import com.dewonderstruck.apps.ashx0.viewobject.ProductListByCatId;
import com.dewonderstruck.apps.ashx0.viewobject.ProductMap;
import com.dewonderstruck.apps.ashx0.viewobject.ProductSpecs;
import com.dewonderstruck.apps.ashx0.viewobject.RelatedProduct;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


@Singleton
public class ProductRepository extends DeRepository {


    //region Variables

    private final ProductDao productDao;
    private String isSelected;

    //endregion


    //region Constructor

    @Inject
    protected SharedPreferences pref;

    @Inject
    public ProductRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ProductDao productDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside ProductRepository");

        this.productDao = productDao;
    }

    //endregion

    //region Get Product List

    public LiveData<Resource<List<Product>>> getProductListByKey(ProductParameterHolder productParameterHolder, String loginUserId, String limit, String offset) {

        return new NetworkBoundResource2<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Product> itemList) {
                Utils.psLog("SaveCallResult of getProductListByKey.");

                try {

                    db.beginTransaction();

                    String mapKey = productParameterHolder.getKeyForProductMap();

                    db.productMapDao().deleteByMapKey(mapKey);

                    db.productDao().insertAll(itemList);

                    String dateTime = Utils.getDateTime();

                    for (int i = 0; i < itemList.size(); i++) {
                        db.productMapDao().insert(new ProductMap(mapKey + itemList.get(i).id, mapKey, itemList.get(i).id, i + 1, dateTime));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getProductListByKey.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                Utils.psLog("Load getProductListByKey From Db");

                String mapKey = productParameterHolder.getKeyForProductMap();

                if(!limit.equals(String.valueOf(Config.LOAD_FROM_DB))){
                    return productDao.getProductsByKey(mapKey);
                }else {
                    return productDao.getProductsByKeyByLimit(mapKey,limit);
                }

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                Utils.psLog("Call API Service to getProductListByKey.");

                Utils.psLog("holder.searchTerm : " + productParameterHolder.search_term);
                Utils.psLog("holder.catId : " + productParameterHolder.catId);
                Utils.psLog("holder.subCatId : " + productParameterHolder.subCatId);
                Utils.psLog("holder.isFeatured : " + productParameterHolder.isFeatured);
                Utils.psLog("holder.isDiscount : " + productParameterHolder.isDiscount);
                Utils.psLog("holder.isAvailable : " + productParameterHolder.isAvailable);
                Utils.psLog("holder.max_price : " + productParameterHolder.max_price);
                Utils.psLog("holder.min_price : " + productParameterHolder.min_price);
                Utils.psLog("holder.overallRating : " + productParameterHolder.overall_rating);
                Utils.psLog("holder.order_by : " + productParameterHolder.order_by);
                Utils.psLog("holder.order_type : " + productParameterHolder.order_type);

                return psApiService.searchProduct(Config.API_KEY, Utils.checkUserId(loginUserId), limit, offset, productParameterHolder.search_term, productParameterHolder.catId,
                        productParameterHolder.subCatId, productParameterHolder.isFeatured, productParameterHolder.isDiscount, productParameterHolder.isAvailable, productParameterHolder.max_price,
                        String.valueOf(productParameterHolder.min_price), productParameterHolder.overall_rating, productParameterHolder.order_by, productParameterHolder.order_type);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getProductListByKey) : " + message);
            }

        }.asLiveData();

    }

    public LiveData<Resource<Boolean>> getNextPageProductListByKey(ProductParameterHolder productParameterHolder, String loginUserId, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        prepareRatingValueForServer(productParameterHolder);

        LiveData<ApiResponse<List<Product>>> apiResponse = psApiService.searchProduct(Config.API_KEY, Utils.checkUserId(loginUserId), limit, offset, productParameterHolder.search_term, productParameterHolder.catId,
                productParameterHolder.subCatId, productParameterHolder.isFeatured, productParameterHolder.isDiscount, productParameterHolder.isAvailable, productParameterHolder.max_price,
                String.valueOf(productParameterHolder.min_price), productParameterHolder.overall_rating, productParameterHolder.order_by, productParameterHolder.order_type);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                if (response.body != null) {
                    appExecutors.diskIO().execute(() -> {

                        try {

                            db.beginTransaction();

                            db.productDao().insertAll(response.body);

                            int finalIndex = db.productMapDao().getMaxSortingByValue(productParameterHolder.getKeyForProductMap());

                            int startIndex = finalIndex + 1;

                            String mapKey = productParameterHolder.getKeyForProductMap();
                            String dateTime = Utils.getDateTime();

                            for (int i = 0; i < response.body.size(); i++) {
                                db.productMapDao().insert(new ProductMap(mapKey + response.body.get(i).id, mapKey, response.body.get(i).id, startIndex + i, dateTime));
                            }

                            db.setTransactionSuccessful();

                        } catch (NullPointerException ne) {
                            Utils.psErrorLog("Null Pointer Exception : ", ne);
                        } catch (Exception e) {
                            Utils.psErrorLog("Exception : ", e);
                        } finally {
                            db.endTransaction();
                        }

                        statusLiveData.postValue(Resource.success(true));
                    });
                } else {
                    statusLiveData.postValue(Resource.error(response.errorMessage, null));
                }

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;

    }

    //endregion


    //region Get Related Product

    public LiveData<Resource<List<Product>>> getRelatedList(String apiKey, String productId, String catId) {

        return new NetworkBoundResource2<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Product> itemList) {
                Utils.psLog("SaveCallResult of related products.");

                db.beginTransaction();

                try {

                    productDao.deleteAllRelatedProducts();

                    productDao.deleteAllBasedOnRelated();

                    productDao.insertAll(itemList);

                    for (Product item : itemList) {
                        productDao.insert(new RelatedProduct(item.id));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of related list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                Utils.psLog("Load related From Db");

                return productDao.getAllRelatedProducts();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                Utils.psLog("Call API Service to get related.");

                return psApiService.getProductDetailRelatedList(apiKey, productId, catId);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getRelated) : " + message);
            }

        }.asLiveData();
    }

    //endregion


    //region Get Favourite Product

    public LiveData<Resource<List<Product>>> getFavouriteList(String apiKey, String loginUserId, String offset) {

        return new NetworkBoundResource2<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Product> itemList) {
                Utils.psLog("SaveCallResult of related products.");

                db.beginTransaction();

                try {

                    productDao.deleteAllFavouriteProducts();

                    productDao.insertAll(itemList);

                    for (int i = 0; i < itemList.size(); i++) {
                        productDao.insertFavourite(new FavouriteProduct(itemList.get(i).id, i + 1));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of related list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                Utils.psLog("Load related From Db");

                return productDao.getAllFavouriteProducts();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                Utils.psLog("Call API Service to get related.");

                return psApiService.getFavouriteList(apiKey, Utils.checkUserId(loginUserId), String.valueOf(Config.PRODUCT_COUNT), offset);//////////////////////////////////////

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getRelated) : " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageFavouriteProductList(String loginUserId, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<Product>>> apiResponse = psApiService.getFavouriteList(Config.API_KEY, Utils.checkUserId(loginUserId),String.valueOf(Config.PRODUCT_COUNT), offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {

                            int lastIndex = db.productDao().getMaxSortingFavourite();
                            lastIndex = lastIndex + 1;

                            for (int i = 0; i < response.body.size(); i++) {
                                db.productDao().insertFavourite(new FavouriteProduct(response.body.get(i).id, lastIndex + i));
                            }

                            db.productDao().insertAll(response.body);
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;

    }

    //endregion


    //region Get Product List by CatId

    public LiveData<Resource<List<Product>>> getProductListByCatId(String apiKey, String loginUserId, String offset, String catId) {

        return new NetworkBoundResource2<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Product> itemList) {
                Utils.psLog("SaveCallResult of recent products.");

                db.beginTransaction();

                try {

                    productDao.deleteAllProductListByCatIdVOs();

                    productDao.insertAll(itemList);

                    for (Product item : itemList) {
                        productDao.insert(new ProductListByCatId(item.id, item.catId));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of product list by catId .", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                Utils.psLog("Load product From Db by catId");

                return productDao.getAllProductListByCatId(catId);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                Utils.psLog("Call API Service to get product list by catId.");

                return psApiService.getProductListByCatId(apiKey, Utils.checkUserId(loginUserId), String.valueOf(Config.PRODUCT_COUNT), offset, catId);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (get product list by catId) : " + message);
            }

        }.asLiveData();
    }

    // get next page Product List by catId
    public LiveData<Resource<Boolean>> getNextPageProductListByCatId(String loginUserId, String offset, String catId) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<Product>>> apiResponse = psApiService.getProductListByCatId(Config.API_KEY, Utils.checkUserId(loginUserId), String.valueOf(Config.PRODUCT_COUNT), offset, catId);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {

                            for (Product item : response.body) {
                                db.productDao().insert(new ProductListByCatId(item.id, item.catId));

                            }

                            db.productDao().insertAll(response.body);
                        }

                        db.setTransactionSuccessful();

                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;

    }

    //endregion


    //region Get Product detail

    public LiveData<Resource<Product>> getProductDetail(String apiKey, String productId, String historyFlag, String userId) {

        return new NetworkBoundResource2<Product, Product>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Product itemList) {
                Utils.psLog("SaveCallResult of recent products.");

                db.beginTransaction();

                try {

                    productDao.insert(itemList);

                    db.productColorDao().deleteProductColorById(productId);
                    db.productColorDao().insertAll(itemList.productColorList);

                    db.productSpecsDao().deleteProductSpecsById(productId);
                    db.productSpecsDao().insertAll(itemList.productSpecsList);

                    db.productAttributeHeaderDao().deleteProductAttributeHeaderById(productId);
                    db.productAttributeHeaderDao().insertAll(itemList.attributesHeaderList);

                    for (int i = 0; i < itemList.attributesHeaderList.size(); i++) {
                        db.productAttributeDetailDao().deleteProductAttributeDetailById(productId, itemList.attributesHeaderList.get(i).id);
                        db.productAttributeDetailDao().insertAll(itemList.attributesHeaderList.get(i).attributesDetailList);
                    }

                    if (historyFlag.equals(Constants.ONE)) {

                        db.historyDao().insert(new HistoryProduct(productId, itemList.getName(), itemList.defaultPhoto.imgPath, Utils.getDateTime()));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Product data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<Product> loadFromDb() {
                Utils.psLog("Load discount From Db");

                return productDao.getProductById(productId);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Product>> createCall() {
                Utils.psLog("Call API Service to get discount.");

                return psApiService.getProductDetail(apiKey, productId, Utils.checkUserId(userId));

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getDiscount) : " + message);
            }

        }.asLiveData();
    }

    //endregion


    //region Favourite post

    public LiveData<Resource<Boolean>> uploadFavouritePostToServer(String product_id, String userId) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                try {
                    db.beginTransaction();

                    isSelected = db.productDao().selectFavouriteById(product_id);
                    if (isSelected.equals(Constants.ONE)) {
                        db.productDao().updateProductForFavById(product_id, Constants.ZERO);
                    } else {
                        db.productDao().updateProductForFavById(product_id, Constants.ONE);
                    }


                    db.setTransactionSuccessful();
                } catch (NullPointerException ne) {
                    Utils.psErrorLog("Null Pointer Exception : ", ne);
                } catch (Exception e) {
                    Utils.psErrorLog("Exception : ", e);
                } finally {
                    db.endTransaction();
                }

                // Call the API Service
                Response<Product> response;

                response = psApiService.setPostFavourite(Config.API_KEY, product_id, userId).execute();

                // Wrap with APIResponse Class
                ApiResponse<Product> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            db.productDao().insert(apiResponse.body);

                            if (isSelected.equals(Constants.ONE)) {
                                db.productDao().deleteFavouriteProductByProductId(apiResponse.body.id);
                            }else {
                                int lastIndex = db.productDao().getMaxSortingFavourite();
                                lastIndex = lastIndex + 1;

                                db.productDao().insertFavourite(new FavouriteProduct(apiResponse.body.id, lastIndex ));
                            }
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(apiResponse.getNextPage() != null));

                } else {

                    try {
                        db.beginTransaction();

                        isSelected = db.productDao().selectFavouriteById(product_id);
                        if (isSelected.equals(Constants.ONE)) {
                            db.productDao().updateProductForFavById(product_id, Constants.ZERO);
                        } else {
                            db.productDao().updateProductForFavById(product_id, Constants.ONE);
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }
        });

        return statusLiveData;
    }

    //endregion


    //region Touch count post

    public LiveData<Resource<Boolean>> uploadTouchCountPostToServer(String userId, String typeId, String typeName) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<ApiStatus> response;

            try {
                response = psApiService.setrawPostTouchCount(Config.API_KEY, typeId, typeName, Utils.checkUserId(userId)).execute();

                ApiResponse<ApiStatus> apiResponse = new ApiResponse<>(response);

                if (apiResponse.isSuccessful()) {
                    statusLiveData.postValue(Resource.success(true));
                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }

        });

        return statusLiveData;
    }

    //endregion

    //region Get Product color

    public LiveData<List<ProductColor>> getProductColor(String productId) {
        return db.productColorDao().getProductColorById(productId);
    }

    //endregion


    //region Get Product specs

    public LiveData<List<ProductSpecs>> getProductSpecs(String productId) {
        return db.productSpecsDao().getProductSpecsById(productId);
    }

    //endregion


    //region Get Product attribute header

    public LiveData<List<ProductAttributeHeader>> getProductAttributeHeader(String productId) {

        MutableLiveData<List<ProductAttributeHeader>> hotelInfoGroupList = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> {
            List<ProductAttributeHeader> groupList = db.productAttributeHeaderDao().getProductAttributeHeaderAndDetailById(productId);
            appExecutors.mainThread().execute(() ->
                    hotelInfoGroupList.setValue(groupList)
            );
        });

        return hotelInfoGroupList;
    }

    //endregion


    //region Get Product attribute detail

    public LiveData<List<ProductAttributeDetail>> getProductAttributeDetail(String productId, String headerId) {
        return db.productAttributeDetailDao().getProductAttributeDetailById(productId, headerId);
    }

    //endregion


    //region Get history

    public LiveData<List<HistoryProduct>> getAllHistoryList(String offset) {

        return db.historyDao().getAllHistoryProductListData(offset);

    }

    //endregion


    //region Support Functions

    private void prepareRatingValueForServer(ProductParameterHolder productParameterHolder) {

        List<String> ratingValue = new ArrayList<>();
        if (!productParameterHolder.rating_value_one.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_one);
        }
        if (!productParameterHolder.rating_value_two.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_two);
        }
        if (!productParameterHolder.rating_value_three.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_three);
        }
        if (!productParameterHolder.rating_value_four.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_four);
        }
        if (!productParameterHolder.rating_value_five.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_five);
        }

        String selectedStars;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ratingValue.size(); i++) {
            if (i == ratingValue.size() - 1) {
                stringBuilder.append(ratingValue.get(i));
            } else {
                stringBuilder.append(ratingValue.get(i)).append(",");
            }
        }
        selectedStars = String.valueOf(stringBuilder);
        Utils.psLog(selectedStars + "selected rating stars");
        productParameterHolder.overall_rating = selectedStars;

    }

    //endregion

}
