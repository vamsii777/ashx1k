package com.dewonderstruck.apps.ashx0.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemShippingMethodBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.ShippingMethod

class ShippingMethodsAdapter(private val dataBindingComponent: DataBindingComponent,
                             private val callback: NewsClickCallback, private val defaultShippingId: String, private val selectedShippingId: String) : DataBoundListAdapter<ShippingMethod?, ItemShippingMethodBinding?>() {
    private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface? = null
    private var oldItem: ItemShippingMethodBinding? = null
    private var newItem: ItemShippingMethodBinding? = null
    override fun createBinding(parent: ViewGroup?): ItemShippingMethodBinding {
        val binding = DataBindingUtil
                .inflate<ViewDataBinding>(LayoutInflater.from(parent!!.context),
                        R.layout.item_shipping_method, parent, false,
                        dataBindingComponent) as ItemShippingMethodBinding
        binding.root.setOnClickListener { v: View? ->
            callback.onClick(binding.shippingMethod)
            if (newItem != null && oldItem != null) {
                oldItem = newItem
                newItem = binding
                changeToWhite(oldItem)
                changeToOrange(newItem)
            } else if (oldItem != null) {
                newItem = binding
                changeToWhite(oldItem)
                changeToOrange(newItem)
            } else {
                newItem = binding
                oldItem = binding
                changeToOrange(newItem)
            }
        }
        return binding
    }

    // For general animation
    override fun bindView(holder: DataBoundViewHolder<ItemShippingMethodBinding?>?, position: Int) {
        super.bindView(holder, position)

        // setAnimation(holder.itemView, position);
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemShippingMethodBinding?, shippingMethod: ShippingMethod?) {
        binding!!.shippingMethod = shippingMethod
        if (shippingMethod!!.price * 10 % 10 == 0f) {
            binding.cashTextView.text = shippingMethod.currencySymbol + Math.round(shippingMethod.price)
        } else {
            binding.cashTextView.text = shippingMethod.currencySymbol + shippingMethod.price
        }
        if (!selectedShippingId.isEmpty()) {
            if (shippingMethod.id == selectedShippingId) {
                oldItem = binding
                changeToOrange(binding)
            } else {
                changeToWhite(binding)
            }
        } else {
            if (!defaultShippingId.isEmpty()) {
                if (shippingMethod.id == defaultShippingId) {
                    oldItem = binding
                    changeToOrange(binding)
                } else {
                    changeToWhite(binding)
                }
            }
        }
    }

    protected override fun areItemsTheSame(oldItem: ShippingMethod?, newItem: ShippingMethod?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    protected override fun areContentsTheSame(oldItem: ShippingMethod?, newItem: ShippingMethod?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.name == newItem.name)
    }

    interface NewsClickCallback {
        fun onClick(shippingMethod: ShippingMethod?)
    }

    private fun changeToWhite(binding: ItemShippingMethodBinding?) {
        binding!!.cashCardView.setCardBackgroundColor(binding.root.resources.getColor(R.color.md_white_1000))
        binding.cashTextView.setTextColor(binding.root.resources.getColor(R.color.global__primary))
        binding.typeTextView.setTextColor(binding.root.resources.getColor(R.color.md_grey_700))
        binding.daysTextView.setTextColor(binding.root.resources.getColor(R.color.md_black_1000))
        binding.textView19.setTextColor(binding.root.resources.getColor(R.color.md_black_1000))
    }

    private fun changeToOrange(binding: ItemShippingMethodBinding?) {
        binding!!.cashCardView.setCardBackgroundColor(binding.root.resources.getColor(R.color.global__primary))
        binding.cashTextView.setTextColor(binding.root.resources.getColor(R.color.md_white_1000))
        binding.typeTextView.setTextColor(binding.root.resources.getColor(R.color.md_white_1000))
        binding.daysTextView.setTextColor(binding.root.resources.getColor(R.color.md_white_1000))
        binding.textView19.setTextColor(binding.root.resources.getColor(R.color.md_white_1000))
    }

}