package com.dewonderstruck.apps.ashx0.ui.product.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemViewPagerAdapterBinding
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Product

class ViewPagerAdapter(private val dataBindingComponent: DataBindingComponent, private val callback: ItemClick) : PagerAdapter() {
    private var featuredProducts: List<Product>? = null
    fun replaceFeaturedList(featuredProductList: List<Product>?) {
        featuredProducts = featuredProductList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return if (featuredProducts != null && featuredProducts!!.size != 0) {
            featuredProducts!!.size
        } else {
            0
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ItemViewPagerAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(container.context), R.layout.item_view_pager_adapter, container, false, dataBindingComponent)
        binding.product = featuredProducts!![position]
        if (featuredProducts!![position].isDiscount == Constants.ONE) {
            changeVisibilityOfDiscountTextView(binding, View.VISIBLE)
            binding.oldDiscountPriceTextView.paintFlags = binding.oldDiscountPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            val discountValue = featuredProducts!![position].discountPercent.toInt()
            val discountValueStr = "-$discountValue%"
            binding.discountPercentTextView.text = discountValueStr
        } else {
            changeVisibilityOfDiscountTextView(binding, View.GONE)
        }
        if (featuredProducts!![position].isFeatured == Constants.ONE) {
            binding.featuredIconImageView.visibility = View.VISIBLE
        } else {
            binding.featuredIconImageView.visibility = View.GONE
        }
        binding.ratingBar.rating = featuredProducts!![position].ratingDetails.totalRatingValue
        binding.newDiscountPriceTextView.text = featuredProducts!![position].currencySymbol + " " + Utils.format(featuredProducts!![position].unitPrice.toDouble())
        binding.oldDiscountPriceTextView.text = featuredProducts!![position].currencySymbol + " " + Utils.format(featuredProducts!![position].originalPrice.toDouble())
        binding.ratingValueTextView.text = binding.root.resources.getString(R.string.discount__rating5, featuredProducts!![position].ratingDetails.totalRatingValue.toString(), featuredProducts!![position].ratingDetails.totalRatingCount.toString())
        binding.root.setOnClickListener { view: View? -> callback.onClick(featuredProducts!![position]) }
        container.addView(binding.root)
        return binding.root
    }

    interface ItemClick {
        fun onClick(product: Product?)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    private fun changeVisibilityOfDiscountTextView(binding: ItemViewPagerAdapterBinding, status: Int) {
        binding.discountPercentTextView.visibility = status
        binding.oldDiscountPriceTextView.visibility = status
    }

}