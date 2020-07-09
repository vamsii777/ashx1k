package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.SpinnerHeaderDetailBinding
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail

class ProductHeaderDetailAdapter internal constructor(context: Context, @LayoutRes resource: Int,
                                                      private val items: List<ProductAttributeDetail>, private val dataBindingComponent: DataBindingComponent) : ArrayAdapter<ProductAttributeDetail>(context, resource, 0, items) {
    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): ProductAttributeDetail {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    private fun createItemView(position: Int, parent: ViewGroup): View {
        val binding: SpinnerHeaderDetailBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.spinner_header_detail
                        , parent, false,
                        dataBindingComponent)
        bind(binding, getItem(position))
        return binding.root
    }

    protected fun bind(binding: SpinnerHeaderDetailBinding, item: ProductAttributeDetail?) {
        binding.headerDetailProduct = item
    }

    interface ProductHeaderDetailClickCallBack {
        fun onClick(productAttributeDetail: ProductAttributeDetail?)
    }

}