package com.dewonderstruck.apps.ashx0.viewmodel.shop;

import com.dewonderstruck.apps.ashx0.repository.shop.ShopRepository;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.Shop;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by Vamsi Madduluri on 3/19/19.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class ShopViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<Shop>> shopData;
    private MutableLiveData<ShopProfileTmpDataHolder> shopObj = new MutableLiveData<>();
    public String selectedShopId;
    public String flag;
    public String stripePublishableKey;

    //endregion


    //region Constructors

    @Inject
    ShopViewModel(ShopRepository repository) {

        shopData = Transformations.switchMap(shopObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShop(obj.apiKey);

        });
        
    }

    //endregion

    //region Shop Detail

    public void setShopObj(String apiKey ) {
        ShopProfileTmpDataHolder tmpDataHolder = new ShopProfileTmpDataHolder(apiKey);

        this.shopObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Shop>> getShopData() {
        return shopData;
    }

    //endregion

    //region Holders
    static class ShopProfileTmpDataHolder {
        String apiKey;

        ShopProfileTmpDataHolder(String apiKey) {
            this.apiKey = apiKey;
        }
    }
    //endregion


}
