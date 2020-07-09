package com.dewonderstruck.apps.ashx0.viewmodel.product;

import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail;
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductAttributeHeaderViewModel extends PSViewModel {
    //for product attribute detail list

    private final LiveData<List<ProductAttributeHeader>> ProductAttributeHeaderListData;
    private MutableLiveData<ProductAttributeHeaderViewModel.TmpDataHolder> ProductAttributeHeaderObj = new MutableLiveData<>();

    public String headerId;
    public boolean isHeaderData = false;
    public ProductAttributeDetail productAttributeDetail;
    public float price = 0;
    public float originalPrice = 0;

    public List<String> headerIdList = new ArrayList<>();

    public Map<String, String> basketItemHolderHashMap = new HashMap<>();
    public Map<String, Integer> attributeHeaderHashMap = new HashMap<>();
    public float priceAfterAttribute = 0;
    public float originalPriceAfterAttribute = 0;

    //endregion

    //region Constructor

    @Inject
    public ProductAttributeHeaderViewModel(ProductRepository productRepository) {
        //  product attribute detail List
        ProductAttributeHeaderListData = Transformations.switchMap(ProductAttributeHeaderObj, (ProductAttributeHeaderViewModel.TmpDataHolder obj) -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ProductAttributeHeaderListData");
            return productRepository.getProductAttributeHeader(obj.productId);
        });

    }

    //endregion
    //region Getter And Setter for product attribute detail List

    public void setProductAttributeHeaderListObj(String productId) {

        ProductAttributeHeaderViewModel.TmpDataHolder tmpDataHolder = new ProductAttributeHeaderViewModel.TmpDataHolder();
        tmpDataHolder.productId = productId;
        ProductAttributeHeaderObj.setValue(tmpDataHolder);

    }

    public LiveData<List<ProductAttributeHeader>> getProductAttributeHeaderListData() {
        return ProductAttributeHeaderListData;
    }

    //endregion

    //region Holder
    static class TmpDataHolder {
        public String offset = "";
        public String productId = "";
        public Boolean isConnected = false;
    }
    //endregion
}
