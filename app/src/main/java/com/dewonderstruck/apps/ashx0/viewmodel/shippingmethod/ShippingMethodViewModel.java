package com.dewonderstruck.apps.ashx0.viewmodel.shippingmethod;

import com.dewonderstruck.apps.ashx0.repository.shippingmethod.ShippingMethodRepository;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.ShippingMethod;
import com.dewonderstruck.apps.ashx0.viewobject.ShippingCostContainer;
import com.dewonderstruck.apps.ashx0.viewobject.ShippingCost;
import com.dewonderstruck.apps.ashx0.viewobject.ShippingProductContainer;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ShippingMethodViewModel extends PSViewModel {

    private final LiveData<Resource<List<ShippingMethod>>> shippingMethodsData;
    private MutableLiveData<Boolean> shippingMethodsObj = new MutableLiveData<>();

    public String shippingCostByZone;
    public List<ShippingProductContainer> shippingProductContainer = new ArrayList<>();

    private final LiveData<Resource<ShippingCost>> shippingCostByCountryAndCityData;
    private MutableLiveData<ShippingMethodViewModel.TmpDataHolder> shippingCostByCountryAndCityDataObj = new MutableLiveData<>();

    @Inject
    ShippingMethodViewModel(ShippingMethodRepository repository) {

        shippingMethodsData = Transformations.switchMap(shippingMethodsObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getAllShippingMethods();

        });

        shippingCostByCountryAndCityData = Transformations.switchMap(shippingCostByCountryAndCityDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.postShippingByCountryAndCity(obj.shippingCostContainer);
        });

    }

    public void setShippingMethodsObj() {

        this.shippingMethodsObj.setValue(true);
    }

    public LiveData<Resource<List<ShippingMethod>>> getShippingMethodsData() {
        return shippingMethodsData;
    }

    //get shipping cost by zone
    public void setshippingCostByCountryAndCityObj(ShippingCostContainer shippingCostContainer) {

        ShippingMethodViewModel.TmpDataHolder tmpDataHolder = new ShippingMethodViewModel.TmpDataHolder();
        tmpDataHolder.shippingCostContainer = shippingCostContainer;
        shippingCostByCountryAndCityDataObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<ShippingCost>> getshippingCostByCountryAndCityData() {
        return shippingCostByCountryAndCityData;
    }

    class TmpDataHolder {
        public ShippingCostContainer shippingCostContainer;

    }

}

