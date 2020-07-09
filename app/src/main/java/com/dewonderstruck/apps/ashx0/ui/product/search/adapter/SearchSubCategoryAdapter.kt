package com.dewonderstruck.apps.ashx0.ui.product.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemSearchSubCategoryAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory

class SearchSubCategoryAdapter : DataBoundListAdapter<SubCategory?, ItemSearchSubCategoryAdapterBinding?> {
    private val dataBindingComponent: DataBindingComponent
    private val callback: NewsClickCallback?
    private var diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null
    private var lastPosition = -1
    private var subCatId: String? = null

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?,
                diffUtilDispatchedInterface: DiffUtilDispatchedInterface2?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface
    }

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?, subCatId: String?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.subCatId = subCatId
    }

    override fun createBinding(parent: ViewGroup?): ItemSearchSubCategoryAdapterBinding? {
        val binding: ItemSearchSubCategoryAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_search_sub_category_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val subCategory = binding.subCategory
            if (subCategory != null && callback != null) {
                callback.onClick(subCategory)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemSearchSubCategoryAdapterBinding?>?, position: Int) {
        super.bindView(holder, position)
    }

    override fun dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface!!.onDispatched()
        }
    }

    protected override fun bind(binding: ItemSearchSubCategoryAdapterBinding?, item: SubCategory?) {
        binding!!.subCategory = item
        if (subCatId != null) {
            if (item!!.id == subCatId) {
                binding.groupview.setBackgroundColor(binding.groupview.resources.getColor(R.color.md_green_50))
            }
        }
    }

    protected override fun areItemsTheSame(oldItem: SubCategory?, newItem: SubCategory?): Boolean {
        return Objects.equals(oldItem!!.id, newItem!!.id)
    }

    protected override fun areContentsTheSame(oldItem: SubCategory?, newItem: SubCategory?): Boolean {
        return Objects.equals(oldItem!!!!.id, newItem!!.id)
    }

    interface NewsClickCallback {
        fun onClick(subCategory: SubCategory?)
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