package com.dewonderstruck.apps.ashx0.repository.country;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.dewonderstruck.apps.AppExecutors;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.ApiResponse;
import com.dewonderstruck.apps.ashx0.api.PSApiService;
import com.dewonderstruck.apps.ashx0.db.CountryDao;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource2;
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.Country;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class CountryRepository extends PSRepository {


    //region Variables

    private final CountryDao countryDao;

    //endregion


    //region Constructor

    @Inject
    CountryRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, CountryDao countryDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside CategoryRepository");

        this.countryDao = countryDao;
    }

    //endregion


    //region Category Repository Functions for ViewModel
    public LiveData<Resource<List<Country>>> getCountryListWithShopId(String apiKey, String shopId, String limit, String offset) {
        return new NetworkBoundResource2<List<Country>, List<Country>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Country> itemList) {

                Utils.psLog("SaveCallResult of getAllSubCategoryList.");

                try {
                    db.beginTransaction();

                    countryDao.deleteCountry();

                    countryDao.insertAll(itemList);

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllSubCategoryList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Country> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<Country>> loadFromDb() {
                return countryDao.getAllCountry();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Country>>> createCall() {
                return psApiService.getCountryListWithShopId(apiKey, shopId, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageCityListWithShopId(String apiKey,String shopId, String limit, String offset) {
        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<Country>>> apiResponse = psApiService.getCountryListWithShopId(Config.API_KEY, shopId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            for (Country news : response.body) {
                                db.countryDao().insert(new Country(news.id, news.name, news.status, news.addedDate, news.addedUserId, news.updatedDate, news.updatedUserId, news.updatedFlag, news.addedDateStr));
                            }

                            db.countryDao().insertAll(response.body);
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
