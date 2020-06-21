package com.dewonderstruck.apps.ashx0.ui.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemTrendingCategoryAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Category

class TrendingCategoryAdapter(private val dataBindingComponent: DataBindingComponent,
                              private val callback: CategoryClickCallback?,
                              private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<Category?, ItemTrendingCategoryAdapterBinding?>() {
    private val lastPosition = -1
    override fun createBinding(parent: ViewGroup?): ItemTrendingCategoryAdapterBinding {
        val binding: ItemTrendingCategoryAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_trending_category_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val category = binding.category
            if (category != null && callback != null) {
                callback.onClick(category)
            }
        }
        return binding
    }

    // For general animation
    override fun bindView(holder: DataBoundViewHolder<ItemTrendingCategoryAdapterBinding>?, position: Int) {
        super.bindView(holder, position)

        //setAnimation(holder.itemView, position);
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemTrendingCategoryAdapterBinding?, item: Category) {
        binding!!.category = item
        binding.itemImageView.setOnClickListener { view: View? -> callback!!.onClick(item) }
    }

    protected override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return (Objects.equals(oldItem.id, newItem.id)
                && oldItem.name == newItem.name)
    }

    protected override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return (Objects.equals(oldItem.id, newItem.id)
                && oldItem.name == newItem.name)
    }

    interface CategoryClickCallback {
        fun onClick(category: Category?)
    }

}