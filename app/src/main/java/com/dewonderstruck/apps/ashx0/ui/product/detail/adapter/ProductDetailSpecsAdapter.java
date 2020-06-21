package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.databinding.ItemProductDetailSpecsAdapterBinding;
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter;
import com.dewonderstruck.apps.ashx0.utils.Objects;
import com.dewonderstruck.apps.ashx0.viewobject.ProductSpecs;

import androidx.databinding.DataBindingUtil;

public class ProductDetailSpecsAdapter extends DataBoundListAdapter<ProductSpecs, ItemProductDetailSpecsAdapterBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private SpecsClickCallBack callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;


    public ProductDetailSpecsAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, SpecsClickCallBack specsClickCallBack) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = specsClickCallBack;
    }

    @Override
    protected ItemProductDetailSpecsAdapterBinding createBinding(ViewGroup parent) {
        ItemProductDetailSpecsAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_product_detail_specs_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ProductSpecs productSpecs = binding.getProductspec();
            if (productSpecs != null && callback != null) {
                callback.onClick(productSpecs);
            }
        });
        return binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemProductDetailSpecsAdapterBinding binding, ProductSpecs item) {
        binding.setProductspec(item);
    }

    @Override
    protected boolean areItemsTheSame(ProductSpecs oldItem, ProductSpecs newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId);
    }

    @Override
    protected boolean areContentsTheSame(ProductSpecs oldItem, ProductSpecs newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId);
    }

    public interface SpecsClickCallBack {
        void onClick(ProductSpecs productSpecs);
    }
}
