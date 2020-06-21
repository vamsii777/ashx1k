package com.dewonderstruck.apps.ashx0.ui.product.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemDiscountListAdapterBinding
import com.dewonderstruck.apps.ashx0.databinding.ItemDiscountListAdapterViewAllBinding
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.like.LikeButton

class DiscountListAdapter(private val dataBindingComponent: DataBindingComponent, private val callback: NewsClickCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var discountList: List<Product>? = null
    fun replaceDiscount(discountList: List<Product>?) {
        this.discountList = discountList
        notifyDataSetChanged()
    }

    inner class MyViewAllHolder(val viewAllBinding: ItemDiscountListAdapterViewAllBinding) : RecyclerView.ViewHolder(viewAllBinding.root)

    inner class MyViewHolder(val binding: ItemDiscountListAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product?) {
            binding.product = product
            binding.executePendingBindings()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding: ItemDiscountListAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_discount_list_adapter, parent, false, dataBindingComponent)
            binding.originalPriceTextView.paintFlags = binding.originalPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            MyViewHolder(binding)
        } else {
            val viewAllBinding: ItemDiscountListAdapterViewAllBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_discount_list_adapter_view_all, parent, false, dataBindingComponent)
            MyViewAllHolder(viewAllBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewAllHolder) {
            holder.itemView.setOnClickListener { view: View? -> callback.onViewAllClick() }
        } else if (holder is MyViewHolder) {
            if (discountList != null && discountList!!.size > 0) {
                val product = discountList!![position - 1]
                holder.binding.ratingBar.rating = product.ratingDetails.totalRatingValue
                holder.binding.ratingBarTextView.text = holder.binding.root.resources.getString(R.string.discount__rating5, product.ratingDetails.totalRatingValue.toString(), product.ratingDetails.totalRatingCount.toString())
                holder.binding.priceTextView.text = Utils.format(product.unitPrice.toDouble())
                val originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice.toDouble())
                holder.binding.originalPriceTextView.text = originalPriceStr
                if (product.isFeatured == Constants.ZERO) {
                    holder.binding.featuredIconImageView.visibility = View.GONE
                } else {
                    holder.binding.featuredIconImageView.visibility = View.VISIBLE
                }
                if (product.isDiscount == Constants.ZERO) {
                    holder.binding.originalPriceTextView.visibility = View.GONE
                    holder.binding.discountTextView.visibility = View.GONE
                } else {
                    holder.binding.product = product
                    holder.binding.originalPriceTextView.visibility = View.VISIBLE
                    holder.binding.discountTextView.visibility = View.VISIBLE
                    val discountValue = product.discountPercent.toInt()
                    val discountValueStr = "-$discountValue%"
                    holder.binding.discountTextView.text = discountValueStr

//                    if (product.isFavourited.equals(Constants.RATING_ONE)) {
//                        ((MyViewHolder) holder).binding.heartButton.setLiked(true);
//                    } else {
//                        ((MyViewHolder) holder).binding.heartButton.setLiked(false);
//                    }
                }
                holder.itemView.setOnClickListener { view: View? -> callback.onClick(product) }

//                ((MyViewHolder) holder).binding.heartButton.setOnLikeListener(new OnLikeListener() {
//                    @Override
//                    public void liked(LikeButton likeButton) {
//
//                        Product product = ((MyViewHolder) holder).binding.getProduct();
//                        if (product != null && callback != null) {
//                            callback.onFavLikeClick(product, ((MyViewHolder) holder).binding.heartButton);
//                        }
//
//                    }
//
//                    @Override
//                    public void unLiked(LikeButton likeButton) {
//
//                        Product product = ((MyViewHolder) holder).binding.getProduct();
//                        if (product != null && callback != null) {
//                            callback.onFavUnlikeClick(product, ((MyViewHolder) holder).binding.heartButton);
//                        }
//                    }
//                });
            }
        }
    }

    interface NewsClickCallback {
        fun onClick(product: Product?)
        fun onViewAllClick()
        fun onFavLikeClick(product: Product?, likeButton: LikeButton?)
        fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?)
    }

    override fun getItemCount(): Int {
        return if (discountList != null && discountList!!.size > 0) {
            discountList!!.size + 1
        } else {
            0
        }
    }

}