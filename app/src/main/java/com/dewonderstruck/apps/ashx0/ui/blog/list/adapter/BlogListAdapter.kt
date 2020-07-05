package com.dewonderstruck.apps.ashx0.ui.blog.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemBlogListAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Blog

class BlogListAdapter(private val dataBindingComponent: DataBindingComponent,
                      private val callback: NewsClickCallback?,
                      private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<Blog?, ItemBlogListAdapterBinding?>() {
    private var lastPosition = -1
    override fun createBinding(parent: ViewGroup): ItemBlogListAdapterBinding {
        val binding: ItemBlogListAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_blog_list_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val blog = binding.blog
            if (blog != null && callback != null) {
                callback.onClick(blog)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemBlogListAdapterBinding?>, position: Int) {
        super.bindView(holder, position)
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemBlogListAdapterBinding?, blog: Blog?) {
        binding!!.blog = blog
    }

    protected override fun areItemsTheSame(oldItem: Blog?, newItem: Blog?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    protected override fun areContentsTheSame(oldItem: Blog?, newItem: Blog?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    interface NewsClickCallback {
        fun onClick(blog: Blog?)
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