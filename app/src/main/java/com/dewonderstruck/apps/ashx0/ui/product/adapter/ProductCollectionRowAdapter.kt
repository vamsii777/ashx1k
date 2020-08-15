package com.dewonderstruck.apps.ashx0.ui.product.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemProductCollectionRowAdapterBinding
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader
import com.like.LikeButton

class ProductCollectionRowAdapter(private val dataBindingComponent: DataBindingComponent, var callback: NewsClickCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var productCollectionHeaderList: List<ProductCollectionHeader>? = null
    fun replaceCollectionHeader(productCollectionHeaderList: List<ProductCollectionHeader>?) {
        this.productCollectionHeaderList = productCollectionHeaderList
        notifyDataSetChanged()
    }

    inner class MyViewHolder internal constructor(val binding: ItemProductCollectionRowAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productCollectionHeader: ProductCollectionHeader?) {
            binding.productCollection = productCollectionHeader
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemProductCollectionRowAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_product_collection_row_adapter, parent, false, dataBindingComponent)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val productCollectionHeader = productCollectionHeaderList!![position]
            holder.binding.titleTextView.text = productCollectionHeader.name
            holder.binding.productCollection = productCollectionHeader
            holder.binding.viewAllTextView.setOnClickListener { view: View? -> callback.onViewAllClick(productCollectionHeaderList!![position]) }
            if (productCollectionHeader.productList!!.size != 0) {
                val homeScreenAdapter = ProductHorizontalListAdapter(dataBindingComponent, object : ProductHorizontalListAdapter.NewsClickCallback {
                    override fun onClick(product: Product?) {
                        callback.onClick(product)
                    }

                    override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                        callback.onFavLikeClick(product, likeButton)
                    }

                    override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                        callback.onFavUnlikeClick(product, likeButton)
                    }
                })
                holder.binding.collectionList.adapter = homeScreenAdapter
                homeScreenAdapter.replace(productCollectionHeaderList!![position].productList)
            }
        }
    }

    interface NewsClickCallback {
        fun onClick(product: Product?)
        fun onViewAllClick(productCollectionHeader: ProductCollectionHeader?)
        fun onFavLikeClick(product: Product?, likeButton: LikeButton?)
        fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?)
    }

    override fun getItemCount(): Int {
        return if (productCollectionHeaderList != null && productCollectionHeaderList!!.size > 0) {
            productCollectionHeaderList!!.size
        } else {
            0
        }
    }

}