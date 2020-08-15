package com.dewonderstruck.apps.ashx0.ui.product.filtering

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.TypeFilterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.ui.product.filtering.adapter.CategoryAdapter
import com.dewonderstruck.apps.ashx0.ui.product.filtering.adapter.CategoryAdapter.filteringClickCallback
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.category.CategoryViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.subcategory.SubCategoryViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource

class CategoryFilterFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var categoryViewModel: CategoryViewModel? = null
    private var subCategoryViewModel: SubCategoryViewModel? = null
    private var catId: String? = null
    private var subCatId: String? = null
    var intent = Intent()

    @VisibleForTesting
    private var binding: AutoClearedValue<TypeFilterBinding>? = null
    private var adapter: AutoClearedValue<CategoryAdapter>? = null
    private var lastCategoryData: AutoClearedValue<List<Category>?>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: TypeFilterBinding = DataBindingUtil.inflate(inflater, R.layout.type_filter, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get().loadingMore = connectivity.isConnected
        setHasOptionsMenu(true)
        return binding!!.get().root
    }

    override fun initUIAndActions() {

        // initToolBar();
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clear_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear) {
            catId = ""
            subCatId = ""
            initializeAdapter()
            initData()
            navigationController.navigateBackToHomeFeaturedFragment(this@CategoryFilterFragment.activity!!, catId, subCatId)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewModels() {
        categoryViewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
        subCategoryViewModel = ViewModelProvider(this, viewModelFactory).get(SubCategoryViewModel::class.java)
    }

    override fun initAdapters() {
        try {
            if (activity != null) {
                intent = requireActivity().intent
                catId = intent.getStringExtra(Constants.CATEGORY_ID)
                subCatId = intent.getStringExtra(Constants.SUBCATEGORY_ID)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("", e)
        }
        initializeAdapter()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initializeAdapter() {
        val nvAdapter = CategoryAdapter(dataBindingComponent, object : filteringClickCallback {
            override fun onClick(catId: String?, subCatId: String?) {
                assignCategoryId(catId!!, subCatId!!)
                navigationController.navigateBackToHomeFeaturedFragment(this@CategoryFilterFragment.activity!!, catId, subCatId)
                if (activity != null) {
                    this@CategoryFilterFragment.activity!!.finish()
                }
            }
        }, catId!!, subCatId!!)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().CategoryList.setAdapter(nvAdapter)
    }

    private fun assignCategoryId(catId: String, subCatId: String) {
        this.catId = catId
        this.subCatId = subCatId
    }

    override fun initData() {
        categoryViewModel!!.setCategoryListObj(loginUserId, categoryViewModel!!.categoryParameterHolder, java.lang.String.valueOf(Config.LIST_CATEGORY_COUNT), categoryViewModel!!.offset.toString())
        subCategoryViewModel!!.setAllSubCategoryListObj()
        val categories = categoryViewModel!!.categoryListData
        val subCategories = subCategoryViewModel!!.allSubCategoryListData
        categories?.observe(this, Observer { listResource: Resource<List<Category>?>? ->
            if (listResource != null) {
                if (listResource.data != null && listResource.data.size > 0) {
                    lastCategoryData = AutoClearedValue(this, listResource.data)
                    replaceCategory(lastCategoryData!!.get())
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
        subCategories?.observe(this, Observer { listResource: Resource<List<SubCategory>?>? ->
            if (listResource != null) {
                if (listResource.data != null && listResource.data.size > 0) {
                    replaceSubCategory(listResource.data)
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
    }

    private fun replaceCategory(CategoryList: List<Category>?) {
        adapter!!.get().replaceCategory(CategoryList)
        adapter!!.get().notifyDataSetChanged()
        binding!!.get().executePendingBindings()
    }

    private fun replaceSubCategory(subCategoryList: List<SubCategory>?) {
        adapter!!.get().replaceSubCategory(subCategoryList!!)
        adapter!!.get().notifyDataSetChanged()
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {

//      if  (categoryViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
////            if (binding.get().CategoryList != null) {
////
////                LinearLayoutManager layoutManager = (LinearLayoutManager)
////                        binding.get().CategoryList.getLayoutManager();
////
////
////                if (layoutManager != null) {
////                    layoutManager.scrollToPosition(0);
////                }
////            }
//        }
//
    }
}