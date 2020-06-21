package com.dewonderstruck.apps.ashx0.ui.comment.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemCommentDetailAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.CommentDetail

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class CommentDetailAdapter constructor(private val dataBindingComponent: DataBindingComponent,
                                       private val callback: CommentDetailClickCallback?,
                                       private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<CommentDetail?, ItemCommentDetailAdapterBinding?>() {
    override fun createBinding(parent: ViewGroup?): ItemCommentDetailAdapterBinding {
        val binding: ItemCommentDetailAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.getContext()),
                        R.layout.item_comment_detail_adapter, parent, false,
                        dataBindingComponent)
        binding.getRoot().setOnClickListener(View.OnClickListener({ v: View? ->
            val commentDetail: CommentDetail? = binding.getCommentDetail()
            if (commentDetail != null && callback != null) {
                callback.onClick(commentDetail)
            }
        }))
        return binding
    }

    override fun dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched()
        }
    }

    protected override fun bind(binding: ItemCommentDetailAdapterBinding?, item: CommentDetail) {
        binding!!.setCommentDetail(item)
        binding.commentTextView.setText(item.detailComment)
    }

    public override fun bindView(holder: DataBoundViewHolder<ItemCommentDetailAdapterBinding>?, position: Int) {
        super.bindView(holder, position)
    }

    protected override fun areItemsTheSame(oldItem: CommentDetail, newItem: CommentDetail): Boolean {
        return (Objects.equals(oldItem.id, newItem.id)
                && (oldItem.headerId == newItem.headerId))
    }

    protected override fun areContentsTheSame(oldItem: CommentDetail, newItem: CommentDetail): Boolean {
        return (Objects.equals(oldItem.id, newItem.id)
                && (oldItem.headerId == newItem.headerId))
    }

    open interface CommentDetailClickCallback {
        fun onClick(comment: CommentDetail?)
    }

}