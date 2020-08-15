package com.dewonderstruck.apps.ashx0.ui.collection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentCollectionProductsBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductVerticalListAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.collection.ProductCollectionProductViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.ProductFavouriteViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.like.LikeButton

class CollectionFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var productCollectionProductViewModel: ProductCollectionProductViewModel? = null
    private var favouriteViewModel: ProductFavouriteViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private val basketMenuItem: MenuItem? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentCollectionProductsBinding>? = null
    private var adapter: AutoClearedValue<ProductVerticalListAdapter>? = null
    private var intent: AutoClearedValue<Intent>? = null
    private var id: String? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentCollectionProductsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection_products, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get().loadingMore = connectivity.isConnected
        setHasOptionsMenu(true)
        if (this.activity != null) {
            val intent1 = requireActivity().intent
            intent = AutoClearedValue(this, intent1)
        }
        id = intent!!.get().getStringExtra(Constants.COLLECTION_ID)
        val image = intent!!.get().getStringExtra(Constants.COLLECTION_IMAGE)
        binding!!.get().image = image
        return binding!!.get().root
    }

    private fun setBasketMenuItemVisible(isVisible: Boolean) {
        if (basketMenuItem != null) {
            basketMenuItem.isVisible = isVisible
        }
    }

    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = menu.findItem(R.id.action_basket);

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.setVisible(true);
            } else {
                basketMenuItem.setVisible(false);
            }
        }
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_basket) {
            //// navigationController.navigateToBasketList(activity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get().newsList.isNestedScrollingEnabled = false
        binding!!.get().newsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !productCollectionProductViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected) {
                                productCollectionProductViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                val limit = Config.PRODUCT_COUNT
                                productCollectionProductViewModel!!.offset = productCollectionProductViewModel!!.offset + limit
                                productCollectionProductViewModel!!.setNextPageLoadingStateObj(Config.PRODUCT_COUNT.toString(), productCollectionProductViewModel!!.offset.toString(), id)
                            }
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            productCollectionProductViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset productViewModel.offset
            productCollectionProductViewModel!!.offset = 0

            // reset productViewModel.forceEndLoading
            productCollectionProductViewModel!!.forceEndLoading = false

            // update live data
            productCollectionProductViewModel!!.setProductCollectionProductListObj(Config.PRODUCT_COUNT.toString(), productCollectionProductViewModel!!.offset.toString(), id)
        }
    }

    override fun initViewModels() {
        productCollectionProductViewModel = ViewModelProvider(this, viewModelFactory).get(ProductCollectionProductViewModel::class.java)
        favouriteViewModel = ViewModelProvider(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
        basketViewModel = ViewModelProvider(this, viewModelFactory).get(BasketViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = ProductVerticalListAdapter(dataBindingComponent, object : ProductVerticalListAdapter.NewsClickCallback {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@CollectionFragment.activity!!, product!!)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().newsList.adapter = nvAdapter
    }

    override fun initData() {
        loadDiscount()
        //basketData()
    }

   /* private fun basketData() {
        //set and get basket list
        basketViewModel!!.setBasketListObj()
        basketViewModel!!.allBasketList.observe(this, Observer { resource: List<Basket?>? ->
            if (resource != null) {
                basketViewModel!!.basketCount = resource.size
                if (resource.size > 0) {
                    setBasketMenuItemVisible(true)
                } else {
                    setBasketMenuItemVisible(false)
                }
            }
        })
    }*/

    //region Private Methods
    private fun loadDiscount() {

        // Load Latest Product
        productCollectionProductViewModel!!.setProductCollectionProductListObj(Config.PRODUCT_COUNT.toString(), productCollectionProductViewModel!!.offset.toString(), id)
        val news = productCollectionProductViewModel!!.productCollectionProductListData
        news?.observe(this, Observer { listResource: Resource<List<Product?>>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
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
                        productCollectionProductViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        productCollectionProductViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (productCollectionProductViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    productCollectionProductViewModel!!.forceEndLoading = true
                }
            }
        })
        productCollectionProductViewModel!!.nextPageLoadingStateData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    productCollectionProductViewModel!!.setLoadingState(false)
                    productCollectionProductViewModel!!.forceEndLoading = true
                }
            }
        })
        productCollectionProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = productCollectionProductViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun replaceData(newsList: List<Product?>) {
        adapter!!.get().replace(newsList)
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {
        if (productCollectionProductViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().newsList != null) {
                val layoutManager = binding!!.get().newsList.layoutManager as LinearLayoutManager?
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