package com.dewonderstruck.apps.ashx0.ui.product.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemSearchCategoryAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Category

class SearchCategoryAdapter : DataBoundListAdapter<Category?, ItemSearchCategoryAdapterBinding?> {
    private val dataBindingComponent: DataBindingComponent
    private val callback: NewsClickCallback?
    private var diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null
    private var lastPosition = -1
    var catId: String? = ""

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?,
                diffUtilDispatchedInterface: DiffUtilDispatchedInterface2?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface
    }

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?, catId: String?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.catId = catId
    }

    override fun createBinding(parent: ViewGroup?): ItemSearchCategoryAdapterBinding {
        val binding: ItemSearchCategoryAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_search_category_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val category = binding.category
            if (category != null && callback != null) {
                binding.groupview.setBackgroundColor(parent.resources.getColor(R.color.md_green_50))
                callback.onClick(category, category.id)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemSearchCategoryAdapterBinding?>?, position: Int) {
        super.bindView(holder, position)
    }

    override fun dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface!!.onDispatched()
        }
    }

    protected override fun bind(binding: ItemSearchCategoryAdapterBinding?, item: Category?) {
        binding!!.category = item
        if (catId != null) {
            if (item!!.id == catId) {
                binding.groupview.setBackgroundColor(binding.groupview.resources.getColor(R.color.md_green_50))
            }
        }
    }

    protected override fun areItemsTheSame(oldItem: Category?, newItem: Category?): Boolean {
        return Objects.equals(oldItem!!.id, newItem!!.id)
    }

    protected override fun areContentsTheSame(oldItem: Category?, newItem: Category?): Boolean {
        return Objects.equals(oldItem!!.id, newItem!!.id)
    }

    interface NewsClickCallback {
        fun onClick(category: Category?, id: String?)
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