package com.dewonderstruck.apps.ashx0.ui.product.search

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
import com.dewonderstruck.apps.ashx0.databinding.FragmentSearchCityListBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.search.adapter.SearchCityAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.city.CityViewModel
import com.dewonderstruck.apps.ashx0.viewobject.City
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

/**
 * A simple [Fragment] subclass.
 */
class SearchCityListFragment : PSFragment() {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var cityViewModel: CityViewModel? = null
    var cityId: String? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentSearchCityListBinding>? = null
    private var adapter: AutoClearedValue<SearchCityAdapter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentSearchCityListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_city_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        if (activity != null) {
            val intent = requireActivity().intent
            cityId = intent.getStringExtra(Constants.CITY_ID)
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
            cityId = ""
            initAdapters()
            initData()
            navigationController.navigateBackToSearchFragmentFromCity(this@SearchCityListFragment.activity, cityId, "")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewModels() {
        cityViewModel = ViewModelProviders.of(this, viewModelFactory).get(CityViewModel::class.java)
    }

    override fun initAdapters() {
        /*val nvadapter = SearchCityAdapter(dataBindingComponent,
                SearchCityAdapter.NewsClickCallback { city: City, id: String? ->
                    navigationController.navigateBackToSearchFragmentFromCity(this@SearchCityListFragment.activity, city.id, city.name)
                    if (this@SearchCityListFragment.activity != null) {
                        this@SearchCityListFragment.activity!!.finish()
                    }
                }, cityId)
        adapter = AutoClearedValue(this, nvadapter)
        binding!!.get().searchCategoryRecyclerView.adapter = nvadapter*/
    }

    override fun initData() {
        if (activity != null) {
            cityViewModel!!.countryId = requireActivity().intent.getStringExtra(Constants.COUNTRY_ID)
        }
        loadCity()
    }

    private fun loadCity() {

        // Load City List
        cityViewModel!!.setCityListObj(shopId, cityViewModel!!.countryId, Config.LIST_CATEGORY_COUNT.toString(), cityViewModel!!.offset.toString())
        val news = cityViewModel!!.cityListData
        news?.observe(this, Observer { listResource: Resource<List<City?>>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceData(listResource.data)
                        }
                        cityViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        cityViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (cityViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    cityViewModel!!.forceEndLoading = true
                }
            }
        })
        cityViewModel!!.nextPageCityListData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    cityViewModel!!.setLoadingState(false)
                    cityViewModel!!.forceEndLoading = true
                }
            }
        })
    }

    private fun replaceData(cityList: List<City?>) {
        adapter!!.get().replace(cityList)
        binding!!.get().executePendingBindings()
    }
}