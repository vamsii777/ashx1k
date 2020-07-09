package com.dewonderstruck.apps.ashx0.ui.product.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemSearchCityBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.City

class SearchCityAdapter : DataBoundListAdapter<City?, ItemSearchCityBinding?> {
    private val dataBindingComponent: DataBindingComponent
    private val callback: NewsClickCallback?
    private var diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null
    private var lastPosition = -1
    var cityId: String? = ""

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?,
                diffUtilDispatchedInterface: DiffUtilDispatchedInterface2?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface
    }

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?, cityId: String?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.cityId = cityId
    }

    override fun createBinding(parent: ViewGroup?): ItemSearchCityBinding {
        val binding: ItemSearchCityBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_search_city, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val city = binding.city
            if (city != null && callback != null) {
                binding.groupview.setBackgroundColor(parent.resources.getColor(R.color.md_green_50))
                callback.onClick(city, city.id)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemSearchCityBinding?>?, position: Int) {
        super.bindView(holder, position)
    }

    override fun dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface!!.onDispatched()
        }
    }

    protected override fun bind(binding: ItemSearchCityBinding?, item: City?) {
        binding!!.city = item
        if (cityId != null) {
            if (item!!.id == cityId) {
                binding.groupview.setBackgroundColor(binding.groupview.resources.getColor(R.color.md_green_50))
            }
        }
    }

    protected override fun areItemsTheSame(oldItem: City?, newItem: City?): Boolean {
        return Objects.equals(oldItem!!.id, newItem!!.id)
    }

    protected override fun areContentsTheSame(oldItem: City?, newItem: City?): Boolean {
        return Objects.equals(oldItem!!.id, newItem!!.id)
    }

    interface NewsClickCallback {
        fun onClick(City: City?, id: String?)
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