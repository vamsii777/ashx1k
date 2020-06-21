package com.dewonderstruck.apps.ashx0.repository.transaction;

import com.dewonderstruck.apps.AppExecutors;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.ApiResponse;
import com.dewonderstruck.apps.ashx0.api.ApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.db.TransactionOrderDao;
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource;
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.TransactionDetail;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

@Singleton
public class TransactionOrderRepository extends PSRepository {

    //region variable
    private final TransactionOrderDao transactionOrderDao;
    //end region

    //region constructor
    @Inject
    TransactionOrderRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db, TransactionOrderDao transactionOrderDao) {
        super(apiService, appExecutors, db);
        this.transactionOrderDao = transactionOrderDao;
    }
    //end constructor

    //region start get transaction order list
    public LiveData<Resource<List<TransactionDetail>>> getTransactionOrderList(String apiKey, String transactionHeaderId, String offset) {

        return new NetworkBoundResource<List<TransactionDetail>, List<TransactionDetail>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<TransactionDetail> item) {
                Utils.psLog("SaveCallResult of recent transaction order.");

                db.beginTransaction();

                try {

                    transactionOrderDao.deleteAllTransactionOrderList(transactionHeaderId);

                    transactionOrderDao.insertAllTransactionOrderList(item);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction order of recent transaction order list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<TransactionDetail> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<TransactionDetail>> loadFromDb() {
                Utils.psLog("Load Recent transaction From Db");
                return transactionOrderDao.getAllTransactionOrderList(transactionHeaderId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<TransactionDetail>>> createCall() {
                return apiService.getTransactionOrderList(apiKey, transactionHeaderId, String.valueOf(Config.TRANSACTION_ORDER_COUNT), offset);
            }
        }.asLiveData();
    }
    //region end of transaction order list

    //region start next page transaction order list
    public LiveData<Resource<Boolean>> getNextPageTransactionOrderList(String transactionHeaderId, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<TransactionDetail>>> apiResponse = apiService.getTransactionOrderList(Config.API_KEY, transactionHeaderId, String.valueOf(Config.TRANSACTION_ORDER_COUNT), offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            db.transactionOrderDao().insertAllTransactionOrderList(response.body);
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
                statusLiveData.postValue(Resource.error(response.errorMessage, false));
            }

        });

        return statusLiveData;
    }
    //region end of next page transaction order list
}
