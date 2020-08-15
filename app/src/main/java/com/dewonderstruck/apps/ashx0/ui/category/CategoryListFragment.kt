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
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.category.CategoryViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.TouchCountViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

class CategoryListFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var categoryViewModel: CategoryViewModel? = null
    private var touchCountViewModel: TouchCountViewModel? = null
    private var basketViewModel: BasketViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentCategoryListBinding>? = null
    private var adapter: AutoClearedValue<CategoryAdapter>? = null
    private val basketMenuItem: AutoClearedValue<MenuItem>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentCategoryListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        binding!!.get().loadingMore = connectivity.isConnected
        return binding!!.get().root
    }

    private fun setBasketMenuItemVisible(isVisible: Boolean) {
        if (basketMenuItem != null) {
            basketMenuItem.get().isVisible = isVisible
        }
    }

    /* @Override
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
    }
*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_basket) {
            // navigationController.navigateToBasketList(activity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
        binding!!.get().categoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !categoryViewModel!!.forceEndLoading) {
                            if (connectivity.isConnected) {
                                categoryViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                val limit = Config.LIST_CATEGORY_COUNT
                                categoryViewModel!!.offset = categoryViewModel!!.offset + limit
                                categoryViewModel!!.setNextPageLoadingStateObj(Config.LIST_CATEGORY_COUNT.toString(), categoryViewModel!!.offset.toString(), categoryViewModel!!.categoryParameterHolder)
                            }
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            categoryViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset categoryViewModel.offset
            categoryViewModel!!.offset = 0

            // reset categoryViewModel.forceEndLoading
            categoryViewModel!!.forceEndLoading = false

            // update live data
            categoryViewModel!!.setCategoryListObj(loginUserId, categoryViewModel!!.categoryParameterHolder, Config.LIST_CATEGORY_COUNT.toString(), categoryViewModel!!.offset.toString())
        }
    }

    override fun initViewModels() {
        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel::class.java)
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = CategoryAdapter(dataBindingComponent,
                object: CategoryAdapter.CategoryClickCallback {
                    override fun onClick(category: Category?) {
                        categoryViewModel!!.productParameterHolder.catId = category!!.id
                        navigationController.navigateToHomeFilteringActivity(activity!!, categoryViewModel!!.productParameterHolder, category!!.name)
                        if (connectivity.isConnected) {
                            touchCountViewModel!!.setTouchCountPostDataObj(loginUserId, category!!.id, Constants.FILTERING_TYPE_NAME_CAT)
                        }
                    }
                }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().categoryList.adapter = nvAdapter
    }

    override fun initData() {
        loadCategory()
        //basketData()
    }

    /*private fun basketData() {
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
        categoryViewModel!!.setCategoryListObj(loginUserId, categoryViewModel!!.categoryParameterHolder, Config.LIST_CATEGORY_COUNT.toString(), categoryViewModel!!.offset.toString())
        val news = categoryViewModel!!.categoryListData
        news?.observe(this, Observer { listResource: Resource<List<Category>>? ->
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
                        categoryViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        categoryViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                if (categoryViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    categoryViewModel!!.forceEndLoading = true
                }
            }
        })
        categoryViewModel!!.nextPageLoadingStateData.observe(this, Observer { state: Resource<Boolean?>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    categoryViewModel!!.setLoadingState(false)
                    categoryViewModel!!.forceEndLoading = true
                }
            }
        })
        categoryViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = categoryViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })

        //get touch count post method
        touchCountViewModel!!.touchCountPostData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this@CategoryListFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                    }
                } else if (result.status == Status.ERROR) {
                    if (this@CategoryListFragment.activity != null) {
                        Utils.psLog(result.status.toString())
                    }
                }
            }
        })
    }

    private fun replaceData(categoryList: List<Category>) {
        adapter!!.get().replace(categoryList)
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {
        if (categoryViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().categoryList != null) {
                val layoutManager = binding!!.get().categoryList.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }
}