package com.dewonderstruck.apps.ashx0.ui.product.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductHorizontalListAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.like.LikeButton

class ProductHorizontalListAdapter : DataBoundListAdapter<Product?, ItemProductHorizontalListAdapterBinding?> {
    private val dataBindingComponent: DataBindingComponent
    private val callback: NewsClickCallback?
    private var diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?,
                diffUtilDispatchedInterface: DiffUtilDispatchedInterface2?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface
    }

    constructor(dataBindingComponent: DataBindingComponent,
                callback: NewsClickCallback?) {
        this.dataBindingComponent = dataBindingComponent
        this.callback = callback
    }

    override fun createBinding(parent: ViewGroup?): ItemProductHorizontalListAdapterBinding? {
        val binding: ItemProductHorizontalListAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_product_horizontal_list_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val product = binding.product
            if (product != null && callback != null) {
                callback.onClick(product)
            }
        }

//        binding.heartButton.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//
//                Product product = binding.getProduct();
//                if (product != null && callback != null) {
//                    callback.onFavLikeClick(product, binding.heartButton);
//                }
//
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//
//                Product product = binding.getProduct();
//                if (product != null && callback != null) {
//                    callback.onFavUnlikeClick(product, binding.heartButton);
//                }
//            }
//        });
        return binding
    }

    // For general animation
    override fun bindView(holder: DataBoundViewHolder<ItemProductHorizontalListAdapterBinding?>?, position: Int) {
        super.bindView(holder, position)

        // setAnimation(holder.itemView, position);
    }

    override fun dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface!!.onDispatched()
        }
    }

    protected override fun bind(binding: ItemProductHorizontalListAdapterBinding?, product: Product?) {
        binding!!.product = product
        binding.priceTextView.text = Utils.format(product!!.unitPrice.toDouble()).toString()
        val originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice.toDouble())
        binding.originalPriceTextView.text = originalPriceStr
        binding.ratingBar.rating = product.ratingDetails.totalRatingValue
        binding.ratingBarTextView.text = binding.root.resources.getString(R.string.discount__rating5, product.ratingDetails.totalRatingValue.toString(), product.ratingDetails.totalRatingCount.toString())
        if (product.isDiscount == Constants.ZERO) {
            binding.originalPriceTextView.visibility = View.GONE
            binding.discountTextView.visibility = View.GONE
        } else {
            binding.originalPriceTextView.paintFlags = binding.originalPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.originalPriceTextView.visibility = View.VISIBLE
            binding.discountTextView.visibility = View.VISIBLE
            val discountValue = product.discountPercent.toInt()
            val discountValueStr = "-$discountValue%"
            binding.discountTextView.text = discountValueStr
        }
        if (product.isFeatured == Constants.ZERO) {
            binding.featuredIconImageView.visibility = View.GONE
        } else {
            binding.featuredIconImageView.visibility = View.VISIBLE
        }

//        if (product.isFavourited.equals(Constants.RATING_ONE)) {
//            binding.heartButton.setLiked(true);
//        } else {
//            binding.heartButton.setLiked(false);
//        }
    }

    protected override fun areItemsTheSame(oldItem: Product?, newItem: Product?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.isFavourited == newItem.isFavourited && oldItem.name == newItem.name && oldItem.ratingDetails.totalRatingValue == newItem.ratingDetails.totalRatingValue && oldItem.ratingDetails.totalRatingCount == newItem.ratingDetails.totalRatingCount)
    }

    protected override fun areContentsTheSame(oldItem: Product?, newItem: Product?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.isFavourited == newItem.isFavourited && oldItem.name == newItem.name && oldItem.ratingDetails.totalRatingValue == newItem.ratingDetails.totalRatingValue && oldItem.ratingDetails.totalRatingCount == newItem.ratingDetails.totalRatingCount)
    }

    interface NewsClickCallback {
        fun onClick(product: Product?)
        fun onFavLikeClick(product: Product?, likeButton: LikeButton?)
        fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?)
    }
}