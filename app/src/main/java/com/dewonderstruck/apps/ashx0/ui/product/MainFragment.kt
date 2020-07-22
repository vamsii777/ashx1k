package com.dewonderstruck.apps.ashx0.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentMainBinding
import com.dewonderstruck.apps.ashx0.ui.category.adapter.CategoryIconListAdapter
import com.dewonderstruck.apps.ashx0.ui.category.adapter.TrendingCategoryAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.adapter.DiscountListAdapter
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductCollectionRowAdapter
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductHorizontalListAdapter
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ViewPagerAdapter
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ViewPagerAdapter.ItemClick
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.apploading.AppLoadingViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.category.CategoryViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.clearalldata.ClearAllDataViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.collection.ProductCollectionViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.homelist.*
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.ProductFavouriteViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.TouchCountViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shop.ShopViewModel
import com.dewonderstruck.apps.ashx0.viewobject.*
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import com.google.android.gms.ads.AdRequest
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.like.LikeButton
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : PSFragment(), DiffUtilDispatchedInterface2 {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var homeLatestProductViewModel: HomeLatestProductViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private var homeSearchProductViewModel: HomeSearchProductViewModel? = null
    private var homeTrendingProductViewModel: HomeTrendingProductViewModel? = null
    private var homeTrendingCategoryListViewModel: HomeTrendingCategoryListViewModel? = null
    private var productCollectionViewModel: ProductCollectionViewModel? = null
    private var homeFeaturedProductViewModel: HomeFeaturedProductViewModel? = null
    private var touchCountViewModel: TouchCountViewModel? = null
    private var categoryViewModel: CategoryViewModel? = null
    private var favouriteViewModel: ProductFavouriteViewModel? = null
    private var shopViewModel: ShopViewModel? = null
    private var psAppInfoViewModel: AppLoadingViewModel? = null
    private var clearAllDataViewModel: ClearAllDataViewModel? = null
    private var dotsCount = 0
    private var dots: Array<ImageView?>? = null
    private var layoutDone = false
    private var loadingCount = 0
    private var gridLayoutManager: GridLayoutManager? = null
    private var categoryGridLayoutManager: GridLayoutManager? = null
    private val basketMenuItem: MenuItem? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var startDate = Constants.ZERO
    private val endDate = Constants.ZERO
    private var extendedFloatingActionButton: ExtendedFloatingActionButton? = null


    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentMainBinding?>? = null
    private var viewPagerAdapter: AutoClearedValue<ViewPagerAdapter>? = null
    private var categoryIconListAdapter: AutoClearedValue<CategoryIconListAdapter>? = null
    private var discountListAdapter: AutoClearedValue<DiscountListAdapter>? = null
    private var trendingAdapter: AutoClearedValue<ProductHorizontalListAdapter>? = null
    private var latestAdapter: AutoClearedValue<ProductHorizontalListAdapter>? = null
    private var categoryAdapter: AutoClearedValue<TrendingCategoryAdapter>? = null
    private var verticalRowAdapter: AutoClearedValue<ProductCollectionRowAdapter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get()!!.loadingMore = connectivity.isConnected
        setHasOptionsMenu(true)
        //isOnline();
        return binding!!.get()!!.root
    }

    private fun setBasketMenuItemVisible(isVisible: Boolean) {
        if (basketMenuItem != null) {
            basketMenuItem.isVisible = isVisible
        }
    }

    /* @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        inflater.inflate(R.menu.blog_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = menu.findItem(R.id.action_basket);
        MenuItem blogMenuItem = menu.findItem(R.id.action_blog);
        blogMenuItem.setVisible(true);

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.setVisible(true);
            } else {
                basketMenuItem.setVisible(false);
            }
        }
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_blog) {
            navigationController.navigateToBlogList(this!!.requireActivity())
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get()!!.adView.loadAd(adRequest)
            val adRequest2 = AdRequest.Builder()
                    .build()
            binding!!.get()!!.adView2.loadAd(adRequest2)
        } else {
            binding!!.get()!!.adView.visibility = View.GONE
            binding!!.get()!!.adView2.visibility = View.GONE
        }
        extendedFloatingActionButton = binding!!.get()!!.gotoyounew
        extendedFloatingActionButton!!.setOnClickListener { /*Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://www.youtube.com/channel/UCXsOq3SXWaSxeE7zNK9FgYA"));
                intent.setComponent(new ComponentName("com.google.android.youtube","com.google.android.youtube.PlayerActivity"));
                PackageManager manager = getContext().getPackageManager();
                List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
                */
            val intent1 = Intent(Intent.ACTION_VIEW)
            intent1.data = Uri.parse("https://www.youtube.com/channel/UCXsOq3SXWaSxeE7zNK9FgYA")
            startActivity(intent1)
        }
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get()!!.viewAllSliderTextView.setOnClickListener { view: View? -> navigationController.navigateToHomeFilteringActivity(this@MainFragment.activity!!, homeFeaturedProductViewModel!!.productParameterHolder, getString(R.string.menu__featured_product)) }
        binding!!.get()!!.viewAllDiscountTextView.setOnClickListener { view: View? -> navigationController.navigateToHomeFilteringActivity(this@MainFragment.activity!!, homeSearchProductViewModel!!.holder.discountParameterHolder, getString(R.string.menu__discount)) }
        binding!!.get()!!.viewAllTrendingTextView.setOnClickListener { view: View? -> navigationController.navigateToHomeFilteringActivity(this@MainFragment.activity!!, homeTrendingProductViewModel!!.productParameterHolder, getString(R.string.menu__trending_products)) }
        binding!!.get()!!.viewAllTrendingCategoriesTextView.setOnClickListener { view: View? -> navigationController.navigateToCategoryActivity(this@MainFragment.activity!!, Constants.CATEGORY_TRENDING) }
        binding!!.get()!!.viewALlLatestTextView.setOnClickListener { view: View? -> navigationController.navigateToHomeFilteringActivity(this@MainFragment.activity!!, homeLatestProductViewModel!!.productParameterHolder, getString(R.string.menu__latest_product)) }
        binding!!.get()!!.categoryViewAllTextView.setOnClickListener { v: View? -> navigationController.navigateToCategoryActivity(this@MainFragment.activity!!, Constants.CATEGORY) }

        binding!!.get()!!.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                setupSliderPagination()
                if (dotsCount != 0) {
                    for (i in 0 until dotsCount) {
                        dots!![i]!!.setImageDrawable(resources.getDrawable(R.drawable.nonselecteditem_dot))
                    }
                    dots!![position]!!.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initViewModels() {
        homeLatestProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeLatestProductViewModel::class.java)
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel::class.java)
        homeTrendingProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingProductViewModel::class.java)
        homeTrendingCategoryListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingCategoryListViewModel::class.java)
        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
        productCollectionViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductCollectionViewModel::class.java)
        homeFeaturedProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeFeaturedProductViewModel::class.java)
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel::class.java)
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel::class.java)
        psAppInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(AppLoadingViewModel::class.java)
        clearAllDataViewModel = ViewModelProviders.of(this, viewModelFactory).get(ClearAllDataViewModel::class.java)
    }

    override fun initAdapters() {

        /*LatestList*/
        val latestAdapter1 = ProductHorizontalListAdapter(dataBindingComponent, object : ProductHorizontalListAdapter.NewsClickCallback {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@MainFragment.activity!!, product!!)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        }, this)
        latestAdapter = AutoClearedValue(this, latestAdapter1)
        binding!!.get()!!.productList.adapter = latestAdapter1

        /*LatestList*/

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*discountList*/
        val discountListAdapter1 = DiscountListAdapter(dataBindingComponent, object : DiscountListAdapter.NewsClickCallback {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@MainFragment.activity!!, product!!)
            }

            override fun onViewAllClick() {
                navigationController.navigateToHomeFilteringActivity(activity!!, ProductParameterHolder().discountParameterHolder, getString(R.string.menu__discount))
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        })
        discountListAdapter = AutoClearedValue(this, discountListAdapter1)
        binding!!.get()!!.discountList.adapter = discountListAdapter1

        /*discountList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*featuredList*/
        val viewPagerAdapter1 = ViewPagerAdapter(dataBindingComponent, object : ItemClick {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@MainFragment.activity!!, product!!)
            }
        })
        viewPagerAdapter = AutoClearedValue(this, viewPagerAdapter1)
        binding!!.get()!!.viewPager.adapter = viewPagerAdapter1
        binding!!.get()!!.viewPagerCountDots.visibility = View.VISIBLE
        //binding!!.get()!!.viewPager.autoScroll(30000)

        /*After setting the adapter use the timer *//*
        val handler = Handler()
        val Update = Runnable {
            val NUM_PAGES = 4
            if (currentPage === NUM_PAGES - 1) {
                currentPage = 0
            }
            binding!!.get()!!.viewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)*/

        /*val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                binding!!.get()!!.viewPager.post(Runnable {  binding!!.get()!!.viewPager.setCurrentItem(( binding!!.get()!!.viewPager.getCurrentItem() + 1) % id) })
            }
        }
        timer = Timer()
        timer.schedule(timerTask, 3000, 3000)*/



        /*featuredList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*CategoryIconList*/
        val categoryIconListAdapter1 = CategoryIconListAdapter(dataBindingComponent, object : CategoryIconListAdapter.CategoryClickCallback {

            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(category: Category?) {
                categoryViewModel!!.productParameterHolder.catId = category!!.id
                navigationController.navigateToHomeFilteringActivity(this@MainFragment.activity!!, categoryViewModel!!.productParameterHolder, category!!.name)

            }


        }, this)

//                new CategoryIconListAdapter().CategoryClickCallback() {
//            @Override
//            public void onClick(Category category) {
//
//                categoryViewModel.productParameterHolder.catId = category.id;
//                navigationController.navigateToHomeFilteringActivity(SelectedShopFragment.this.getActivity(), categoryViewModel.productParameterHolder, category.name);
//            }
//
//        });
        categoryIconListAdapter = AutoClearedValue(this, categoryIconListAdapter1)
        binding!!.get()!!.categoryIconList.adapter = categoryIconListAdapter1
        //        binding.get().categoryIconList.setNestedScrollingEnabled(false);

        /*CategoryIconList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Latest Product*/
        val trendingAdapter1 = ProductHorizontalListAdapter(dataBindingComponent, object : ProductHorizontalListAdapter.NewsClickCallback {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@MainFragment.activity!!, product!!)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        }, this)
        trendingAdapter = AutoClearedValue(this, trendingAdapter1)
        binding!!.get()!!.trendingList.adapter = trendingAdapter1

        /* Latest Product*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val trendingCategoryAdapter = TrendingCategoryAdapter(dataBindingComponent,
                object : TrendingCategoryAdapter.CategoryClickCallback {
                    @SuppressLint("UseRequireInsteadOfGet")
                    override fun onClick(category: Category?) {
                        categoryViewModel!!.productParameterHolder.catId = category!!.id
                        navigationController.navigateToHomeFilteringActivity(this@MainFragment.activity!!, categoryViewModel!!.productParameterHolder, category.name)
                    }

                }, this)
        categoryAdapter = AutoClearedValue(this, trendingCategoryAdapter)
        binding!!.get()!!.trendingCategoryList.adapter = trendingCategoryAdapter

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val verticalRowAdapter1 = ProductCollectionRowAdapter(dataBindingComponent, object : ProductCollectionRowAdapter.NewsClickCallback {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(product: Product?) {
                navigationController.navigateToDetailActivity(this@MainFragment.activity!!, product!!)
            }

            @SuppressLint("UseRequireInsteadOfGet")
            override fun onViewAllClick(productCollectionHeader: ProductCollectionHeader?) {
                navigationController.navigateToCollectionProductList(this@MainFragment.activity!!, productCollectionHeader!!.id, productCollectionHeader.name, productCollectionHeader.defaultPhoto.imgPath)
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        })
        verticalRowAdapter = AutoClearedValue(this, verticalRowAdapter1)
        binding!!.get()!!.collections.adapter = verticalRowAdapter1
        binding!!.get()!!.collections.isNestedScrollingEnabled = false
    }

    private fun ViewPager.autoScroll(interval: Long) {

        val handler = Handler()
        var scrollPosition = 0

        val runnable = object : Runnable {

            override fun run() {

                /**
                 * Calculate "scroll position" with
                 * adapter pages count and current
                 * value of scrollPosition.
                 */
                val count = adapter?.count ?: 0
                setCurrentItem(scrollPosition++ % count, true)

                handler.postDelayed(this, interval)
            }
        }

        handler.post(runnable)
    }

    private fun replaceLatestData(productList: List<Product>?) {
        latestAdapter!!.get().replace(productList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceCategoryIconList(categoryList: List<Category>?) {
        categoryIconListAdapter!!.get().replace(categoryList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceFeaturedData(featuredProductList: List<Product>?) {
        viewPagerAdapter!!.get().replaceFeaturedList(featuredProductList)
        setupSliderPagination()
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceDiscountList(productList: List<Product>?) {
        discountListAdapter!!.get().replaceDiscount(productList)
        discountListAdapter!!.get().notifyDataSetChanged()
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceTrendingData(productList: List<Product>?) {
        trendingAdapter!!.get().replace(productList)
        trendingAdapter!!.get().notifyDataSetChanged()
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceTrendingCategoryData(categoryList: List<Category>?) {
        categoryAdapter!!.get().replace(categoryList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceCollection(productCollectionHeaders: List<ProductCollectionHeader>?) {
        verticalRowAdapter!!.get().replaceCollectionHeader(productCollectionHeaders)
        binding!!.get()!!.executePendingBindings()
    }

    override fun initData() {
        setShopTouchCount()
        basketData()
        loadProducts()
        deleteHistoryData()
    }

    //Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
    val isOnline: Boolean
        get() {
            val conMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = conMgr.activeNetworkInfo
            if (netInfo == null || !netInfo.isConnected || !netInfo.isAvailable) {
                //Toast.makeText(getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
                return false
            } else {
            }
            return true
        }

    private fun deleteHistoryData() {
        if (connectivity.isConnected) {
            if (startDate == Constants.ZERO) {
                startDate = dateTime
                Utils.setDatesToShared(startDate, endDate, pref)
            }
        } else {
            if (Config.APP_VERSION != versionNo && !force_update) {
                psDialogMsg!!.showInfoDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
        clearAllDataViewModel!!.deleteAllDataData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                when (result.status) {
                    Status.ERROR -> {
                    }
                    Status.SUCCESS -> {
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

    private fun loadProducts() {

        //get favourite post method
        favouriteViewModel!!.favouritePostData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.activity != null) {
                        Utils.psLog(result.status.toString())
                        favouriteViewModel!!.setLoadingState(false)
                    }
                } else if (result.status == Status.ERROR) {
                    if (this.activity != null) {
                        Utils.psLog(result.status.toString())
                        favouriteViewModel!!.setLoadingState(false)
                    }
                }
            }
        })
        shopViewModel!!.setShopObj(Config.API_KEY)
        shopViewModel!!.shopData.observe(this, Observer { result: Resource<Shop?>? ->
            if (result != null) {
                when (result.status) {
                    Status.SUCCESS -> if (result.data != null) {
                        //pref!!.edit().putString(Constants.SHIPPING_ID, result.data.shippingId).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_SHIPPING_TAX, result.data.shippingTaxValue.toString()).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_OVER_ALL_TAX, result.data.overallTaxValue.toString()).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_SHIPPING_TAX_LABEL, result.data.shippingTaxLabel.toString()).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_OVER_ALL_TAX_LABEL, result.data.overallTaxLabel.toString()).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_CASH_ON_DELIVERY, result.data.codEnabled.toString()).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_PAYPAL, result.data.paypalEnabled.toString()).apply()
                        //pref!!.edit().putString(Constants.PAYMENT_STRIPE, result.data.stripeEnabled.toString()).apply()
                        //pref!!.edit().putString(Constants.MESSENGER, result.data.messenger.toString()).apply()
                        //pref!!.edit().putString(Constants.WHATSAPP, result.data.whapsappNo.toString()).apply()
                    }
                    Status.ERROR -> {
                    }
                }
            }
        })

        // Load Latest Product List
        homeLatestProductViewModel!!.setGetProductListByKeyObj(homeLatestProductViewModel!!.productParameterHolder, loginUserId, Config.LOAD_FROM_DB.toString(), homeLatestProductViewModel!!.offset.toString())
        val latest = homeLatestProductViewModel!!.getProductListByKeyData
        latest?.observe(this, Observer { listResource: Resource<List<Product>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            binding!!.get()!!.latestTitleTextView.visibility = View.VISIBLE
                            binding!!.get()!!.viewALlLatestTextView.visibility = View.VISIBLE

                            // Update the data
                            replaceLatestData(listResource.data)
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            binding!!.get()!!.latestTitleTextView.visibility = View.VISIBLE
                            binding!!.get()!!.viewALlLatestTextView.visibility = View.VISIBLE

                            // Update the data
                            replaceLatestData(listResource.data)
                        }
                        homeLatestProductViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR -> {
                        // Error State
                        homeLatestProductViewModel!!.setLoadingState(false)
                        homeLatestProductViewModel!!.forceEndLoading = true
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (homeLatestProductViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    homeLatestProductViewModel!!.forceEndLoading = true
                }
            }
        })
        homeLatestProductViewModel!!.getNextPageProductListByKeyData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    homeLatestProductViewModel!!.setLoadingState(false)
                    homeLatestProductViewModel!!.forceEndLoading = true
                }
            }
        })
        homeLatestProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get()!!.loadingMore = homeLatestProductViewModel!!.isLoading
            Utils.psLog("getLoadingState : start")
            if (loadingState != null && !loadingState) {
                Utils.psLog("getLoadingState end")
            }
        })


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        homeFeaturedProductViewModel!!.setGetProductListByKeyObj(homeFeaturedProductViewModel!!.productParameterHolder, loginUserId, Config.LOAD_FROM_DB.toString(), homeFeaturedProductViewModel!!.offset.toString())
        val featured = homeFeaturedProductViewModel!!.getProductListByKeyData
        featured?.observe(this, Observer { listResource: Resource<List<Product>?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING -> if (listResource.data != null) {
                        fadeIn(binding!!.get()!!.viewPager)
                        binding!!.get()!!.sliderHeaderTextView.visibility = View.VISIBLE
                        binding!!.get()!!.viewAllSliderTextView.visibility = View.VISIBLE
                        replaceFeaturedData(listResource.data)
                    }
                    Status.SUCCESS -> if (listResource.data != null) {
                        fadeIn(binding!!.get()!!.viewPager)
                        binding!!.get()!!.sliderHeaderTextView.visibility = View.VISIBLE
                        binding!!.get()!!.viewAllSliderTextView.visibility = View.VISIBLE
                        replaceFeaturedData(listResource.data)
                        homeFeaturedProductViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR -> {
                        homeFeaturedProductViewModel!!.setLoadingState(false)
                        homeFeaturedProductViewModel!!.forceEndLoading = true
                    }
                    else -> {
                    }
                }
            }
        })
        homeFeaturedProductViewModel!!.getNextPageProductListByKeyData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    homeFeaturedProductViewModel!!.setLoadingState(false)
                    homeFeaturedProductViewModel!!.forceEndLoading = true
                }
            }
        })
        homeFeaturedProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get()!!.loadingMore = homeFeaturedProductViewModel!!.isLoading })

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* CategoryRecyclerView*/categoryViewModel!!.setCategoryListObj(loginUserId, categoryViewModel!!.categoryParameterHolder, Config.LOAD_FROM_DB.toString(), homeSearchProductViewModel!!.offset.toString())
        val categories = categoryViewModel!!.categoryListData
        categories?.observe(this, Observer { listResource: Resource<List<Category>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get()!!.categoryIconList)

//                                // Update the data
                            if (listResource.data.size > 0) {
                                if (listResource.data.size < 9) {
                                    categoryGridLayoutManager = GridLayoutManager(context, 1)
                                    categoryGridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    categoryGridLayoutManager!!.reverseLayout
                                    binding!!.get()!!.categoryIconList.layoutManager = categoryGridLayoutManager
                                } else {
                                    categoryGridLayoutManager = GridLayoutManager(context, 2)
                                    categoryGridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    categoryGridLayoutManager!!.reverseLayout
                                    binding!!.get()!!.categoryIconList.layoutManager = categoryGridLayoutManager
                                }
                                replaceCategoryIconList(listResource.data)
                                binding!!.get()!!.categoryTextView.visibility = View.VISIBLE
                                binding!!.get()!!.categoryViewAllTextView.visibility = View.VISIBLE
                            }
                        }
                    Status.SUCCESS ->                             // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get()!!.categoryIconList)

//                                // Update the data
                            if (listResource.data.size > 0) {
                                if (listResource.data.size < 9) {
                                    categoryGridLayoutManager = GridLayoutManager(context, 1)
                                    categoryGridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    categoryGridLayoutManager!!.reverseLayout
                                    binding!!.get()!!.categoryIconList.layoutManager = categoryGridLayoutManager
                                } else {
                                    categoryGridLayoutManager = GridLayoutManager(context, 2)
                                    categoryGridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    categoryGridLayoutManager!!.reverseLayout
                                    binding!!.get()!!.categoryIconList.layoutManager = categoryGridLayoutManager
                                }
                                replaceCategoryIconList(listResource.data)
                                binding!!.get()!!.categoryTextView.visibility = View.VISIBLE
                                binding!!.get()!!.categoryViewAllTextView.visibility = View.VISIBLE
                            }
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
        categoryViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get()!!.loadingMore = categoryViewModel!!.isLoading })


        /* CategoryRecyclerView*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*DiscountList*/homeSearchProductViewModel!!.setGetProductListByKeyObj(homeSearchProductViewModel!!.holder, loginUserId, Config.LOAD_FROM_DB.toString(), homeSearchProductViewModel!!.offset.toString())
        val discount = homeSearchProductViewModel!!.getProductListByKeyData
        discount?.observe(this, Observer { listResource: Resource<List<Product>?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING -> if (listResource.data != null) {
                        binding!!.get()!!.discountTitleTextView.visibility = View.VISIBLE
                        binding!!.get()!!.viewAllDiscountTextView.visibility = View.VISIBLE
                        replaceDiscountList(listResource.data)
                    }
                    Status.SUCCESS -> if (listResource.data != null) {
                        binding!!.get()!!.discountTitleTextView.visibility = View.VISIBLE
                        binding!!.get()!!.viewAllDiscountTextView.visibility = View.VISIBLE
                        replaceDiscountList(listResource.data)
                        homeSearchProductViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR -> {
                        homeSearchProductViewModel!!.setLoadingState(false)
                        homeSearchProductViewModel!!.forceEndLoading = true
                    }
                    else -> {
                    }
                }
            }
        })
        homeSearchProductViewModel!!.getNextPageProductListByKeyData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    homeSearchProductViewModel!!.setLoadingState(false)
                    homeSearchProductViewModel!!.forceEndLoading = true
                }
            }
        })
        homeSearchProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get()!!.loadingMore = homeSearchProductViewModel!!.isLoading })

        /*DiscountList*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*trendingList*/homeTrendingProductViewModel!!.setGetProductListByKeyObj(homeTrendingProductViewModel!!.productParameterHolder, loginUserId, Config.LOAD_FROM_DB.toString(), homeSearchProductViewModel!!.offset.toString())
        val trending = homeTrendingProductViewModel!!.getProductListByKeyData
       trending!!.observe(this, Observer { listResource: Resource<List<Product>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING -> {
                        binding!!.get()!!.trendingTitleTextView.visibility = View.VISIBLE
                        binding!!.get()!!.viewAllTrendingTextView.visibility = View.VISIBLE
                        //replaceTrendingData(listResource.data!!)
                    }
                    Status.SUCCESS -> {
                        binding!!.get()!!.trendingTitleTextView.visibility = View.VISIBLE
                        binding!!.get()!!.viewAllTrendingTextView.visibility = View.VISIBLE
                        replaceTrendingData(listResource.data!!)
                        homeTrendingProductViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR -> {
                        homeTrendingProductViewModel!!.setLoadingState(false)
                        homeTrendingProductViewModel!!.forceEndLoading = true
                    }
                    else -> {
                    }
                }
            }
        })
        homeTrendingProductViewModel!!.getNextPageProductListByKeyData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    homeTrendingProductViewModel!!.setLoadingState(false)
                    homeTrendingProductViewModel!!.forceEndLoading = true
                }
            }
        })
        homeTrendingProductViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get()!!.loadingMore = homeTrendingProductViewModel!!.isLoading })

        /*trendingList*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*trendingCategoryList*/homeTrendingCategoryListViewModel!!.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel!!.categoryParameterHolder, Config.LOAD_FROM_DB.toString(), homeTrendingCategoryListViewModel!!.offset.toString())
        val trendingCategories = homeTrendingCategoryListViewModel!!.homeTrendingCategoryListData
        trendingCategories?.observe(this, Observer { listResource: Resource<List<Category>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            //fadeIn(binding.get().getRoot());
                            if (listResource.data.isNotEmpty()) {
                                if (listResource.data.size < 5) {
                                    gridLayoutManager = GridLayoutManager(context, 1)
                                    gridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    binding!!.get()!!.trendingCategoryList.layoutManager = gridLayoutManager
                                } else {
                                    gridLayoutManager = GridLayoutManager(context, 2)
                                    gridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    binding!!.get()!!.trendingCategoryList.layoutManager = gridLayoutManager
                                }
                                binding!!.get()!!.trendingCategoriesTitleTextView.visibility = View.VISIBLE
                                binding!!.get()!!.viewAllTrendingCategoriesTextView.visibility = View.INVISIBLE
                                replaceTrendingCategoryData(listResource.data)
                            }
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            if (listResource.data.size > 0) {
                                if (listResource.data.size < 5) {
                                    gridLayoutManager = GridLayoutManager(context, 1)
                                    gridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    binding!!.get()!!.trendingCategoryList.layoutManager = gridLayoutManager
                                } else {
                                    gridLayoutManager = GridLayoutManager(context, 2)
                                    gridLayoutManager!!.orientation = RecyclerView.HORIZONTAL
                                    binding!!.get()!!.trendingCategoryList.layoutManager = gridLayoutManager
                                }
                                binding!!.get()!!.trendingCategoriesTitleTextView.visibility = View.VISIBLE
                                binding!!.get()!!.viewAllTrendingCategoriesTextView.visibility = View.INVISIBLE
                                replaceTrendingCategoryData(listResource.data)
                            }
                        }
                        categoryViewModel!!.setLoadingState(false)
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
        homeTrendingCategoryListViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get()!!.loadingMore = homeTrendingCategoryListViewModel!!.isLoading })
        /*trendingCategoryList*/

        /*Collection List*/
        productCollectionViewModel!!.setProductCollectionHeaderListForHomeObj(Config.COLLECTION_PRODUCT_LIST_LIMIT.toString(), Config.COLLECTION_PRODUCT_LIST_LIMIT.toString(), Config.COLLECTION_PRODUCT_LIST_LIMIT.toString(), homeTrendingCategoryListViewModel!!.offset.toString())
        val productCollection = productCollectionViewModel!!.productCollectionHeaderListDataForHome
        productCollection?.observe(this, Observer { listResource: Resource<List<ProductCollectionHeader>?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            //fadeIn(binding.get().getRoot());

                            // Update the data
                            replaceCollection(listResource.data)
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceCollection(listResource.data)
                        }
                        productCollectionViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        productCollectionViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (productCollectionViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    productCollectionViewModel!!.forceEndLoading = true
                }
            }
        })
        productCollectionViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get()!!.loadingMore = productCollectionViewModel!!.isLoading })
        binding!!.get()!!.categoryIconList.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding!!.get() != null) {
                    if (binding!!.get()!!.categoryIconList != null) {
                        if (binding!!.get()!!.categoryIconList.childCount > 0) {
                            layoutDone = true
                            loadingCount++
                            hideLoading()
                            binding!!.get()!!.categoryIconList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
            }
        })
        binding!!.get()!!.viewPager.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding!!.get() != null && binding!!.get()!!.viewPager != null) {
                    if (binding!!.get()!!.viewPager.childCount > 0) {
                        layoutDone = true
                        loadingCount++
                        hideLoading()
                        binding!!.get()!!.viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        })
        binding!!.get()!!.discountList.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding!!.get() != null && binding!!.get()!!.discountList != null && binding!!.get()!!.discountList.childCount > 0) {
                    layoutDone = true
                    loadingCount++
                    hideLoading()
                    binding!!.get()!!.discountList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.


    override fun onDispatched() {
        if (homeLatestProductViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            val layoutManager = binding!!.get()!!.productList.layoutManager as LinearLayoutManager?
            layoutManager?.scrollToPosition(0)
        }
        if (homeSearchProductViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            val layoutManager = binding!!.get()!!.discountList.layoutManager as GridLayoutManager?
            layoutManager?.scrollToPosition(0)
        }
        if (homeTrendingProductViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            val layoutManager = binding!!.get()!!.trendingList.layoutManager as GridLayoutManager?
            layoutManager?.scrollToPosition(0)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun setupSliderPagination() {

        dotsCount = viewPagerAdapter!!.get().count
        if (dotsCount > 0 && dots == null) {
            dots = arrayOfNulls(dotsCount)
            if (binding!!.get() != null) {
                if (binding!!.get()!!.viewPagerCountDots.childCount > 0) {
                    binding!!.get()!!.viewPagerCountDots.removeAllViewsInLayout()
                }
            }
            for (i in 0 until dotsCount) {
                dots!![i] = ImageView(context)
                dots!![i]!!.setImageDrawable(resources.getDrawable(R.drawable.nonselecteditem_dot))
                val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(4, 0, 4, 0)
                binding!!.get()!!.viewPagerCountDots.addView(dots!![i], params)
            }
            dots!![0]!!.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))
        }
    }

    private fun hideLoading() {
        if (loadingCount == 3 && layoutDone) {
            binding!!.get()!!.loadingView.visibility = View.GONE
            binding!!.get()!!.loadHolder.visibility = View.GONE
        }
    }

    private fun setShopTouchCount() {
        touchCountViewModel!!.setTouchCountPostDataObj(loginUserId, "", Constants.SHOP)
        touchCountViewModel!!.touchCountPostData.observe(this, Observer { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.activity != null) {
                        Utils.psLog(result.status.toString())
                    }
                } else if (result.status == Status.ERROR) {
                    if (this.activity != null) {
                        Utils.psLog(result.status.toString())
                    }
                }
            }
        })
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

    private val dateTime: String
        private get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA)
            val date = Date()
            return dateFormat.format(date)
        }

    override fun onResume() {
        loadLoginUserId()
        super.onResume()
    }
}