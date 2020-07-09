package com.dewonderstruck.apps.ashx0.viewmodel.product;

import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.Product;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductRelatedViewModel extends PSViewModel {
    //for product detail list

    private final LiveData<Resource<List<Product>>> productRelatedData;
    private MutableLiveData<ProductRelatedViewModel.TmpDataHolder> productRelatedListObj = new MutableLiveData<>();

    //region Constructor

    @Inject
    ProductRelatedViewModel(ProductRepository productRepository) {
        //  product detail List
        productRelatedData = Transformations.switchMap(productRelatedListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ProductRelatedViewModel.");
            return productRepository.getRelatedList(Config.API_KEY, obj.productId, obj.catId);
        });

    }

    //endregion
    //region Getter And Setter for product detail List

    public void setProductRelatedListObj(String productId, String catId) {
        if (!isLoading) {
            ProductRelatedViewModel.TmpDataHolder tmpDataHolder = new ProductRelatedViewModel.TmpDataHolder();
            tmpDataHolder.productId = productId;
            tmpDataHolder.catId = catId;
            productRelatedListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Product>>> getProductRelatedData() {
        return productRelatedData;
    }

    //endregion

    //region Holder
    static class TmpDataHolder {
        public String offset = "";
        public String productId = "";
        public String catId = "";
        public String shopId = "";
        public Boolean isConnected = false;
    }
    //endregion
}
