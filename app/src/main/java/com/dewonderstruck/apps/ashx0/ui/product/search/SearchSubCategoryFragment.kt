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
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.ui.product.search.adapter.SearchSubCategoryAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.viewmodel.subcategory.SubCategoryViewModel
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class SearchSubCategoryFragment : DeFragment() {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var subCategoryViewModel: SubCategoryViewModel? = null
    private var catId: String? = null
    private var subCatId: String? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentSearchCategoryBinding>? = null
    private var adapter: AutoClearedValue<SearchSubCategoryAdapter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentSearchCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_category, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        if (activity != null) {
            val intent = requireActivity().intent
            catId = intent.getStringExtra(Constants.CATEGORY_ID)
            subCatId = intent.getStringExtra(Constants.SUBCATEGORY_ID)
        }
        return binding!!.get().root
    }

    override fun initUIAndActions() {}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clear_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear) {
            subCatId = ""
            initAdapters()
            initData()
            navigationController.navigateBackToSearchFragmentFromSubCategory(this@SearchSubCategoryFragment.requireActivity(), subCatId, "")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewModels() {
        subCategoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(SubCategoryViewModel::class.java)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initAdapters() {
        val nvadapter = SearchSubCategoryAdapter(dataBindingComponent,
                object : SearchSubCategoryAdapter.NewsClickCallback {
                    override fun onClick(subCategory: SubCategory?) {
                        navigationController.navigateBackToSearchFragmentFromSubCategory(this@SearchSubCategoryFragment.requireActivity(), subCategory!!.id, subCategory.name)
                        if (activity != null) {
                            this@SearchSubCategoryFragment.activity!!.finish()
                        }
                    }
                }, subCatId)
        adapter = AutoClearedValue(this, nvadapter)
        binding!!.get().searchCategoryRecyclerView.adapter = nvadapter
    }

    override fun initData() {
        loadCategory()
    }

    private fun loadCategory() {

        // Load Category List
        subCategoryViewModel!!.setNextPageLoadingStateWithCatIdObj(loginUserId, Config.LIST_CATEGORY_COUNT.toString(), subCategoryViewModel!!.offset.toString(), catId)
        subCategoryViewModel!!.setSubCategoryListWithCatIdObj(loginUserId, subCategoryViewModel!!.offset.toString(), catId)
        val news = subCategoryViewModel!!.getsubCategoryListWithCatIdData()
        news?.observe(this, Observer { listResource: Resource<List<SubCategory>>? ->
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
                        subCategoryViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        subCategoryViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                if (subCategoryViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    subCategoryViewModel!!.forceEndLoading = true
                }
            }
        })
        subCategoryViewModel!!.getnextPageLoadingStateWithCatIdData().observe(this, Observer { state: Resource<Boolean?>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    subCategoryViewModel!!.setLoadingState(false)
                    subCategoryViewModel!!.forceEndLoading = true
                }
            }
        })
    }

    private fun replaceData(categoryList: List<SubCategory>) {
        adapter!!.get().replace(categoryList)
        binding!!.get().executePendingBindings()
    }
}