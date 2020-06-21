package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductDetailTagAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.holder.TabObject

class ProductDetailTagAdapter(private val dataBindingComponent: DataBindingComponent, private val callback: SimilarProductClickCallBack?) : DataBoundListAdapter<TabObject?, ItemProductDetailTagAdapterBinding?>() {
    private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface? = null
    override fun createBinding(parent: ViewGroup?): ItemProductDetailTagAdapterBinding {
        val binding: ItemProductDetailTagAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_product_detail_tag_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val tabObject = binding.productTab
            if (tabObject != null && callback != null) {
                callback.onClick(tabObject, tabObject.tag_id)
            }
        }
        return binding
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemProductDetailTagAdapterBinding?, item: TabObject) {
        binding!!.productTab = item
    }

    protected override fun areItemsTheSame(oldItem: TabObject, newItem: TabObject): Boolean {
        return (Objects.equals(oldItem.tag_id, newItem.tag_id)
                && oldItem.tag_name == newItem.tag_name)
    }

    protected override fun areContentsTheSame(oldItem: TabObject, newItem: TabObject): Boolean {
        return (Objects.equals(oldItem.tag_id, newItem.tag_id)
                && oldItem.tag_name == newItem.tag_name)
    }

    interface SimilarProductClickCallBack {
        fun onClick(tabObject: TabObject?, selectedTabId: String?)
    }

}