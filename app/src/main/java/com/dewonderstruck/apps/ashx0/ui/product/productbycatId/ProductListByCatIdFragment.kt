package com.dewonderstruck.apps.ashx0.ui.product.productbycatId

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentProdcutListByCatidBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductVerticalListAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.product.ProductFavouriteViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.ProductListByCatIdViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.like.LikeButton

class ProductListByCatIdFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var catId: String? = null
    private var favouriteViewModel: ProductFavouriteViewModel? = null
    private var productListByCatIdViewModel: ProductListByCatIdViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentProdcutListByCatidBinding>? = null
    private var adapter: AutoClearedValue<ProductVerticalListAdapter>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentProdcutListByCatidBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_prodcut_list_by_catid, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get().loadingMore = connectivity.isConnected
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get().productList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !productListByCatIdViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected) {
                                productListByCatIdViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                val limit = Config.LIST_CATEGORY_COUNT
                                productListByCatIdViewModel!!.offset = productListByCatIdViewModel!!.offset + limit
                                productListByCatIdViewModel!!.setNextPageLoadingStateObj(loginUserId, productListByCatIdViewModel!!.offset.toString(), catId)
                            }
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            productListByCatIdViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset productViewModel.offset
            productListByCatIdViewModel!!.offset = 0

            // reset productViewModel.forceEndLoading
            productListByCatIdViewModel!!.forceEndLoading = false

            // update live data
            productListByCatIdViewModel!!.setProductListObj(loginUserId, productListByCatIdViewModel!!.offset.toString(), catId)
        }
    }

    override fun initViewModels() {
        productListByCatIdViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductListByCatIdViewModel::class.java)
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = ProductVerticalListAdapter(dataBindingComponent, object : ProductVerticalListAdapter.NewsClickCallback {
            override fun onClick(product: Product?) {
                navigationController.navigateToShopProfile((activity as MainActivity?)!!)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().productList.adapter = nvAdapter
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initData() {
        try {
            if (activity != null) {
                if (activity!!.intent.extras != null) {
                    catId = activity!!.intent.extras!!.getString(Constants.CATEGORY_ID)
                }
            }
        } catch (e: Exception) {
            Utils.psErrorLog("", e)
        }
        loadProduct()
    }

    //region Private Methods
    private fun loadProduct() {

        // Load Latest Product
        productListByCatIdViewModel!!.setProductListObj(loginUserId, productListByCatIdViewModel!!.offset.toString(), catId)
        val news = productListByCatIdViewModel!!.getproductList()
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
                        productListByCatIdViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        productListByCatIdViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (productListByCatIdViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    productListByCatIdViewModel!!.forceEndLoading = true
                }
            }
        })
        productListByCatIdViewModel!!.nextPageLoadingStateData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    productListByCatIdViewModel!!.setLoadingState(false)
                    productListByCatIdViewModel!!.forceEndLoading = true
                }
            }
        })
        productListByCatIdViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = productListByCatIdViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun replaceData(newsList: List<Product>) {
        adapter!!.get().replace(newsList)
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {
        if (productListByCatIdViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().productList != null) {
                val layoutManager = binding!!.get().productList.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
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