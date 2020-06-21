package com.dewonderstruck.apps.ashx0.ui.product.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dewonderstruck.apps.ashx0.R;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import com.like.LikeButton;

import com.dewonderstruck.apps.ashx0.databinding.ItemProductVerticalListAdapterBinding;
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter;
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.Objects;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewobject.Product;

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class ProductVerticalListAdapter extends DataBoundListAdapter<Product, ItemProductVerticalListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ProductVerticalListAdapter(DataBindingComponent dataBindingComponent,
                                      NewsClickCallback callback, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemProductVerticalListAdapterBinding createBinding(ViewGroup parent) {
        ItemProductVerticalListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_product_vertical_list_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Product product = binding.getProduct();
            if (product != null && callback != null) {
                callback.onClick(product);
            }
        });

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

        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemProductVerticalListAdapterBinding> holder, int position) {
        super.bindView(holder, position);


        //setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemProductVerticalListAdapterBinding binding, Product product) {
        binding.setProduct(product);

        binding.ratingBar.setRating(product.ratingDetails.totalRatingValue);

        binding.ratingBarTextView.setText((binding.getRoot().getResources().getString(R.string.discount__rating5, String.valueOf(product.ratingDetails.totalRatingValue), String.valueOf(product.ratingDetails.totalRatingCount))));

        binding.priceTextView.setText(String.valueOf(Utils.format(product.unitPrice)));
        String originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice);
        binding.originalPriceTextView.setText(originalPriceStr);

        if (product.isDiscount.equals(Constants.ZERO)) {
            binding.originalPriceTextView.setVisibility(View.GONE);
            binding.discountTextView.setVisibility(View.GONE);
        } else {
            binding.originalPriceTextView.setPaintFlags(binding.originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.originalPriceTextView.setVisibility(View.VISIBLE);
            binding.discountTextView.setVisibility(View.VISIBLE);
            int discountValue = (int) product.discountPercent;
            String discountValueStr = "-" + discountValue + "%";
            binding.discountTextView.setText(discountValueStr);
        }

        if (product.isFeatured.equals(Constants.ZERO)) {
            binding.featuredIconImageView.setVisibility(View.GONE);
        } else {
            binding.featuredIconImageView.setVisibility(View.VISIBLE);
        }

//        if (product.isFavourited.equals(Constants.RATING_ONE)) {
//            binding.heartButton.setLiked(true);
//        } else {
//            binding.heartButton.setLiked(false);
//        }
    }

    @Override
    protected boolean areItemsTheSame(Product oldItem, Product newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.likeCount == newItem.likeCount
                && oldItem.ratingDetails.totalRatingValue == newItem.ratingDetails.totalRatingValue
                && oldItem.ratingDetails.totalRatingCount == newItem.ratingDetails.totalRatingCount;
    }

    @Override
    protected boolean areContentsTheSame(Product oldItem, Product newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.likeCount == newItem.likeCount
                && oldItem.ratingDetails.totalRatingValue == newItem.ratingDetails.totalRatingValue
                && oldItem.ratingDetails.totalRatingCount == newItem.ratingDetails.totalRatingCount;
    }

    public interface NewsClickCallback {
        void onClick(Product product);

        void onFavLikeClick(Product product, LikeButton likeButton);

        void onFavUnlikeClick(Product product, LikeButton likeButton);
    }


}
