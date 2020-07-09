package com.dewonderstruck.apps.ashx0.ui.notification.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemNotificationListAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.Noti

class NotificationListAdapter(private val dataBindingComponent: DataBindingComponent,
                              private val callback: NotificationClickCallback?,
                              private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<Noti?, ItemNotificationListAdapterBinding?>() {
    override fun createBinding(parent: ViewGroup): ItemNotificationListAdapterBinding {
        val binding: ItemNotificationListAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_notification_list_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val notification = binding.notification
            if (notification != null && callback != null) {
                callback.onClick(notification)
            }
        }
        return binding
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemNotificationListAdapterBinding?, item: Noti?) {
        binding!!.notification = item
        if (item!!.isRead == Constants.ONE) {
            binding.notiConstraintLayout.setBackgroundColor(binding.root.resources.getColor(R.color.md_grey_200))
        } else {
            binding.notiConstraintLayout.setBackgroundColor(binding.root.resources.getColor(R.color.md_white_1000))
        }
        binding.messageTextView.text = item.message
        binding.dateTextView.text = item.addedDateStr
    }

    protected override fun areItemsTheSame(oldItem: Noti?, newItem: Noti?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.message == newItem.message && oldItem.isRead == newItem.isRead && oldItem.addedDateStr == newItem.addedDateStr)
    }

    protected override fun areContentsTheSame(oldItem: Noti?, newItem: Noti?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.message == newItem.message && oldItem.isRead == newItem.isRead && oldItem.addedDateStr == newItem.addedDateStr)
    }

    interface NotificationClickCallback {
        fun onClick(notification: Noti?)
    }

}