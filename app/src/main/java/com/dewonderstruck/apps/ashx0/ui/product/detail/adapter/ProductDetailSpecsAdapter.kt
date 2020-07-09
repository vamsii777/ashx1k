package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductDetailSpecsAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.ProductSpecs

class ProductDetailSpecsAdapter(private val dataBindingComponent: DataBindingComponent, private val callback: SpecsClickCallBack?) : DataBoundListAdapter<ProductSpecs?, ItemProductDetailSpecsAdapterBinding?>() {
    private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null
    override fun createBinding(parent: ViewGroup?): ItemProductDetailSpecsAdapterBinding {
        val binding: ItemProductDetailSpecsAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_product_detail_specs_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val productSpecs = binding.productspec
            if (productSpecs != null && callback != null) {
                callback.onClick(productSpecs)
            }
        }
        return binding
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemProductDetailSpecsAdapterBinding?, item: ProductSpecs?) {
        binding!!.productspec = item
    }

    protected override fun areItemsTheSame(oldItem: ProductSpecs?, newItem: ProductSpecs?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.productId == newItem.productId)
    }

    protected override fun areContentsTheSame(oldItem: ProductSpecs?, newItem: ProductSpecs?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.productId == newItem.productId)
    }

    interface SpecsClickCallBack {
        fun onClick(productSpecs: ProductSpecs?)
    }

}