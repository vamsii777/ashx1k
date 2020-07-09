package com.dewonderstruck.apps.ashx0.ui.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemCategoryAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Category

class CategoryAdapter(private val dataBindingComponent: DataBindingComponent,
                      private val callback: CategoryClickCallback?,
                      private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface2?) : DataBoundListAdapter<Category?, ItemCategoryAdapterBinding?>() {
    private var lastPosition = -1
    override fun createBinding(parent: ViewGroup?): ItemCategoryAdapterBinding? {
        val binding: ItemCategoryAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_category_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val category = binding.category
            if (category != null && callback != null) {
                callback.onClick(category)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemCategoryAdapterBinding?>?, position: Int) {
        super.bindView(holder, position)
        setAnimation(holder!!.itemView, position)
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemCategoryAdapterBinding?, item: Category?) {
        binding!!.category = item
        binding.newsTitleTextView.text = item!!.name
    }

    protected override fun areItemsTheSame(oldItem: Category?, newItem: Category?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    protected override fun areContentsTheSame(oldItem: Category?, newItem: Category?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    interface CategoryClickCallback {
        fun onClick(category: Category?)
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