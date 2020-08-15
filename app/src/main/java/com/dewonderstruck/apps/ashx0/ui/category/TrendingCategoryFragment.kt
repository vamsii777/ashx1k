package com.dewonderstruck.apps.ashx0.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentCategoryListBinding
import com.dewonderstruck.apps.ashx0.ui.category.adapter.CategoryAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.homelist.HomeTrendingCategoryListViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class TrendingCategoryFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var homeTrendingCategoryListViewModel: HomeTrendingCategoryListViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private val basketMenuItem: MenuItem? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentCategoryListBinding>? = null
    private var adapter: AutoClearedValue<CategoryAdapter>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentCategoryListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        Utils.psLog("I am Trending Category")
        binding!!.get().loadingMore = connectivity.isConnected
        setHasOptionsMenu(true)
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
           // // navigationController.navigateToBasketList(activity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initUIAndActions() {
        binding!!.get().categoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !homeTrendingCategoryListViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected) {
                                homeTrendingCategoryListViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                val limit = Config.LIST_CATEGORY_COUNT
                                homeTrendingCategoryListViewModel!!.offset = homeTrendingCategoryListViewModel!!.offset + limit
                                homeTrendingCategoryListViewModel!!.setHomeTrendingCategoryLoadNetworkObj(loginUserId, Config.LIST_CATEGORY_COUNT.toString(), homeTrendingCategoryListViewModel!!.offset.toString(), homeTrendingCategoryListViewModel!!.categoryParameterHolder)
                            }
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            homeTrendingCategoryListViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset homeTrendingCategoryListViewModel.offset
            homeTrendingCategoryListViewModel!!.offset = 0

            // reset categoryViewMdoel.forceEndLoading
            homeTrendingCategoryListViewModel!!.forceEndLoading = false

            // update live data
            homeTrendingCategoryListViewModel!!.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel!!.categoryParameterHolder, Config.LIST_CATEGORY_COUNT.toString(), homeTrendingCategoryListViewModel!!.offset.toString())
        }
    }

    override fun initViewModels() {
        homeTrendingCategoryListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingCategoryListViewModel::class.java)
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
    }

    override fun initAdapters() {
        /*val nvAdapter = CategoryAdapter(dataBindingComponent,
                CategoryAdapter.CategoryClickCallback { category: Category ->
                    homeTrendingCategoryListViewModel!!.productParameterHolder.catId = category.id
                    navigationController.navigateToHomeFilteringActivity(activity, homeTrendingCategoryListViewModel!!.productParameterHolder, null)
                }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().categoryList.adapter = nvAdapter*/
    }

    override fun initData() {
        loadCategory()
        //basketData()
    }

   /* private fun basketData() {
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
    }*/

    //region Private Methods
    private fun loadCategory() {

        // Load Category List
        homeTrendingCategoryListViewModel!!.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel!!.categoryParameterHolder, Config.LIST_CATEGORY_COUNT.toString(), homeTrendingCategoryListViewModel!!.offset.toString())
        val news = homeTrendingCategoryListViewModel!!.homeTrendingCategoryListData
        news?.observe(this, Observer { listResource: Resource<List<Category?>>? ->
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
                        homeTrendingCategoryListViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        homeTrendingCategoryListViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (homeTrendingCategoryListViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    homeTrendingCategoryListViewModel!!.forceEndLoading = true
                }
            }
        })
        homeTrendingCategoryListViewModel!!.homeTrendingCategoryLoadNetworkData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    homeTrendingCategoryListViewModel!!.setLoadingState(false)
                    homeTrendingCategoryListViewModel!!.forceEndLoading = true
                }
            }
        })
        homeTrendingCategoryListViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = homeTrendingCategoryListViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun replaceData(categoryList: List<Category?>?) {
        adapter!!.get().replace(categoryList)
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {
        if (homeTrendingCategoryListViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().categoryList != null) {
                val layoutManager = binding!!.get().categoryList.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }
}