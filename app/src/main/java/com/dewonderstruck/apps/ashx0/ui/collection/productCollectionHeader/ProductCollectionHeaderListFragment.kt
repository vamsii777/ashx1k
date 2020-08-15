package com.dewonderstruck.apps.ashx0.ui.collection.productCollectionHeader

import android.annotation.SuppressLint
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
import com.dewonderstruck.apps.ashx0.databinding.FragmentProductCollectionHeaderListBinding
import com.dewonderstruck.apps.ashx0.ui.collection.adapter.ProductCollectionHeaderListAdapter
import com.dewonderstruck.apps.ashx0.ui.collection.adapter.ProductCollectionHeaderListAdapter.ProductCollectionHeaderClickCallback
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.collection.ProductCollectionViewModel
import com.dewonderstruck.apps.ashx0.viewobject.ProductCollectionHeader
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

/**
 * A simple [Fragment] subclass.
 */
class ProductCollectionHeaderListFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var productCollectionViewModel: ProductCollectionViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentProductCollectionHeaderListBinding>? = null
    private var adapter: AutoClearedValue<ProductCollectionHeaderListAdapter>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentProductCollectionHeaderListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_collection_header_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get().loadingMore = connectivity.isConnected
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
        binding!!.get().productCollectionHeaderList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !productCollectionViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected) {
                                productCollectionViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                val limit = Config.PRODUCT_COUNT
                                productCollectionViewModel!!.offset = productCollectionViewModel!!.offset + limit
                                productCollectionViewModel!!.setNextPageLoadingStateObj(Config.PRODUCT_COUNT.toString(), productCollectionViewModel!!.offset.toString())
                            }
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            productCollectionViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset productViewModel.offset
            productCollectionViewModel!!.offset = 0

            // reset productViewModel.forceEndLoading
            productCollectionViewModel!!.forceEndLoading = false

            // update live data
            productCollectionViewModel!!.setProductCollectionHeaderListObj(Config.PRODUCT_COUNT.toString(), productCollectionViewModel!!.offset.toString())
        }
    }

    override fun initViewModels() {
        productCollectionViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductCollectionViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = ProductCollectionHeaderListAdapter(dataBindingComponent,
                object : ProductCollectionHeaderClickCallback {
                    @SuppressLint("UseRequireInsteadOfGet")
                    override fun onClick(productCollectionHeader: ProductCollectionHeader?) {
                        navigationController.navigateToCollectionProductList(this@ProductCollectionHeaderListFragment.activity!!, productCollectionHeader!!.id, productCollectionHeader.name, productCollectionHeader!!.defaultPhoto.imgPath)
                    }
                },
                this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().productCollectionHeaderList.adapter = nvAdapter
    }

    override fun initData() {
        loadDiscount()
    }

    //region Private Methods
    private fun loadDiscount() {

        // Load Latest ProductCollectionHeader
        productCollectionViewModel!!.setProductCollectionHeaderListObj(Config.PRODUCT_COUNT.toString(), productCollectionViewModel!!.offset.toString())
        val news = productCollectionViewModel!!.productCollectionHeaderListData
        news?.observe(this, Observer { listResource: Resource<List<ProductCollectionHeader?>>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            if (!connectivity.isConnected) {
                                //fadeIn Animation
                                fadeIn(binding!!.get().root)

                                // Update the data
                                replaceData(listResource.data)
                            }
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceData(listResource.data)
                        }
                        productCollectionViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        productCollectionViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (productCollectionViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    productCollectionViewModel!!.forceEndLoading = true
                }
            }
        })
        productCollectionViewModel!!.nextPageLoadingStateData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    productCollectionViewModel!!.setLoadingState(false)
                    productCollectionViewModel!!.forceEndLoading = true
                }
            }
        })
        productCollectionViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = productCollectionViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun replaceData(productCollectionHeaderList: List<ProductCollectionHeader?>) {
        adapter!!.get().replace(productCollectionHeaderList)
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {
        if (productCollectionViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().productCollectionHeaderList != null) {
                val layoutManager = binding!!.get().productCollectionHeaderList.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }
}