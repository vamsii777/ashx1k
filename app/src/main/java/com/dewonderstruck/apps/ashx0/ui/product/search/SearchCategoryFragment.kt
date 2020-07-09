package com.dewonderstruck.apps.ashx0.ui.product.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentSearchCategoryBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.search.adapter.SearchCategoryAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.category.CategoryViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

class SearchCategoryFragment : PSFragment() {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var categoryViewModel: CategoryViewModel? = null
    var catId: String? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentSearchCategoryBinding>? = null
    private var adapter: AutoClearedValue<SearchCategoryAdapter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentSearchCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_category, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        if (activity != null) {
            val intent = requireActivity().intent
            catId = intent.getStringExtra(Constants.CATEGORY_ID)
        }
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clear_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear) {
            catId = ""
            initAdapters()
            initData()
            navigationController.navigateBackToSearchFragment(this@SearchCategoryFragment.activity, catId, "")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewModels() {
        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initAdapters() {
        val nvadapter = SearchCategoryAdapter(dataBindingComponent,
                object: SearchCategoryAdapter.NewsClickCallback {
                    override fun onClick(category: Category?, id: String?) {
                        navigationController.navigateBackToSearchFragment(this@SearchCategoryFragment.activity, category!!.id, category.name)
                        if (activity != null) {
                            this@SearchCategoryFragment.activity!!.finish()
                        }
                    }

                }, catId)
        adapter = AutoClearedValue(this, nvadapter)
        binding!!.get().searchCategoryRecyclerView.adapter = nvadapter
    }

    override fun initData() {
        loadCategory()
    }

    private fun loadCategory() {

        // Load Category List
        categoryViewModel!!.setCategoryListObj(loginUserId, categoryViewModel!!.categoryParameterHolder, Config.LIST_CATEGORY_COUNT.toString(), categoryViewModel!!.offset.toString())
        val news = categoryViewModel!!.categoryListData
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
                        categoryViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        categoryViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (categoryViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    categoryViewModel!!.forceEndLoading = true
                }
            }
        })
        categoryViewModel!!.nextPageLoadingStateData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    categoryViewModel!!.setLoadingState(false)
                    categoryViewModel!!.forceEndLoading = true
                }
            }
        })
    }

    private fun replaceData(categoryList: List<Category?>) {
        adapter!!.get().replace(categoryList)
        binding!!.get().executePendingBindings()
    }
}