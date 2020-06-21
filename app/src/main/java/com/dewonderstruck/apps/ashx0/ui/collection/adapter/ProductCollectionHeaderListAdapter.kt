package com.dewonderstruck.apps.ashx0.ui.collection.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductCollectionHeaderListAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader

/**
 * Created by Vamsi Madduluri on 10/27/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class ProductCollectionHeaderListAdapter(private val dataBindingComponent: DataBindingComponent,
                                         private val callback: ProductCollectionHeaderClickCallback?,
                                         private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<ProductCollectionHeader?, ItemProductCollectionHeaderListAdapterBinding?>() {
    private var lastPosition = -1
    override fun createBinding(parent: ViewGroup): ItemProductCollectionHeaderListAdapterBinding {
        val binding: ItemProductCollectionHeaderListAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_product_collection_header_list_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val productCollectionHeader = binding.productCollectionHeader
            if (productCollectionHeader != null && callback != null) {
                callback.onClick(productCollectionHeader)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemProductCollectionHeaderListAdapterBinding?>?, position: Int) {
        super.bindView(holder, position)
        setAnimation(holder!!.itemView, position)
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemProductCollectionHeaderListAdapterBinding?, item: ProductCollectionHeader?) {
        binding!!.productCollectionHeader = item
        binding.newsTitleTextView.text = item!!.name
    }

    protected override fun areItemsTheSame(oldItem: ProductCollectionHeader?, newItem: ProductCollectionHeader?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    protected override fun areContentsTheSame(oldItem: ProductCollectionHeader?, newItem: ProductCollectionHeader?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    interface ProductCollectionHeaderClickCallback {
        fun onClick(productCollectionHeader: ProductCollectionHeader?)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        lastPosition = if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_bottom)
            viewToAnimate.startAnimation(animation)
            position
        } else {
            position
        }
    }

}