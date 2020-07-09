package com.dewonderstruck.apps.ashx0.ui.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemHomeCategoryIconListBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Category

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class CategoryIconListAdapter(private val dataBindingComponent: DataBindingComponent,
                              private val callback: CategoryClickCallback?,
                              private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface2?) : DataBoundListAdapter<Category?, ItemHomeCategoryIconListBinding?>() {
    private var lastPosition = -1
    override fun createBinding(parent: ViewGroup?): ItemHomeCategoryIconListBinding? {
        val binding: ItemHomeCategoryIconListBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_home_category_icon_list, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val category = binding.cat
            if (category != null && callback != null) {
                callback.onClick(category)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemHomeCategoryIconListBinding?>?, position: Int) {
        super.bindView(holder, position)

        //setAnimation(holder.itemView, position);
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemHomeCategoryIconListBinding?, item: Category?) {
        binding!!.cat = item
    }

    protected override fun areItemsTheSame(oldItem: Category?, newItem: Category?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    protected override fun areContentsTheSame(oldItem: Category?, newItem: Category???): Boolean {
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