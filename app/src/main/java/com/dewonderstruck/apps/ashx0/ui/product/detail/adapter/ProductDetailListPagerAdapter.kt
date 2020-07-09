package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductDetailListPagerAdapterBinding
import com.dewonderstruck.apps.ashx0.viewobject.Image

class ProductDetailListPagerAdapter(private val dataBindingComponent: DataBindingComponent,
                                    private val callback: ImageClickCallback?) : PagerAdapter() {
    private var images: List<Image>? = null
    override fun getCount(): Int {
        return if (images == null) {
            0
        } else {
            images!!.size
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ItemProductDetailListPagerAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(container.context),
                        R.layout.item_product_detail_list_pager_adapter, container, false,
                        dataBindingComponent)
        binding.img = images!![position]
        container.addView(binding.root)
        binding.root.setOnClickListener { view: View? ->
            val image = binding.img
            if (image != null && callback != null) {
                callback.onItemClick(view, image, position)
            }
        }
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

    fun setImageList(imageList: List<Image>?) {
        images = imageList
        notifyDataSetChanged()
    }

    interface ImageClickCallback {
        fun onItemClick(view: View?, obj: Image?, position: Int)
    }

}