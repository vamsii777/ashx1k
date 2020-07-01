package com.dewonderstruck.apps.ashx0.repository.collection;

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
import com.dewonderstruck.apps.ashx0.db.ProductCollectionDao;
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource;
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.Product;
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollection;
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vamsi Madduluri on 10/27/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Singleton
public class ProductCollectionRepository extends PSRepository {


    //region Variables

    private final ProductCollectionDao productCollectionDao;

    //endregion


    //region Constructor

    @Inject
    ProductCollectionRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ProductCollectionDao productCollectionDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside ProductCollectionRepository");

        this.productCollectionDao = productCollectionDao;
    }

    //endregion


    //region ProductCollectionHeader Repository Functions for ViewModel

    public LiveData<Resource<List<ProductCollectionHeader>>> getProductionCollectionHeaderListForHome(String apiKey, String collectionLimit, String colProductLimit, String productLimit, String offset) {
        return new NetworkBoundResource<List<ProductCollectionHeader>, List<ProductCollectionHeader>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ProductCollectionHeader> itemList) {
                Utils.psLog("SaveCallResult of getProductionCollectionHeaderListForHome.");

                db.beginTransaction();

                try {

                    productCollectionDao.deleteAll();

                    productCollectionDao.insertAllCollectionHeader(itemList);

                    for (ProductCollectionHeader header : itemList) {

                        productCollectionDao.deleteAllBasedOnCollectionId(header.id);
                        if (header.productList != null) {
                            for (Product product : header.productList) {
                                productCollectionDao.insert(new ProductCollection(header.id, product.id));

                                db.productDao().insert(product);
                            }
                        }
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getProductionCollectionHeaderListForHome.", e);

                } finally {
                    db.endTransaction();
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<ProductCollectionHeader> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ProductCollectionHeader>> loadFromDb() {
                Utils.psLog("Load Featured ProductCollectionHeader From Db");

                //return productCollectionDao.getAll();
                MutableLiveData<List<ProductCollectionHeader>> productCollectionHeaderList = new MutableLiveData<>();
                appExecutors.diskIO().execute(() -> {
                    List<ProductCollectionHeader> groupList = productCollectionDao.getAllIncludingProductList(Integer.parseInt(collectionLimit), Integer.parseInt(colProductLimit));
                    appExecutors.mainThread().execute(() ->
                            productCollectionHeaderList.setValue(groupList)
                    );
                });

                return productCollectionHeaderList;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ProductCollectionHeader>>> createCall() {

                return psApiService.getProductCollectionHeader(apiKey, productLimit, offset);
            }
        }.asLiveData();
    }
    //endregion

    // get next page ProductionCollectionHeaderList

    public LiveData<Resource<List<ProductCollectionHeader>>> getProductionCollectionHeaderList(String apiKey, String productlimit,String offset) {
        return new NetworkBoundResource<List<ProductCollectionHeader>, List<ProductCollectionHeader>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ProductCollectionHeader> itemList) {
                Utils.psLog("SaveCallResult of getProductionCollectionHeaderListForHome.");

                db.beginTransaction();

                try {

                    productCollectionDao.deleteAll();

                    productCollectionDao.insertAllCollectionHeader(itemList);

                    for (ProductCollectionHeader header : itemList) {

                        productCollectionDao.deleteAllBasedOnCollectionId(header.id);
                        if (header.productList != null) {
                            for (Product product : header.productList) {
                                productCollectionDao.insert(new ProductCollection(header.id, product.id));

                                db.productDao().insert(product);
                            }
                        }
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getProductionCollectionHeaderListForHome.", e);

                } finally {
                    db.endTransaction();
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<ProductCollectionHeader> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ProductCollectionHeader>> loadFromDb() {
                Utils.psLog("Load Featured ProductCollectionHeader From Db");

                return productCollectionDao.getAll();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ProductCollectionHeader>>> createCall() {

                return psApiService.getProductCollectionHeader(apiKey, productlimit, offset);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageProductionCollectionHeaderList(String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<ProductCollectionHeader>>> apiResponse = psApiService.getProductCollectionHeader(Config.API_KEY, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            productCollectionDao.insertAllCollectionHeader(response.body);

                            for (ProductCollectionHeader header : response.body) {

                                productCollectionDao.deleteAllBasedOnCollectionId(header.id);
                                if (header.productList != null) {
                                    for (Product product : header.productList) {
                                        productCollectionDao.insert(new ProductCollection(header.id, product.id));
                                    }
                                }
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

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;
    }

    public LiveData<Resource<List<Product>>> getProductCollectionProducts(String apiKey, String limit, String offset, String id) {
        return new NetworkBoundResource<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Product> itemList) {
                Utils.psLog("SaveCallResult of getProductCollectionProducts.");

                db.beginTransaction();

                try {

                    productCollectionDao.deleteAllBasedOnCollectionId(id);

                    for (Product product : itemList) {

                        productCollectionDao.insert(new ProductCollection(id, product.id));

                        db.productDao().insert(product);
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getProductCollectionProducts.", e);

                } finally {
                    db.endTransaction();
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                Utils.psLog("Load Featured getProductCollectionProducts From Db");

                return productCollectionDao.getProductListByCollectionId(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {

                return psApiService.getCollectionProducts(apiKey, limit, offset, id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageProductCollectionProduct(String limit, String offset, String id) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<Product>>> apiResponse = psApiService.getCollectionProducts(Config.API_KEY, limit, offset, id);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            for (Product product : response.body) {

                                productCollectionDao.insert(new ProductCollection(id, product.id));

                                db.productDao().insert(product);

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

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;

    }


    //endregion
}
