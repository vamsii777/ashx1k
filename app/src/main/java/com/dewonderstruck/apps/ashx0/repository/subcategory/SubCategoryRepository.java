package com.dewonderstruck.apps.ashx0.repository.subcategory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.dewonderstruck.apps.AppExecutors;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.ApiResponse;
import com.dewonderstruck.apps.ashx0.api.PSApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.db.SubCategoryDao;
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource2;
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SubCategoryRepository extends PSRepository {

    private final SubCategoryDao subCategoryDao;

    @Inject
    SubCategoryRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, SubCategoryDao subCategoryDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside SubCategoryRepository");

        this.subCategoryDao = subCategoryDao;
    }

    public LiveData<Resource<List<SubCategory>>> getAllSubCategoryList(String apiKey) {
        return new NetworkBoundResource2<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of getAllSubCategoryList.");

                try {
                    db.beginTransaction();

                    subCategoryDao.deleteAllSubCategory();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId, item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllSubCategoryList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getAllSubCategory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return psApiService.getAllSubCategoryList(apiKey);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }


    public LiveData<Resource<List<SubCategory>>> getSubCategoriesWithCatId(String loginUserId, String offset, String catId) {
        return new NetworkBoundResource2<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of Sub Category.");

                try {
                    db.beginTransaction();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId,item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of recent product list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getSubCategoryList(catId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return psApiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, "", offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageSubCategoriesWithCatId(String loginUserId, String limit, String offset, String catId) {
        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<SubCategory>>> apiResponse = psApiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            for (SubCategory news : response.body) {
                                db.subCategoryDao().insert(new SubCategory(news.id, news.catId, news.name, news.status, news.ordering, news.addedDate, news.addedUserId, news.updatedDate, news.updatedUserId, news.updatedFlag, news.addedDateStr, news.defaultPhoto, news.defaultIcon));
                            }

                            db.subCategoryDao().insertAll(response.body);
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

}
