package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductColorAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.ProductColor

class ProductColorAdapter(private val dataBindingComponent: DataBindingComponent, private val callback: ColorClickCallBack?) : DataBoundListAdapter<ProductColor?, ItemProductColorAdapterBinding?>() {
    private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null
    override fun createBinding(parent: ViewGroup?): ItemProductColorAdapterBinding {
        val binding: ItemProductColorAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_product_color_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val productColor = binding.productColor
            if (productColor != null && callback != null) {
                callback.onClick(productColor, productColor.id, productColor.colorValue)
            }
        }
        return binding
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemProductColorAdapterBinding?, item: ProductColor?) {
        binding!!.productColor = item
        val b = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        canvas.drawColor(Color.parseColor(item!!.colorValue))
        dataBindingComponent.fragmentBindingAdapters.bindCircleBitmap(binding.color1BgImageView, b)
        if (item!!.isColorSelect) {
            binding.color1ImageView.visibility = View.VISIBLE
        } else {
            binding.color1ImageView.visibility = View.GONE
        }
    }

    protected override fun areItemsTheSame(oldItem: ProductColor?, newItem: ProductColor?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.productId == newItem.productId && oldItem.isColorSelect == newItem.isColorSelect)
    }

    protected override fun areContentsTheSame(oldItem: ProductColor?, newItem: ProductColor?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.productId == newItem.productId && oldItem.isColorSelect == newItem.isColorSelect)
    }

    interface ColorClickCallBack {
        fun onClick(productColor: ProductColor?, selectedColorId: String?, selectedColorValue: String?)
    }

}