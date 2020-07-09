package com.dewonderstruck.apps.ashx0.repository.paypal;

import com.dewonderstruck.apps.AppExecutors;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.api.ApiResponse;
import com.dewonderstruck.apps.ashx0.api.PSApiService;
import com.dewonderstruck.apps.ashx0.db.PSCoreDb;
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.io.IOException;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Response;

public class PaypalRepository extends PSRepository {


    @Inject
    PaypalRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside PaypalRepository");
    }

    public LiveData<Resource<Boolean>> getPaypalToekn() {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<ApiStatus> response;

            try {
                response = psApiService.getPaypalToken(Config.API_KEY).execute();

                ApiResponse<ApiStatus> apiResponse = new ApiResponse<>(response);

                if (apiResponse.isSuccessful()) {
                    if(apiResponse.body != null) {
                        statusLiveData.postValue(Resource.successWithMsg(apiResponse.body.message, true));
                    }else {
                        statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                    }
                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }

        });

        return statusLiveData;
    }
}
