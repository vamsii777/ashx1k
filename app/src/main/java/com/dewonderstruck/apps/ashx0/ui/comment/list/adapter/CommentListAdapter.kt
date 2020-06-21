package com.dewonderstruck.apps.ashx0.ui.comment.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemCommentListAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundViewHolder
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Comment

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class CommentListAdapter(private val dataBindingComponent: DataBindingComponent,
                         private val callback: CommentClickCallback?,
                         private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<Comment?, ItemCommentListAdapterBinding?>() {
    override fun createBinding(parent: ViewGroup): ItemCommentListAdapterBinding {
        val binding: ItemCommentListAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_comment_list_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val comment = binding.comment
            if (comment != null && callback != null) {
                callback.onClick(comment)
            }
        }
        return binding
    }

    override fun bindView(holder: DataBoundViewHolder<ItemCommentListAdapterBinding>, position: Int) {
        super.bindView(holder, position)
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemCommentListAdapterBinding, item: Comment) {
        binding.comment = item
        if (item.commentReplyCount == Constants.ZERO) {
            binding.replyCountTextView.visibility = View.GONE
        } else {
            binding.replyCountTextView.visibility = View.VISIBLE
        }
        binding.replyCountTextView.text = binding.root.resources.getString(R.string.comment__replies, item.commentReplyCount)
    }

    protected override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return (Objects.equals(oldItem.id, newItem.id)
                && oldItem.headerComment == newItem.headerComment && oldItem.commentReplyCount == newItem.commentReplyCount)
    }

    protected override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return (Objects.equals(oldItem.id, newItem.id)
                && oldItem.headerComment == newItem.headerComment && oldItem.commentReplyCount == newItem.commentReplyCount)
    }

    interface CommentClickCallback {
        fun onClick(comment: Comment?)
    }

}