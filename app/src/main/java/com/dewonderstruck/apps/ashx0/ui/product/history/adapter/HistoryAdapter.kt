package com.dewonderstruck.apps.ashx0.ui.product.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemHistoryAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct

class HistoryAdapter(private val dataBindingComponent: DataBindingComponent, private val callback: HistoryClickCallback?) : DataBoundListAdapter<HistoryProduct?, ItemHistoryAdapterBinding?>() {
    private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface? = null
    override fun createBinding(parent: ViewGroup): ItemHistoryAdapterBinding {
        val binding: ItemHistoryAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.item_history_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val historyProduct = binding.history
            if (historyProduct != null && callback != null) {
                callback.onClick(historyProduct)
            }
        }
        return binding
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemHistoryAdapterBinding?, item: HistoryProduct?) {
        binding!!.history = item
        binding!!.historyNameTextView.text = item!!.historyName
    }

    protected override fun areItemsTheSame(oldItem: HistoryProduct?, newItem: HistoryProduct?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.historyName == newItem.historyName)
    }

    protected override fun areContentsTheSame(oldItem: HistoryProduct?, newItem: HistoryProduct?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.historyName == newItem.historyName)
    }

    interface HistoryClickCallback {
        fun onClick(historyProduct: HistoryProduct?)
    }

}