package com.dewonderstruck.apps.ashx0.viewmodel.common;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dewonderstruck.apps.ashx0.utils.Utils;

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class PSViewModel extends ViewModel{

    public Utils.LoadingDirection loadingDirection = Utils.LoadingDirection.none;
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    public int offset = 0;
    public int limit;

    public boolean forceEndLoading = false;
    public boolean isLoading = false;


    //region For loading status
    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    //endregion
}
