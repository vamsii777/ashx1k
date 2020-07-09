package com.dewonderstruck.apps.ashx0.ui.product.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentFavouriteListBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductVerticalListAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.product.ProductFavouriteViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.like.LikeButton

class FavouriteListFragment : PSFragment(), DiffUtilDispatchedInterface2 {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var productFavouriteViewModel: ProductFavouriteViewModel? = null
    private var favouriteViewModel: ProductFavouriteViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentFavouriteListBinding>? = null
    private var adapter: AutoClearedValue<ProductVerticalListAdapter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentFavouriteListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get().loadingMore = connectivity.isConnected
        return binding!!.get().root
    }

    override fun onDispatched() {
        if (productFavouriteViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().favouriteList != null) {
                val layoutManager = binding!!.get().favouriteList.layoutManager as GridLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get().favouriteList.isNestedScrollingEnabled = false
        binding!!.get().favouriteList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !productFavouriteViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected) {
                                productFavouriteViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                val limit = Config.RATING_COUNT
                                productFavouriteViewModel!!.offset = productFavouriteViewModel!!.offset + limit
                                productFavouriteViewModel!!.setNextPageLoadingFavouriteObj(productFavouriteViewModel!!.offset.toString(), loginUserId)
                            }
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            productFavouriteViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset productViewModel.offset
            productFavouriteViewModel!!.offset = 0

            // reset productViewModel.forceEndLoading
            productFavouriteViewModel!!.forceEndLoading = false

            // update live data
            productFavouriteViewModel!!.setProductFavouriteListObj(loginUserId, productFavouriteViewModel!!.offset.toString())
        }
    }

    override fun initViewModels() {
        productFavouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = ProductVerticalListAdapter(dataBindingComponent, object : ProductVerticalListAdapter.NewsClickCallback {
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(activity!!, product!!)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().favouriteList.adapter = nvAdapter
    }

    override fun initData() {
        productFavouriteViewModel!!.nextPageFavouriteLoadingData.observe(this, Observer { state: Resource<Boolean?>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    productFavouriteViewModel!!.setLoadingState(false) //hide
                    productFavouriteViewModel!!.forceEndLoading = true //stop
                }
            }
        })
        productFavouriteViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = productFavouriteViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })
        productFavouriteViewModel!!.setProductFavouriteListObj(loginUserId, productFavouriteViewModel!!.offset.toString())
        val news = productFavouriteViewModel!!.productFavouriteData
        news?.observe(this, Observer { listResource: Resource<List<Product>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get().root)

                            // Update the data
                            replaceData(listResource.data)
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceData(listResource.data)
                        }
                        productFavouriteViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR -> {
                        // Error State
                        productFavouriteViewModel!!.setLoadingState(false)
                        productFavouriteViewModel!!.forceEndLoading = true
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                if (productFavouriteViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    productFavouriteViewModel!!.forceEndLoading = true
                }
            }
        })

        //get favourite post method
        favouriteViewModel!!.favouritePostData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this@FavouriteListFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                        favouriteViewModel!!.setLoadingState(false)
                    }
                } else if (result.status == Status.ERROR) {
                    if (this@FavouriteListFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                        favouriteViewModel!!.setLoadingState(false)
                    }
                }
            }
        })
    }

    private fun replaceData(favouriteList: List<Product>) {
        adapter!!.get().replace(favouriteList)
        binding!!.get().executePendingBindings()
    }

    private fun unFavFunction(product: Product?, likeButton: LikeButton?) {
        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, activity, navigationController, likeButton) {

            // Success Action
            if (!favouriteViewModel!!.isLoading) {
                favouriteViewModel!!.setFavouritePostDataObj(product!!.id, loginUserId)
                likeButton!!.setLikeDrawable(ResourcesCompat.getDrawable(resources, R.drawable.heart_off, null))
            }
        }
    }

    private fun favFunction(product: Product?, likeButton: LikeButton?) {
        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, activity, navigationController, likeButton) {

            // Success Action
            if (!favouriteViewModel!!.isLoading) {
                favouriteViewModel!!.setFavouritePostDataObj(product!!.id, loginUserId)
                likeButton!!.setLikeDrawable(ResourcesCompat.getDrawable(resources, R.drawable.heart_on, null))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadLoginUserId()
    }
}