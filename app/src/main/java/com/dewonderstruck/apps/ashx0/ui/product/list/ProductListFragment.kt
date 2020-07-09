package com.dewonderstruck.apps.ashx0.ui.product.list

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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.BottomBoxLayoutBinding
import com.dewonderstruck.apps.ashx0.databinding.FragmentProductListBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductVerticalListAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.homelist.HomeSearchProductViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.ProductFavouriteViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.TouchCountViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.like.LikeButton
import java.util.*

class ProductListFragment : PSFragment(), DiffUtilDispatchedInterface2 {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var typeClicked = false
    private var filterClicked = false
    private val clearRecyclerView: List<Product> = ArrayList()
    private var homeSearchProductViewModel: HomeSearchProductViewModel? = null
    private var touchCountViewModel: TouchCountViewModel? = null
    private var favouriteViewModel: ProductFavouriteViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentProductListBinding?>? = null
    private var adapter: AutoClearedValue<ProductVerticalListAdapter>? = null
    private var bottomBoxLayoutBinding: AutoClearedValue<BottomBoxLayoutBinding>? = null
    private var mBottomSheetDialog: AutoClearedValue<BottomSheetDialog>? = null
    private val basketMenuItem: AutoClearedValue<MenuItem>? = null

    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        val dataBinding: FragmentProductListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        return binding!!.get()!!.root
    }

    private fun setBasketMenuItemVisible(isVisible: Boolean) {
        if (basketMenuItem != null) {
            basketMenuItem.get().isVisible = isVisible
        }
    }

    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = new AutoClearedValue<>(this, menu.findItem(R.id.action_basket));

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.get().setVisible(true);
            } else {
                basketMenuItem.get().setVisible(false);
            }
        }
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_basket) {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE__PRODUCT_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__CATEGORY_FILTER) {

            // Result from Category Filter List
            val catId = data!!.getStringExtra(Constants.CATEGORY_ID)
            if (catId != null) {
                homeSearchProductViewModel!!.holder.catId = catId
            }
            val subCatId = data.getStringExtra(Constants.SUBCATEGORY_ID)
            if (subCatId != null) {
                homeSearchProductViewModel!!.holder.subCatId = subCatId
            }
            typeClicked = (homeSearchProductViewModel!!.holder.catId != null && homeSearchProductViewModel!!.holder.catId != ""
                    || homeSearchProductViewModel!!.holder.subCatId != null && homeSearchProductViewModel!!.holder.subCatId != "")
            typeButtonClicked(typeClicked)
            replaceData(clearRecyclerView)
            loadProductList()
        } else if (requestCode == Constants.REQUEST_CODE__PRODUCT_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__SPECIAL_FILTER) {

            // Result From Filter
            if (data!!.getSerializableExtra(Constants.FILTERING_HOLDER) != null) {
                homeSearchProductViewModel!!.holder = data.getSerializableExtra(Constants.FILTERING_HOLDER) as ProductParameterHolder?
            }
            filterClicked = homeSearchProductViewModel!!.holder.search_term != Constants.FILTERING_INACTIVE ||  //!homeSearchProductViewModel.holder.min_price.equals(Constants.FILTERING_INACTIVE) ||
                    homeSearchProductViewModel!!.holder.max_price != Constants.FILTERING_INACTIVE ||
                    homeSearchProductViewModel!!.holder.isFeatured != Constants.FILTERING_INACTIVE ||
                    homeSearchProductViewModel!!.holder.isDiscount != Constants.FILTERING_INACTIVE ||
                    homeSearchProductViewModel!!.holder.overall_rating != Constants.FILTERING_INACTIVE
            tuneButtonClicked(filterClicked)
            replaceData(clearRecyclerView)
            loadProductList()
        }
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        if (context != null) {

            // Prepare Sorting Bottom Sheet
            mBottomSheetDialog = AutoClearedValue(this, BottomSheetDialog(requireContext()))
            bottomBoxLayoutBinding = AutoClearedValue(this, DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_box_layout, null, false))
            mBottomSheetDialog!!.get().setContentView(bottomBoxLayoutBinding!!.get().root)
        }
        binding!!.get()!!.typeButton.setOnClickListener { view: View -> ButtonClick(view) }
        binding!!.get()!!.tuneButton.setOnClickListener { view: View -> ButtonClick(view) }
        binding!!.get()!!.sortButton.setOnClickListener { view: View -> ButtonClick(view) }
        binding!!.get()!!.newsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get()!!.loadingMore && !homeSearchProductViewModel!!.forceEndLoading) {
                            homeSearchProductViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                            val limit = Config.PRODUCT_COUNT
                            homeSearchProductViewModel!!.offset = homeSearchProductViewModel!!.offset + limit
                            loadNextPageProductList(homeSearchProductViewModel!!.offset.toString())
                        }
                    }
                }
            }
        })
        binding!!.get()!!.swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get()!!.swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get()!!.swipeRefresh.setOnRefreshListener {
            homeSearchProductViewModel!!.forceEndLoading = false
            homeSearchProductViewModel!!.loadingDirection = Utils.LoadingDirection.top
            loadProductList()
        }
    }

    override fun initViewModels() {
        // ViewModel need to get from ViewModelProviders
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel::class.java)
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel::class.java)
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = ProductVerticalListAdapter(dataBindingComponent, object : ProductVerticalListAdapter.NewsClickCallback {
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@ProductListFragment.requireActivity(), product!!)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get()!!.newsList.adapter = nvAdapter
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initData() {

//        homeSearchProductViewModel.holder.shopId = selectedShopId;
        basketData()
        if (activity != null) {
            homeSearchProductViewModel!!.holder = activity!!.intent.getSerializableExtra(Constants.PRODUCT_PARAM_HOLDER_KEY) as ProductParameterHolder?
            if (arguments != null) {
                homeSearchProductViewModel!!.holder = arguments!!.getSerializable(Constants.PRODUCT_PARAM_HOLDER_KEY) as ProductParameterHolder?
            }
            if (homeSearchProductViewModel!!.holder != null) {
                when (homeSearchProductViewModel!!.holder.order_by) {
                    Constants.FILTERING_ADDED_DATE -> setSortingSelection(0)
                    Constants.FILTERING_TRENDING -> setSortingSelection(1)
                    else -> setSortingSelection(0)
                }
            }
            initProductData()
            setTouchCount()
            filterClicked = homeSearchProductViewModel!!.holder.search_term != Constants.FILTERING_INACTIVE ||  //!homeSearchProductViewModel.holder.min_price.equals("0") ||
                    homeSearchProductViewModel!!.holder.max_price != Constants.FILTERING_INACTIVE ||
                    homeSearchProductViewModel!!.holder.isFeatured != Constants.FILTERING_INACTIVE ||
                    homeSearchProductViewModel!!.holder.isDiscount != Constants.FILTERING_INACTIVE ||
                    homeSearchProductViewModel!!.holder.overall_rating != Constants.FILTERING_INACTIVE
            typeClicked = homeSearchProductViewModel!!.holder.catId != Constants.FILTERING_INACTIVE || homeSearchProductViewModel!!.holder.subCatId != Constants.FILTERING_INACTIVE
            typeButtonClicked(typeClicked)
            tuneButtonClicked(filterClicked)
        }

        // touch count post method
        if (connectivity.isConnected) {
            touchCountViewModel!!.setTouchCountPostDataObj(loginUserId, homeSearchProductViewModel!!.holder.catId, Constants.FILTERING_TYPE_NAME_CAT)
        }
        touchCountViewModel!!.touchCountPostData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this@ProductListFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                    }
                } else if (result.status == Status.ERROR) {
                    if (this@ProductListFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                    }
                }
            }
        })
    }

    private fun basketData() {
        //set and get basket list
        basketViewModel!!.setBasketListObj()
        basketViewModel!!.allBasketList.observe(this, Observer { resourse: List<Basket?>? ->
            if (resourse != null) {
                basketViewModel!!.basketCount = resourse.size
                if (resourse.size > 0) {
                    setBasketMenuItemVisible(true)
                } else {
                    setBasketMenuItemVisible(false)
                }
            }
        })
    }

    //endregion
    //region Private Methods
    private fun initProductData() {
        loadProductList()
        val news = homeSearchProductViewModel!!.getProductListByKeyData
        news?.observe(this, Observer { listResource: Resource<List<Product>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.SUCCESS -> if (listResource.data != null) {
                        if (listResource.data.size == 0) {
                            if (!binding!!.get()!!.loadingMore) {
                                binding!!.get()!!.noItemConstraintLayout.visibility = View.VISIBLE
                            }
                        } else {
                            binding!!.get()!!.noItemConstraintLayout.visibility = View.INVISIBLE
                        }
                        fadeIn(binding!!.get()!!.root)
                        replaceData(listResource.data)
                        homeSearchProductViewModel!!.setLoadingState(false)
                        onDispatched()
                    }
                    Status.LOADING -> if (listResource.data != null) {
                        if (listResource.data.size == 0) {
                            if (!binding!!.get()!!.loadingMore) {
                                binding!!.get()!!.noItemConstraintLayout.visibility = View.VISIBLE
                            }
                        } else {
                            binding!!.get()!!.noItemConstraintLayout.visibility = View.INVISIBLE
                        }
                        fadeIn(binding!!.get()!!.root)
                        replaceData(listResource.data)
                        onDispatched()
                    }
                    Status.ERROR -> {
                        homeSearchProductViewModel!!.setLoadingState(false)
                        homeSearchProductViewModel!!.forceEndLoading = true
                        if (homeSearchProductViewModel!!.getProductListByKeyData != null) {
                            if (homeSearchProductViewModel!!.getProductListByKeyData.value != null) {
                                if (homeSearchProductViewModel!!.getProductListByKeyData.value!!.data != null) {
                                    if (!binding!!.get()!!.loadingMore && homeSearchProductViewModel!!.getProductListByKeyData.value!!.data!!.size == 0) {
                                        binding!!.get()!!.noItemConstraintLayout.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
        })
        homeSearchProductViewModel!!.getNextPageProductListByKeyData.observe(this, Observer { state: Resource<Boolean?>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    homeSearchProductViewModel!!.setLoadingState(false)
                    homeSearchProductViewModel!!.forceEndLoading = true
                }
            }
        })
        homeSearchProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get()!!.loadingMore = homeSearchProductViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get()!!.swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun replaceData(newsList: List<Product>) {
        adapter!!.get().replace(newsList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun typeButtonClicked(b: Boolean) {
        if (b) {
            binding!!.get()!!.typeButton.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.baseline_list_with_check_orange_24), null, null)
        } else {
            binding!!.get()!!.typeButton.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.baseline_list_orange_24), null, null)
        }
    }

    private fun tuneButtonClicked(b: Boolean) {
        if (b) {
            binding!!.get()!!.tuneButton.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.baseline_tune_with_check_orange_24), null, null)
        } else {
            binding!!.get()!!.tuneButton.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.baseline_tune_orange_24), null, null)
        }
    }

    private fun ButtonClick(view: View) {
        when (view.id) {
            R.id.typeButton -> {
                navigationController.navigateToTypeFilterFragment(this@ProductListFragment.requireActivity(), homeSearchProductViewModel!!.holder.catId,
                        homeSearchProductViewModel!!.holder.subCatId, homeSearchProductViewModel!!.holder, Constants.FILTERING_TYPE_FILTER)
                typeButtonClicked(typeClicked)
            }
            R.id.tuneButton -> {
                navigationController.navigateToTypeFilterFragment(this@ProductListFragment.requireActivity(), homeSearchProductViewModel!!.holder.catId,
                        homeSearchProductViewModel!!.holder.subCatId, homeSearchProductViewModel!!.holder, Constants.FILTERING_SPECIAL_FILTER)
                tuneButtonClicked(filterClicked)
            }
            R.id.sortButton -> {
                mBottomSheetDialog!!.get().show()
                ButtonSheetClick()
            }
            else -> Utils.psLog("No ID for Buttons")
        }
    }

    private fun ButtonSheetClick() {
        bottomBoxLayoutBinding!!.get().popularButton.setOnClickListener { view: View? ->
            homeSearchProductViewModel!!.loadingDirection = Utils.LoadingDirection.top
            setSortingSelection(1)
            homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_TRENDING
            homeSearchProductViewModel!!.holder.order_type = Constants.FILTERING_DESC
            replaceData(clearRecyclerView)
            loadProductList()
            mBottomSheetDialog!!.get().dismiss()
        }
        bottomBoxLayoutBinding!!.get().recentButton.setOnClickListener { view: View? ->
            homeSearchProductViewModel!!.loadingDirection = Utils.LoadingDirection.top
            setSortingSelection(0)
            if (homeSearchProductViewModel!!.holder.isFeatured == Constants.ONE) {
                homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_FEATURE
            } else {
                homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_ADDED_DATE
            }
            homeSearchProductViewModel!!.holder.order_type = Constants.FILTERING_DESC
            replaceData(clearRecyclerView)
            loadProductList()
            mBottomSheetDialog!!.get().dismiss()
        }
        bottomBoxLayoutBinding!!.get().lowestButton.setOnClickListener { view: View? ->
            homeSearchProductViewModel!!.loadingDirection = Utils.LoadingDirection.top
            setSortingSelection(2)
            homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_PRICE
            homeSearchProductViewModel!!.holder.order_type = Constants.FILTERING_ASC
            replaceData(clearRecyclerView)
            loadProductList()
            mBottomSheetDialog!!.get().dismiss()
        }
        bottomBoxLayoutBinding!!.get().highestButton.setOnClickListener { view: View? ->
            homeSearchProductViewModel!!.loadingDirection = Utils.LoadingDirection.top
            setSortingSelection(3)
            homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_PRICE
            homeSearchProductViewModel!!.holder.order_type = Constants.FILTERING_DESC
            replaceData(clearRecyclerView)
            loadProductList()
            mBottomSheetDialog!!.get().dismiss()
        }
    }

    private fun setSortingSelection(index: Int) {
        binding!!.get()!!.sortButton.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.baseline_sort_with_check_orange_24), null, null)
        if (index == 0) {
            bottomBoxLayoutBinding!!.get().recentButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_access_time_24), null, resources.getDrawable(R.drawable.baseline_check_green_24), null)
        } else {
            bottomBoxLayoutBinding!!.get().recentButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_access_time_24), null, null, null)
        }
        if (index == 1) {
            bottomBoxLayoutBinding!!.get().popularButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_trending_up_24), null, resources.getDrawable(R.drawable.baseline_check_green_24), null)
        } else {
            bottomBoxLayoutBinding!!.get().popularButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_trending_up_24), null, null, null)
        }
        if (index == 2) {
            bottomBoxLayoutBinding!!.get().lowestButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24), null, resources.getDrawable(R.drawable.baseline_check_green_24), null)
        } else {
            bottomBoxLayoutBinding!!.get().lowestButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24), null, null, null)
        }
        if (index == 3) {
            bottomBoxLayoutBinding!!.get().highestButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24), null, resources.getDrawable(R.drawable.baseline_check_green_24), null)
        } else {
            bottomBoxLayoutBinding!!.get().highestButton.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24), null, null, null)
        }
    }

    private fun setTouchCount() {
        if (!homeSearchProductViewModel!!.holder.catId.isEmpty()) {
            //Utils.psLog("Hotel Id : " + touchCountViewModel.hotelId + "  Login User Id : " + loginUserId);
            if (connectivity.isConnected) {
                if (loginUserId == "") {
                    touchCountViewModel!!.setTouchCountPostDataObj(Constants.ZERO, homeSearchProductViewModel!!.holder.catId, Constants.FILTERING_CATEGORY_TYPE_NAME)
                } else {
                    touchCountViewModel!!.setTouchCountPostDataObj(loginUserId, homeSearchProductViewModel!!.holder.catId, Constants.FILTERING_CATEGORY_TYPE_NAME)
                }
                touchCountViewModel!!.touchCountPostData.observe(this, Observer { state: Resource<Boolean?>? ->
                    if (state != null) {
                        if (state.status == Status.SUCCESS) {
                            Utils.psLog("SUCCEED")
                        } else if (state.status == Status.ERROR) {
                            Utils.psLog("FAILED")
                        }
                    }
                })
            }
        }
    }

    override fun onDispatched() {
        if (homeSearchProductViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get() != null) {
                val layoutManager = binding!!.get()!!.newsList.layoutManager as GridLayoutManager?
                layoutManager?.scrollToPositionWithOffset(0, 0)
            }
        }
    }

    private fun resetLimitAndOffset() {
        homeSearchProductViewModel!!.offset = 0
    }

    private fun loadNextPageProductList(offset: String) {
        checkAndUpdateFeatureSorting()
        homeSearchProductViewModel!!.setGetNextPageProductListByKeyObj(homeSearchProductViewModel!!.holder, loginUserId, Config.PRODUCT_COUNT.toString(), offset)
    }

    private fun loadProductList() {
        resetLimitAndOffset()
        checkAndUpdateFeatureSorting()
        homeSearchProductViewModel!!.setGetProductListByKeyObj(homeSearchProductViewModel!!.holder, loginUserId, Config.PRODUCT_COUNT.toString(), Constants.ZERO)
    }

    private fun checkAndUpdateFeatureSorting() {
        if (homeSearchProductViewModel!!.holder.isFeatured == Constants.ONE && homeSearchProductViewModel!!.holder.order_by == Constants.FILTERING_ADDED_DATE) {
            homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_FEATURE
        }
    }

    //endregion
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