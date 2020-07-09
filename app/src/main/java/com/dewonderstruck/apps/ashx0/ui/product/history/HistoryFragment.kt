package com.dewonderstruck.apps.ashx0.ui.product.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentHistoryBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.history.adapter.HistoryAdapter
import com.dewonderstruck.apps.ashx0.ui.product.history.adapter.HistoryAdapter.HistoryClickCallback
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.product.HistoryProductViewModel
import com.dewonderstruck.apps.ashx0.viewobject.HistoryProduct

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : PSFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var historyProductViewModel: HistoryProductViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentHistoryBinding>? = null
    private var historyAdapter: AutoClearedValue<HistoryAdapter>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {
        if (historyProductViewModel!!.loadingDirection == Utils.LoadingDirection.bottom) {
            if (binding!!.get().historyRecycler != null) {
                val layoutManager = binding!!.get().historyRecycler.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }

    override fun initUIAndActions() {
        binding!!.get().historyRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == historyAdapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !historyProductViewModel!!.forceEndLoading) {
                            historyProductViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                            val limit = Config.COMMENT_COUNT
                            historyProductViewModel!!.offset = historyProductViewModel!!.offset + limit
                            historyProductViewModel!!.setHistoryProductListObj(historyProductViewModel!!.offset.toString())
                        }
                    }
                }
            }
        })
    }

    override fun initViewModels() {
        historyProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryProductViewModel::class.java)
    }

    override fun initAdapters() {
        val historyAdapter = HistoryAdapter(dataBindingComponent, object : HistoryClickCallback {
            override fun onClick(historyProduct: HistoryProduct?) {
                navigationController.navigateToProductDetailActivity(this@HistoryFragment.requireActivity(), historyProduct!!)
            }
        })
        this.historyAdapter = AutoClearedValue(this, historyAdapter)
        binding!!.get().historyRecycler.adapter = historyAdapter
    }

    override fun initData() {
        loadData()
    }

    private fun loadData() {

        //load basket
        historyProductViewModel!!.offset = Config.COMMENT_COUNT
        historyProductViewModel!!.setHistoryProductListObj(historyProductViewModel!!.offset.toString())
        val historyProductList = historyProductViewModel!!.allHistoryProductList
        historyProductList?.observe(this, Observer { listResource: List<HistoryProduct>? ->
            listResource?.let { replaceProductHistoryData(it) }
        })
        historyProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get().loadingMore = historyProductViewModel!!.isLoading })
    }

    private fun replaceProductHistoryData(historyProductList: List<HistoryProduct>) {
        historyAdapter!!.get().replace(historyProductList)
        binding!!.get().executePendingBindings()
    }
}