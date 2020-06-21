package com.dewonderstruck.apps.ashx0.ui.product.detail

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.BottomBoxBasketAndBuyBinding
import com.dewonderstruck.apps.ashx0.databinding.FragmentProductDetailBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.product.adapter.ProductHorizontalListAdapter
import com.dewonderstruck.apps.ashx0.ui.product.detail.ProductDetailFragment
import com.dewonderstruck.apps.ashx0.ui.product.detail.adapter.*
import com.dewonderstruck.apps.ashx0.ui.product.detail.adapter.ProductAttributeHeaderAdapter.HeaderProductClickCallBack
import com.dewonderstruck.apps.ashx0.ui.product.detail.adapter.ProductColorAdapter.ColorClickCallBack
import com.dewonderstruck.apps.ashx0.ui.product.detail.adapter.ProductDetailSpecsAdapter.SpecsClickCallBack
import com.dewonderstruck.apps.ashx0.ui.product.detail.adapter.ProductDetailTagAdapter.SimilarProductClickCallBack
import com.dewonderstruck.apps.ashx0.utils.*
import com.dewonderstruck.apps.ashx0.viewmodel.image.ImageViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.*
import com.dewonderstruck.apps.ashx0.viewobject.*
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import com.dewonderstruck.apps.ashx0.viewobject.holder.TabObject
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.like.LikeButton
import com.like.OnLikeListener
import java.util.*

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class ProductDetailFragment() : PSFragment(), DiffUtilDispatchedInterface {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var dotsCount = 0
    private var dots: Array<ImageView?>?
    private var num = 1
    private var minOrder = 0
    private var imageViewModel: ImageViewModel? = null
    private var productDetailViewModel: ProductDetailViewModel? = null
    private var productColorViewModel: ProductColorViewModel? = null
    private var productSpecsViewModel: ProductSpecsViewModel? = null
    private var productRelatedViewModel: ProductRelatedViewModel? = null
    private var productAttributeHeaderViewModel: ProductAttributeHeaderViewModel? = null
    private var productAttributeDetailViewModel: ProductAttributeDetailViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private var favouriteViewModel: ProductFavouriteViewModel? = null
    private var touchCountViewModel: TouchCountViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var additionPrice: String? = null
    private val productParameterHolder = ProductParameterHolder()
    private var available = true
    private var twist = false

    @VisibleForTesting
    private var pagerAdapter: AutoClearedValue<ProductDetailListPagerAdapter>? = null
    private var binding: AutoClearedValue<FragmentProductDetailBinding?>? = null
    var adapter: AutoClearedValue<ProductDetailListPagerAdapter>? = null
    private var colorAdapter: AutoClearedValue<ProductColorAdapter>? = null
    private var specsAdapter: AutoClearedValue<ProductDetailSpecsAdapter>? = null
    private var tabAdapter: AutoClearedValue<ProductDetailTagAdapter>? = null
    private var headerAdapter: AutoClearedValue<ProductAttributeHeaderAdapter>? = null
    private var tabObjectList: AutoClearedValue<MutableList<TabObject>>? = null
    private var relatedAdapter: AutoClearedValue<ProductHorizontalListAdapter>? = null
    private var mBottomSheetDialog: AutoClearedValue<BottomSheetDialogExpanded>? = null
    private var bottomBoxLayoutBinding: AutoClearedValue<BottomBoxBasketAndBuyBinding?>? = null
    var imageViewPager: AutoClearedValue<ViewPager?>? = null
    var pageIndicatorLayout: AutoClearedValue<LinearLayout?>? = null

    /*AutoClearedValue<Button> addToCartButton;*/
    var buyNowButton: AutoClearedValue<Button>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_product_detail, container, false, dataBindingComponent) as FragmentProductDetailBinding
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        val tabObjectList1: MutableList<TabObject> = ArrayList()
        tabObjectList = AutoClearedValue(this, tabObjectList1)
        return binding!!.get()!!.root
    }

    override fun onDispatched() {}

    @SuppressLint("ClickableViewAccessibility")
    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        hideFloatingButton()
        if (context != null) {

            //mBottomSheetDialog2.setCanceledOnTouchOutside(false);
            mBottomSheetDialog = AutoClearedValue(this, BottomSheetDialogExpanded((context)!!))
            val bottomBoxLayoutBinding: BottomBoxBasketAndBuyBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_box_basket_and_buy, null, false, dataBindingComponent)
            this.bottomBoxLayoutBinding = AutoClearedValue(this, bottomBoxLayoutBinding)
            mBottomSheetDialog!!.get().setContentView(this.bottomBoxLayoutBinding!!.get()!!.root)
        }
        if ((imageViewPager != null) && (imageViewPager!!.get() != null) && (pageIndicatorLayout!!.get() != null)) {
            imageViewPager!!.get()!!.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    if (pageIndicatorLayout!!.get() != null) {
                        setupSliderPagination()
                    }
                    for (i in 0 until dotsCount) {
                        if (dots != null && dots!!.size > i) {
                            dots!![i]!!.setImageDrawable(resources.getDrawable(R.drawable.nonselecteditem_dot))
                        }
                    }
                    if (dots != null && dots!!.size > position) {
                        dots!![position]!!.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
        binding!!.get()!!.mainFloatingActionButton.setOnClickListener({ v: View? ->
            twist = Utils.twistFab(v, !twist)

//            Toast.makeText(getContext(),shopPhoneNumber,Toast.LENGTH_SHORT).show();
            if (messenger!!.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding!!.get()!!.whatsappFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.whatsAppTextView)
                    Utils.showFab(binding!!.get()!!.phoneFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.phoneProfile)
                } else {
                    Utils.hideFab(binding!!.get()!!.whatsappFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.whatsAppTextView)
                    Utils.hideFab(binding!!.get()!!.phoneFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.phoneProfile)
                }
            } else if (whatsappNo!!.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding!!.get()!!.messengerFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.messengerTextView)
                    Utils.showFab(binding!!.get()!!.phoneFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.phoneProfile)
                } else {
                    Utils.hideFab(binding!!.get()!!.messengerFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.messengerTextView)
                    Utils.hideFab(binding!!.get()!!.phoneFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.phoneProfile)
                }
            } else if (shopPhoneNumber!!.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding!!.get()!!.messengerFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.messengerTextView)
                    Utils.showFab(binding!!.get()!!.whatsappFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.whatsAppTextView)
                } else {
                    Utils.hideFab(binding!!.get()!!.messengerFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.messengerTextView)
                    Utils.hideFab(binding!!.get()!!.whatsappFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.whatsAppTextView)
                }
            } else {
                if (twist) {
                    Utils.showFab(binding!!.get()!!.messengerFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.whatsappFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.phoneFloatingActionButton)
                    Utils.showFab(binding!!.get()!!.messengerTextView)
                    Utils.showFab(binding!!.get()!!.whatsAppTextView)
                    //Utils.showFab(binding.get().phoneTextView);
                } else {
                    Utils.hideFab(binding!!.get()!!.messengerFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.whatsappFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.phoneFloatingActionButton)
                    Utils.hideFab(binding!!.get()!!.messengerTextView)
                    Utils.hideFab(binding!!.get()!!.whatsAppTextView)
                    //Utils.hideFab(binding.get().phoneTextView);
                }
            }
        })
        binding!!.get()!!.messengerFloatingActionButton.setOnClickListener({ v: View? ->
            try {
                val intent: Intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                intent.setPackage("com.facebook.orca")
                intent.setData(Uri.parse("https://m.me/" + messenger))
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(getActivity(), getString(R.string.product_detail__install_messenger_app), Toast.LENGTH_SHORT).show()
            }
        })
        binding!!.get()!!.whatsappFloatingActionButton.setOnClickListener({ v: View? ->
            try {
                val sendIntent: Intent = Intent("android.intent.action.MAIN")
                sendIntent.setComponent(ComponentName("com.whatsapp", "com.whatsapp.Conversation"))
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(whatsappNo) + "@s.whatsapp.net") //phone number without "+" prefix
                startActivity(sendIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(getActivity(), getString(R.string.product_detail__install_whatsapp_app), Toast.LENGTH_SHORT).show()
            }
        })
        binding!!.get()!!.starTextView.setOnClickListener({ v: View? -> navigationController!!.navigateToRatingList((getActivity())!!, productDetailViewModel!!.productId) })
        binding!!.get()!!.ratingBar.setOnTouchListener({ v: View?, event: MotionEvent ->
            if (event.getAction() == MotionEvent.ACTION_UP) {
                navigationController!!.navigateToRatingList((getActivity())!!, productDetailViewModel!!.productId)
            }
            true
        })

        /*addToCartButton.get().setOnClickListener(view -> {

            if (available) {
                productDetailViewModel.isAddtoCart = true;

                bottomBoxLayoutBinding.get().lowestButton.setText(getString(R.string.product_detail__add_to_busket));

                mBottomSheetDialog.get().show();


            } else {
                psDialogMsg.showWarningDialog(getString(R.string.product_detail__not_available), getString(R.string.app__ok));
                psDialogMsg.show();
            }


        });
*/buyNowButton!!.get().setOnClickListener(View.OnClickListener {
            if (available) {
                //productDetailViewModel.isAddtoCart = false;
                //bottomBoxLayoutBinding.get().lowestButton.setText(getString(R.string.product_detail__buy));
                //mBottomSheetDialog.get().show();
                val url = binding!!.get()!!.product.productLink
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                //i.setData(Uri.parse(url));
                //requireContext().startActivity(i);
                if (i.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(i)
                } else {
                    psDialogMsg!!.showWarningDialog(getString(R.string.product_detail__error_browser), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                }
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.product_detail__not_available), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        })

//        buyNowButton.get().setOnClickListener(view -> {
//
//            if (available) {
//                //productDetailViewModel.isAddtoCart = false;
//                //bottomBoxLayoutBinding.get().lowestButton.setText(getString(R.string.product_detail__buy));
//                //mBottomSheetDialog.get().show();
//                String url = binding.get().getProduct().productLink;
//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                //i.setData(Uri.parse(url));
//                getContext().startActivity(i);
//            } else {
//                psDialogMsg.showWarningDialog(getString(R.string.product_detail__not_available), getString(R.string.app__ok));
//                psDialogMsg.show();
//            }
//
//        });
        binding!!.get()!!.seeAllFactButton.setOnClickListener({ view: View? -> navigationController!!.navigateToTermsAndConditionsActivity((getActivity())!!, Constants.SHOP_TERMS) })
        binding!!.get()!!.refundPolicyButton.setOnClickListener({ view: View? -> navigationController!!.navigateToTermsAndConditionsActivity((getActivity())!!, Constants.SHOP_REFUND) })
        binding!!.get()!!.phoneFloatingActionButton.setOnClickListener({ v: View? ->
            if (!(shopPhoneNumber!!.trim { it <= ' ' }.isEmpty() || (shopPhoneNumber!!.trim { it <= ' ' } == "-"))) {
                Utils.callPhone(this@ProductDetailFragment, shopPhoneNumber)
            }
        })
    }

    private fun hideFloatingButton() {
        Utils.hideFirstFab(binding!!.get()!!.messengerFloatingActionButton)
        Utils.hideFirstFab(binding!!.get()!!.whatsappFloatingActionButton)
        Utils.hideFirstFab(binding!!.get()!!.phoneFloatingActionButton)
        Utils.hideFab(binding!!.get()!!.messengerTextView)
        Utils.hideFab(binding!!.get()!!.whatsAppTextView)
        //Utils.hideFab(binding.get().phoneTextView);
    }

    private fun setupSliderPagination() {
        dotsCount = pagerAdapter!!.get().count
        if (dotsCount > 0) {
            dots = arrayOfNulls(dotsCount)
            if (pageIndicatorLayout!!.get() != null) {
                if (pageIndicatorLayout!!.get()!!.childCount > 0) {
                    pageIndicatorLayout!!.get()!!.removeAllViewsInLayout()
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
                pageIndicatorLayout!!.get()!!.addView(dots!![i], params)
            }
            val currentItem = imageViewPager!!.get()!!.currentItem
            if (currentItem > 0) {
                dots!![currentItem]!!.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))
            } else {
                dots!![0]!!.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))
            }
        }
        if (dotsCount == 1) {
            pageIndicatorLayout!!.get()!!.visibility = View.INVISIBLE
        } else {
            pageIndicatorLayout!!.get()!!.visibility = View.VISIBLE
        }
    }

    override fun initViewModels() {
        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel::class.java)
        productDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductDetailViewModel::class.java)
        productColorViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductColorViewModel::class.java)
        productSpecsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductSpecsViewModel::class.java)
        productRelatedViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductRelatedViewModel::class.java)
        productAttributeHeaderViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductAttributeHeaderViewModel::class.java)
        productAttributeDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductAttributeDetailViewModel::class.java)
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel::class.java)
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel::class.java)
    }

    override fun initAdapters() {
        //view pager slide
        val pagerAdapter = ProductDetailListPagerAdapter(dataBindingComponent, ProductDetailListPagerAdapter.ImageClickCallback { view: View?, obj: Image?, position: Int -> navigationController!!.navigateToGalleryActivity((getActivity())!!, Constants.IMAGE_TYPE_PRODUCT, productDetailViewModel!!.productId) })
        this.pagerAdapter = AutoClearedValue(this, pagerAdapter)
        adapter = AutoClearedValue(this, this.pagerAdapter!!.get())
        if (imageViewPager != null) {
            imageViewPager!!.get()!!.adapter = adapter!!.get()
        }

        //color
        val nvcolorAdapter = ProductColorAdapter(dataBindingComponent,
                ColorClickCallBack { productColor: ProductColor?, seletedColorId: String?, selectetColorValue: String? ->
                    productColorViewModel!!.colorSelectId = (seletedColorId)!!
                    productColorViewModel!!.colorSelectValue = (selectetColorValue)!!
                    productColorViewModel!!.proceededColorListData = processDataList(productColorViewModel!!.proceededColorListData)
                    replaceProductColorData(productColorViewModel!!.proceededColorListData)
                })
        colorAdapter = AutoClearedValue(this, nvcolorAdapter)
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding!!.get()!!.colorRecyclerView.layoutManager = layoutManager
        binding!!.get()!!.colorRecyclerView.adapter = colorAdapter!!.get()


        //color in popup
        val layoutManager2 = FlexboxLayoutManager(context)
        layoutManager2.flexDirection = FlexDirection.ROW
        layoutManager2.justifyContent = JustifyContent.FLEX_START
        bottomBoxLayoutBinding!!.get()!!.colorRecycler.layoutManager = layoutManager2
        bottomBoxLayoutBinding!!.get()!!.colorRecycler.adapter = colorAdapter!!.get()

        //tab layout
        val tabAdapter = ProductDetailTagAdapter(dataBindingComponent,
                SimilarProductClickCallBack { tabObject: TabObject, selectedTabId: String? ->
                    when (tabObject.field_name) {
                        Constants.CATEGORY -> {
                            productParameterHolder.resetTheHolder()
                            productParameterHolder.catId = tabObjectList!!.get().get(0).tag_id
                            navigationController!!.navigateToHomeFilteringActivity((getActivity())!!, productParameterHolder, tabObjectList!!.get().get(0).tag_name)
                        }
                        Constants.SUBCATEGORY -> {
                            productParameterHolder.resetTheHolder()
                            productParameterHolder.catId = tabObjectList!!.get().get(0).tag_id
                            productParameterHolder.subCatId = tabObjectList!!.get().get(1).tag_id
                            navigationController!!.navigateToHomeFilteringActivity((getActivity())!!, productParameterHolder, tabObjectList!!.get().get(1).tag_name)
                        }
                        Constants.PRODUCT_TAG -> {
                            productParameterHolder.resetTheHolder()
                            productParameterHolder.search_term = tabObject.tag_name
                            navigationController!!.navigateToHomeFilteringActivity((getActivity())!!, productParameterHolder, tabObject.tag_name)
                        }
                    }
                })
        this.tabAdapter = AutoClearedValue(this, tabAdapter)
        binding!!.get()!!.tabRecyclerView.adapter = tabAdapter

        //specs
        val specsAdapter = ProductDetailSpecsAdapter(dataBindingComponent, SpecsClickCallBack { productSpecs: ProductSpecs? -> })
        this.specsAdapter = AutoClearedValue(this, specsAdapter)
        binding!!.get()!!.factsRecyclerView.adapter = specsAdapter

        //header attribute
        val headerAdapter: ProductAttributeHeaderAdapter = object : ProductAttributeHeaderAdapter(dataBindingComponent, productAttributeHeaderViewModel!!.basketItemHolderHashMap,
                HeaderProductClickCallBack { productAttributeDetail: ProductAttributeDetail ->
                    productAttributeHeaderViewModel!!.productAttributeDetail = productAttributeDetail
                    if ((productAttributeDetail.id == "-1")) {
                        productAttributeHeaderViewModel!!.basketItemHolderHashMap.remove(productAttributeDetail.headerId)
                    } else {
                        productAttributeHeaderViewModel!!.basketItemHolderHashMap.put(productAttributeDetail.headerId, productAttributeDetail.name)
                    }
                    additionPrice = productAttributeDetail.additionalPrice
                    productAttributeHeaderViewModel!!.attributeHeaderHashMap.put(productAttributeDetail.headerId, additionPrice!!.toInt())
                    productAttributeHeaderViewModel!!.priceAfterAttribute = productAttributeHeaderViewModel!!.price
                    productAttributeHeaderViewModel!!.originalPriceAfterAttribute = productAttributeHeaderViewModel!!.originalPrice
                    for (item: Int in productAttributeHeaderViewModel!!.attributeHeaderHashMap.values) {
                        productAttributeHeaderViewModel!!.priceAfterAttribute += item
                        productAttributeHeaderViewModel!!.originalPriceAfterAttribute += item
                    }
                    if ((binding != null
                                    ) && (binding!!.get() != null
                                    ) && (binding!!.get()!!.priceTextView != null)) {
                        binding!!.get()!!.priceTextView.setText(Utils.format(productAttributeHeaderViewModel!!.priceAfterAttribute.toDouble()).toString())
                        binding!!.get()!!.originalPriceTextView.setText(getString(R.string.product_detail__original_price, productDetailViewModel!!.currencySymbol, Utils.format(productAttributeHeaderViewModel!!.originalPriceAfterAttribute.toDouble()).toString()))
                    }
                    if ((bottomBoxLayoutBinding != null
                                    ) && (bottomBoxLayoutBinding!!.get() != null
                                    ) && (bottomBoxLayoutBinding!!.get()!!.priceTextView != null)) {
                        bottomBoxLayoutBinding!!.get()!!.priceTextView.setText(Utils.format(productAttributeHeaderViewModel!!.priceAfterAttribute.toDouble()).toString())
                        bottomBoxLayoutBinding!!.get()!!.originalPriceTextView.setText(getString(R.string.product_detail__original_price, productDetailViewModel!!.currencySymbol, Utils.format(productAttributeHeaderViewModel!!.originalPriceAfterAttribute.toDouble()).toString()))
                    }
                }) {}
        this.headerAdapter = AutoClearedValue(this, headerAdapter)

        //spinner in popup
        bottomBoxLayoutBinding!!.get()!!.attributeHeaderRecycler.adapter = headerAdapter
        val homeScreenAdapter1 = ProductHorizontalListAdapter(dataBindingComponent, object : ProductHorizontalListAdapter.NewsClickCallback {
            override fun onClick(product: Product?) {
                navigationController!!.navigateToDetailActivity((this@ProductDetailFragment.activity)!!, (product)!!)
                //                if (ProductDetailFragment.this.getActivity() != null) {
//                    ProductDetailFragment.this.getActivity().finish();
//                }
            }

            override fun onFavLikeClick(product: Product?, likeButton: LikeButton?) {
                favFunction(product, likeButton)
            }

            override fun onFavUnlikeClick(product: Product?, likeButton: LikeButton?) {
                unFavFunction(product, likeButton)
            }
        })
        relatedAdapter = AutoClearedValue(this, homeScreenAdapter1)
        binding!!.get()!!.alsoBuyRecyclerView.adapter = homeScreenAdapter1
    }

    override fun initData() {
        try {
            if (activity != null) {
                if (activity!!.intent.extras != null) {
                    productDetailViewModel!!.productId = (activity!!.intent.extras!!.getString(Constants.PRODUCT_ID))!!
                    productDetailViewModel!!.historyFlag = (activity!!.intent.extras!!.getString(Constants.HISTORY_FLAG))!!
                    productDetailViewModel!!.basketFlag = (activity!!.intent.extras!!.getString(Constants.BASKET_FLAG))!!
                    if (productDetailViewModel!!.basketFlag != null && (productDetailViewModel!!.basketFlag == Constants.ONE)) {
                        productDetailViewModel!!.price = (activity!!.intent.extras!!.getString(Constants.PRODUCT_PRICE))!!
                        productDetailViewModel!!.attributes = (activity!!.intent.extras!!.getString(Constants.PRODUCT_ATTRIBUTE))!!
                        productDetailViewModel!!.count = (activity!!.intent.extras!!.getString(Constants.PRODUCT_COUNT))!!
                        productDetailViewModel!!.colorId = (activity!!.intent.extras!!.getString(Constants.PRODUCT_SELECTED_COLOR))!!
                        productDetailViewModel!!.basketId = activity!!.intent.extras!!.getInt(Constants.BASKET_ID)
                        if (productDetailViewModel!!.colorId != null) {
                            productColorViewModel!!.colorSelectId = productDetailViewModel!!.colorId
                        }
                        val attribute = productDetailViewModel!!.attributes
                        productAttributeHeaderViewModel!!.basketItemHolderHashMap = Gson().fromJson(attribute, object : TypeToken<HashMap<String?, String?>?>() {}.type)
                        headerAdapter!!.get().basketItemHolderHashMap = productAttributeHeaderViewModel!!.basketItemHolderHashMap
                    }
                }
            }
        } catch (e: Exception) {
            Utils.psErrorLog("", e)
        }
        LoadData()

        //check floating button action
        if (messenger!!.isEmpty() && whatsappNo!!.isEmpty()) {
            Utils.hideFirstFab(binding!!.get()!!.mainFloatingActionButton)
        }

        //get favourite post method
        favouriteViewModel!!.favouritePostData.observe(this, Observer<Resource<Boolean?>> { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status === Status.SUCCESS) {
                    if (getActivity() != null) {
                        Utils.psLog(result.status.toString())
                        favouriteViewModel!!.setLoadingState(false)
                    }
                } else if (result.status === Status.ERROR) {
                    if (getActivity() != null) {
                        Utils.psLog(result.status.toString())
                        favouriteViewModel!!.setLoadingState(false)
                    }
                }
            }
        })
        basketViewModel!!.basketSavedData.observe(this, Observer<Resource<Boolean?>> { resourse: Resource<Boolean?>? ->
            if (resourse != null) {
                if (resourse.status === Status.SUCCESS) {
                    if (basketViewModel!!.isDirectCheckout) {
                        basketViewModel!!.isDirectCheckout = false
                        navigationController!!.navigateToBasketList((getActivity())!!)

//                        bottomBoxLayoutBinding.get().qtyEditText.setText(Constants.ONE);
                        productDetailViewModel!!.count = Constants.ZERO

//                        num = 1;
//                        productColorViewModel.colorSelectId = "";
//                        productColorViewModel.colorSelectValue = "";
//                        productAttributeHeaderViewModel.basketItemHolderHashMap.clear();
//                        productAttributeHeaderViewModel.attributeHeaderHashMap.clear();
                    } else {
                        if (getContext() != null) {
                            psDialogMsg!!.showSuccessDialog(getString(R.string.product_detail__successfully_added), getString(R.string.app__ok))
                            psDialogMsg!!.show()
                        }
                    }
                } else {
                    psDialogMsg!!.showWarningDialog(getString(R.string.error), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                }
            }
        })


        //set and get basket list
        basketViewModel!!.setBasketListObj()
        basketViewModel!!.allBasketList.observe(this, Observer<List<Basket?>> { resourse: List<Basket?>? ->
            if (resourse != null) {
                basketViewModel!!.basketCount = resourse.size
                if (resourse.size > 0) {
                    setBasketMenuItemVisible(true)
                } else {
                    setBasketMenuItemVisible(false)
                }
            }
        })

        //get touch count post method
        touchCountViewModel!!.touchCountPostData.observe(this, Observer<Resource<Boolean?>> { result: Resource<Boolean?>? ->
            if (result != null) {
                if (result.status === Status.SUCCESS) {
                    if (getActivity() != null) {
                        Utils.psLog(result.status.toString())
                    }
                } else if (result.status === Status.ERROR) {
                    if (getActivity() != null) {
                        Utils.psLog(result.status.toString())
                    }
                }
            }
        })
    }

    private fun showOrHideSpecs() {
        if (productSpecsViewModel!!.isSpecsData) {
            binding!!.get()!!.factsRecyclerView.visibility = View.VISIBLE
            binding!!.get()!!.detailFactTextView.visibility = View.VISIBLE
        } else {
            binding!!.get()!!.factsRecyclerView.visibility = View.GONE
            binding!!.get()!!.detailFactTextView.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loadLoginUserId()
        if (loginUserId != null) {
            productDetailViewModel!!.setProductDetailObj(productDetailViewModel!!.productId, productDetailViewModel!!.historyFlag, loginUserId!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Constants.REQUEST_CODE__PRODUCT_FRAGMENT
                        && resultCode == Constants.RESULT_CODE__REFRESH_COMMENT_LIST)) {
            Utils.psLog("Here")
            productDetailViewModel!!.setProductDetailObj((data!!.getStringExtra(Constants.PRODUCT_ID))!!, productDetailViewModel!!.historyFlag, (loginUserId)!!)
        }
    }

    private fun LoadData() {
        // Load image
        val news: LiveData<Resource<List<Image>?>> = imageViewModel!!.imageListLiveData
        news?.observe(this, Observer { listResource: Resource<List<Image>?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get()!!.getRoot())

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
                        imageViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        imageViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {
                imageViewModel!!.setLoadingState(false)
                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })

        // Load Product detail
        productDetailViewModel!!.setProductDetailObj(productDetailViewModel!!.productId, productDetailViewModel!!.historyFlag, (loginUserId)!!)
        setTouchCount()
        val productDetail: LiveData<Resource<Product?>> = productDetailViewModel!!.productDetailData
        productDetail?.observe(this, Observer { listResource: Resource<Product?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get()!!.getRoot())
                            productDetailViewModel!!.productContainer = listResource.data
                            headerAdapter!!.get().currencySymbol = listResource.data.currencySymbol
                            productDetailViewModel!!.currencySymbol = listResource.data.currencySymbol
                            productColorViewModel!!.setProductColorListObj(productDetailViewModel!!.productId)
                            productSpecsViewModel!!.setProductSpecsListObj(productDetailViewModel!!.productId)
                            productAttributeHeaderViewModel!!.setProductAttributeHeaderListObj(productDetailViewModel!!.productId)
                            replaceProductDetailData(listResource.data)
                            bindingRatingData(listResource.data)
                            bindingFavoriteData(listResource.data)
                            bindingDescData(listResource.data.description)
                            bindProductDetailInfo(listResource.data)

                            // Load Related Data
                            imageViewModel!!.setImageParentId(Constants.IMAGE_TYPE_PRODUCT, productDetailViewModel!!.productId)
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            productDetailViewModel!!.productContainer = listResource.data
                            headerAdapter!!.get().currencySymbol = listResource.data.currencySymbol
                            productDetailViewModel!!.currencySymbol = listResource.data.currencySymbol
                            productColorViewModel!!.setProductColorListObj(productDetailViewModel!!.productId)
                            productSpecsViewModel!!.setProductSpecsListObj(productDetailViewModel!!.productId)
                            productAttributeHeaderViewModel!!.setProductAttributeHeaderListObj(productDetailViewModel!!.productId)

                            // Update the data
                            replaceProductDetailData(listResource.data)
                            setTagData(listResource.data)
                            bindingRatingData(listResource.data)
                            bindingFavoriteData(listResource.data)
                            bindProductDetailInfo(listResource.data)
                            productRelatedViewModel!!.setProductRelatedListObj(productDetailViewModel!!.productId, listResource.data.catId)
                        }
                        productDetailViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->
                        // Error State
                        productDetailViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
                // productDetailViewModel.isProductDetailData=true;
            } else {

                //productDetailViewModel.isProductDetailData=false;
                productDetailViewModel!!.setLoadingState(false)
                // Init Object or Empty Data
            }
        })

        // Load Product related
        val productRelated: LiveData<Resource<List<Product>>> = productRelatedViewModel!!.productRelatedData
        productRelated?.observe(this, Observer { listResource: Resource<List<Product>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get()!!.getRoot())

                            // Update the data
                            replaceProductRelatedData(listResource.data)
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceProductRelatedData(listResource.data)
                        }
                        productRelatedViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        productRelatedViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {
                productRelatedViewModel!!.setLoadingState(false)
                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })

        //load product color
        val productColor: LiveData<List<ProductColor>> = productColorViewModel!!.productColorData
        productColor?.observe(this, Observer { listResource: List<ProductColor>? ->
            if (listResource != null && listResource.size > 0) {
                productColorViewModel!!.proceededColorListData = processDataList(listResource)
                replaceProductColorData(productColorViewModel!!.proceededColorListData)
                productColorViewModel!!.isColorData = true
            } else {
                productColorViewModel!!.isColorData = false
            }
            showOrHideColor()
        })


        //load product specs
        val productSpecs: LiveData<List<ProductSpecs>> = productSpecsViewModel!!.productSpecsListData
        productSpecs?.observe(this, Observer { listResource: List<ProductSpecs>? ->
            if (listResource != null && listResource.size > 0) {
                replaceProductSpecsData(listResource)
                productSpecsViewModel!!.isSpecsData = true
            } else {
                productSpecsViewModel!!.isSpecsData = false
            }
            showOrHideSpecs()
        })


        //load product header
        val productHeader: LiveData<List<ProductAttributeHeader>> = productAttributeHeaderViewModel!!.productAttributeHeaderListData
        productHeader?.observe(this, Observer { listResource: List<ProductAttributeHeader>? ->
            if (listResource != null && listResource.size > 0) {
                replaceProductAttributeHeaderData(listResource)
                productAttributeHeaderViewModel!!.headerIdList.clear()
                for (i in listResource.indices) {
                    productAttributeHeaderViewModel!!.headerIdList.add(listResource.get(i).id)
                }
                productAttributeHeaderViewModel!!.isHeaderData = true
            } else {
                productAttributeHeaderViewModel!!.isHeaderData = false
            }
        })

        //load product header detail
        val productHeaderDetail: LiveData<List<ProductAttributeDetail?>> = productAttributeDetailViewModel!!.productAttributeDetailListData
        productHeaderDetail?.observe(this, Observer { listResource: List<ProductAttributeDetail?>? ->
            if (listResource != null && listResource.size > 0) {
                replaceProductAttributeDetailData()
            }
        })
    }

    private fun bindingDescData(description: String) {
        if (description.isEmpty()) {
            binding!!.get()!!.desc.visibility = View.GONE
            binding!!.get()!!.descUpDownImageView.visibility = View.GONE
            binding!!.get()!!.view96.visibility = View.GONE
            binding!!.get()!!.descTextView.visibility = View.GONE
        } else {
            binding!!.get()!!.desc.visibility = View.VISIBLE
            binding!!.get()!!.descUpDownImageView.visibility = View.VISIBLE
            binding!!.get()!!.view96.visibility = View.VISIBLE
            binding!!.get()!!.descTextView.text = description
        }
    }

    private fun showOrHideColor() {
        if (productColorViewModel!!.isColorData) {
            binding!!.get()!!.cangetColorTextView.visibility = View.VISIBLE
            binding!!.get()!!.colorRecyclerView.visibility = View.VISIBLE
        } else {
            binding!!.get()!!.cangetColorTextView.visibility = View.GONE
            binding!!.get()!!.colorRecyclerView.visibility = View.GONE
        }
    }

    private fun bindProductDetailInfo(product: Product?) {
        if ((product!!.minimumOrder == null) || (product.minimumOrder == "0") || product.minimumOrder.isEmpty()) {
            binding!!.get()!!.productMinOrderValueTextView.visibility = View.GONE
            binding!!.get()!!.productMinOrderTextView.visibility = View.GONE
        } else {
            binding!!.get()!!.productMinOrderValueTextView.text = product.minimumOrder
        }
        if ((product.productUnit == null) || (product.productUnit == "0") || product.productUnit.isEmpty()) {
            binding!!.get()!!.productUnitValueTextView.visibility = View.GONE
            binding!!.get()!!.productUnitTextView.visibility = View.GONE
        } else {
            binding!!.get()!!.productUnitValueTextView.text = product.productUnit
        }
        if ((product.productMeasurement == null) || (product.productMeasurement == "0") || product.productMeasurement.isEmpty()) {
            binding!!.get()!!.productMeasurementTextView.visibility = View.GONE
            binding!!.get()!!.productMeasurementValueTextView.visibility = View.GONE
        } else {
            binding!!.get()!!.productMeasurementValueTextView.text = product.productMeasurement
        }
        if ((product.shippingCost == null) || (product.shippingCost == "0") || product.shippingCost.isEmpty()) {
            binding!!.get()!!.productShippingCostTextView.visibility = View.GONE
            binding!!.get()!!.productShippingCostValueTextView.visibility = View.GONE
        } else {
            val shippingCostString = product.currencySymbol + Constants.SPACE_STRING + product.shippingCost
            binding!!.get()!!.productShippingCostValueTextView.text = shippingCostString
        }
    }

    private fun bindingFavoriteData(product: Product?) {
        if ((product!!.isFavourited == Constants.ONE)) {
            binding!!.get()!!.heartButton.isLiked = true
        } else {
            binding!!.get()!!.heartButton.isLiked = false
        }
    }

    private fun bindingRatingData(product: Product?) {
        if (product!!.ratingDetails.totalRatingValue.toDouble() == 0.0) {
            binding!!.get()!!.starTextView.text = getString(R.string.product_detail__rating)
        } else {
            binding!!.get()!!.starTextView.text = getString(R.string.rating__total_count_n_value, product.ratingDetails.totalRatingValue.toString() + "", product.ratingDetails.totalRatingCount.toString() + "")
        }
        binding!!.get()!!.ratingBar.rating = product.ratingDetails.totalRatingValue
    }

    //touch count
    private fun setTouchCount() {
        if (connectivity!!.isConnected) {
            touchCountViewModel!!.setTouchCountPostDataObj((loginUserId)!!, productDetailViewModel!!.productId, Constants.FILTERING_TYPE_NAME)
        }
    }

    private fun processDataList(listResource: List<ProductColor>): List<ProductColor> {
        val tmpColorList: MutableList<ProductColor> = ArrayList()
        for (i in listResource.indices) {
            try {
                tmpColorList.add(listResource[i].clone() as ProductColor)
                tmpColorList[i].isColorSelect = (tmpColorList[i].id == productColorViewModel!!.colorSelectId)
            } catch (e: CloneNotSupportedException) {
                e.printStackTrace()
            }
        }
        return tmpColorList
    }

    private fun replaceData(imageList: List<Image>?) {
        pagerAdapter!!.get().setImageList(imageList)
        setupSliderPagination()
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductColorData(productColorList: List<ProductColor>) {
        colorAdapter!!.get().replace(productColorList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductSpecsData(productSpecsList: List<ProductSpecs>) {
        specsAdapter!!.get().replace(productSpecsList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductAttributeHeaderData(productAttributeHeaderList: List<ProductAttributeHeader>) {
        headerAdapter!!.get().replace(productAttributeHeaderList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductAttributeDetailData() {
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductTabData(tabObject: List<TabObject>) {
        tabAdapter!!.get().replace(tabObject)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductRelatedData(productList: List<Product>) {
        relatedAdapter!!.get().replace(productList)
        binding!!.get()!!.executePendingBindings()
    }

    private fun replaceProductDetailData(product: Product?) {
        binding!!.get()!!.product = product
        if ((product!!.isAvailable == Constants.ONE)) {
            binding!!.get()!!.inStockTextView.text = getString(R.string.product_detail__in_stock)
            binding!!.get()!!.inStockTextView.setTextColor(resources.getColor(R.color.md_green_700))
            available = true
        } else if ((product.isAvailable == Constants.ZERO)) {
            binding!!.get()!!.inStockTextView.text = getString(R.string.product_detail__item_not_available)
            binding!!.get()!!.inStockTextView.setTextColor(resources.getColor(R.color.button__primary_bg))
            available = false
        }

        //basket to detail
        if ((productDetailViewModel!!.basketFlag == Constants.ONE)) {
            binding!!.get()!!.priceTextView.text = Utils.format(Utils.round(productDetailViewModel!!.price.toFloat(), 2).toDouble())
            bottomBoxLayoutBinding!!.get()!!.priceTextView.text = Utils.format(Utils.round(productDetailViewModel!!.price.toFloat(), 2).toDouble())
            num = productDetailViewModel!!.count.toInt()
            if ((productDetailViewModel!!.count == Constants.EMPTY_STRING) || (productDetailViewModel!!.count == Constants.ZERO)) {
                productDetailViewModel!!.count = Constants.ONE
            } else {
                bottomBoxLayoutBinding!!.get()!!.qtyEditText.setText(productDetailViewModel!!.count)
            }
            binding!!.get()!!.qtyEditText.setText(productDetailViewModel!!.count)
            binding!!.get()!!.colorRecyclerView.isSelected = true //color
        } else {
            binding!!.get()!!.priceTextView.text = Utils.format(product.unitPrice.toDouble()).toString()
            bottomBoxLayoutBinding!!.get()!!.priceTextView.text = Utils.format(product.unitPrice.toDouble()).toString()
            if (product.minimumOrder != null && product.minimumOrder != "0") {
                num = Integer.valueOf(product.minimumOrder)
                bottomBoxLayoutBinding!!.get()!!.qtyEditText.setText(product.minimumOrder)
            } else {
                num = 1
            }
        }
        if (product.minimumOrder != null && product.minimumOrder != "0") {
            minOrder = Integer.valueOf(product.minimumOrder)
        }
        binding!!.get()!!.priceCurrencyTextView.text = product.currencySymbol
        bottomBoxLayoutBinding!!.get()!!.priceCurrencyTextView.text = product.currencySymbol
        //endregion

        //detail pop
        bottomBoxLayoutBinding!!.get()!!.lowestButton.setOnClickListener({ view: View? ->
            mBottomSheetDialog!!.get().setState(BottomSheetBehavior.STATE_EXPANDED)
            if (productDetailViewModel!!.isAddtoCart) {
                confirmBox()
            } else {
                basketViewModel!!.isDirectCheckout = true
                confirmBox()
            }
        })
        if (productColorViewModel!!.isColorData) {
            bottomBoxLayoutBinding!!.get()!!.cangetColorTextView.visibility = View.VISIBLE
        } else {
            bottomBoxLayoutBinding!!.get()!!.cangetColorTextView.visibility = View.GONE
        }
        if (productAttributeHeaderViewModel!!.isHeaderData) {
            bottomBoxLayoutBinding!!.get()!!.attributeTitleTextView.visibility = View.VISIBLE
        } else {
            bottomBoxLayoutBinding!!.get()!!.attributeTitleTextView.visibility = View.GONE
        }
        if ((product.isDiscount == Constants.ONE)) {
            val originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice.toDouble())
            bottomBoxLayoutBinding!!.get()!!.originalPriceTextView.text = originalPriceStr
            bottomBoxLayoutBinding!!.get()!!.originalPriceTextView.paintFlags = binding!!.get()!!.originalPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            bottomBoxLayoutBinding!!.get()!!.originalPriceTextView.visibility = View.GONE
        }
        bottomBoxLayoutBinding!!.get()!!.imagePath = product.defaultPhoto.imgPath
        bottomBoxLayoutBinding!!.get()!!.prodNameTextView.text = product.name
        bottomBoxLayoutBinding!!.get()!!.colorRecycler.isNestedScrollingEnabled = false
        bottomBoxLayoutBinding!!.get()!!.attributeHeaderRecycler.isNestedScrollingEnabled = false
        bottomBoxLayoutBinding!!.get()!!.floatingbtnMinus.setOnClickListener({ view: View? ->
            if (num != 1) {
                if (minOrder != 0) {
                    if (num <= minOrder) {
                        Toast.makeText(getContext(), getString(R.string.product_detail__min_order_error) + Constants.SPACE_STRING + minOrder, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                num -= 1
                bottomBoxLayoutBinding!!.get()!!.qtyEditText.setText(num.toString())
            }
        })
        bottomBoxLayoutBinding!!.get()!!.floatingbtnAdd.setOnClickListener({ view: View? ->
            num += 1
            bottomBoxLayoutBinding!!.get()!!.qtyEditText.setText(num.toString())
        })
        val originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice.toDouble())
        binding!!.get()!!.originalPriceTextView.text = originalPriceStr
        binding!!.get()!!.commentCountTextView.text = product.commentHeaderCount.toString()
        binding!!.get()!!.favCountTextView.text = product.favouriteCount.toString()
        binding!!.get()!!.touchCountTextView.text = Utils.numberFormat(product.touchCount.toLong())
        productAttributeHeaderViewModel!!.price = product.unitPrice
        productAttributeHeaderViewModel!!.originalPrice = product.originalPrice
        binding!!.get()!!.heartButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                val product = binding!!.get()!!.product
                product?.let { favFunction(it, likeButton) }
            }

            override fun unLiked(likeButton: LikeButton) {
                val product = binding!!.get()!!.product
                product?.let { unFavFunction(it, likeButton) }
            }
        })
        binding!!.get()!!.seeCommentButton.setOnClickListener({ view: View? -> navigationController!!.navigateToCommentListActivity((getActivity())!!, (product)) })
        binding!!.get()!!.writeCommentButton.setOnClickListener({ view: View? -> navigationController!!.navigateToCommentListActivity((getActivity())!!, (product)) })

//        binding.get().likeImageView.setOnClickListener(view -> sendLikePostData());
//        binding.get().likeTextView.setOnClickListener(view -> sendLikePostData());
        if (product.isFeatured != Constants.ONE) {
            binding!!.get()!!.featureTextView.visibility = View.GONE
            binding!!.get()!!.featureIconImageView.visibility = View.GONE
        }
        if (product.isDiscount != Constants.ONE) {
            binding!!.get()!!.originalPriceTextView.visibility = View.GONE
            binding!!.get()!!.discountPercentButton.visibility = View.INVISIBLE
        } else {
            binding!!.get()!!.originalPriceTextView.visibility = View.VISIBLE
            binding!!.get()!!.discountPercentButton.visibility = View.VISIBLE
            val discountValue = product.discountPercent.toInt()
            val discountValueStr = "-$discountValue%"
            binding!!.get()!!.discountPercentTextView.text = discountValueStr
            binding!!.get()!!.originalPriceTextView.paintFlags = binding!!.get()!!.originalPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        if ((product.highlightInformation == "")) {
            binding!!.get()!!.constraintLayout5.visibility = View.GONE
            binding!!.get()!!.highlightTextview.visibility = View.GONE
        } else {
            binding!!.get()!!.constraintLayout5.visibility = View.VISIBLE
            binding!!.get()!!.highlightTextview.visibility = View.VISIBLE
        }
        binding!!.get()!!.executePendingBindings()

        //up and down
        binding!!.get()!!.descUpDownImageView.setOnClickListener({ v: View -> toggleDescription(v) })
        binding!!.get()!!.desc.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(binding!!.get()!!.descUpDownImageView)
            if (show) {
                ViewAnimationUtil.expand(binding!!.get()!!.descTextView)
            } else {
                ViewAnimationUtil.collapse(binding!!.get()!!.descTextView)
            }
        })
        binding!!.get()!!.choiceUpDownImageView.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(v)
            if (show) {
                expandChoiceFunction()
            } else {
                collapseChoiceFunction()
            }
        })
        binding!!.get()!!.textView39.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(binding!!.get()!!.choiceUpDownImageView)
            if (show) {
                expandChoiceFunction()
            } else {
                collapseChoiceFunction()
            }
        })
        binding!!.get()!!.tabUpDownImageView.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(v)
            if (show) {
                expandTabFunction()
            } else {
                collapseTabFunction()
            }
        })
        binding!!.get()!!.textview56.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(binding!!.get()!!.tabUpDownImageView)
            if (show) {
                expandTabFunction()
            } else {
                collapseTabFunction()
            }
        })
        binding!!.get()!!.detailUpDownImageView.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(v)
            if (show) {
                expandDetailFunction()
            } else {
                collapseDetailFunction()
            }
        })
        binding!!.get()!!.textView25.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(binding!!.get()!!.detailUpDownImageView)
            if (show) {
                expandDetailFunction()
            } else {
                collapseDetailFunction()
            }
        })
        binding!!.get()!!.strictUpDownImageView.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(v)
            if (show) {
                expandStrictFunction()
            } else {
                collapseStrictFunction()
            }
        })
        binding!!.get()!!.textview51.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(binding!!.get()!!.strictUpDownImageView)
            if (show) {
                expandStrictFunction()
            } else {
                collapseStrictFunction()
            }
        })
        binding!!.get()!!.seeMoreImageView.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(v)
            if (show) {
                expandSenceFunction(product)
            } else {
                collapseSenceFunction()
            }
        })
        binding!!.get()!!.textview54.setOnClickListener({ v: View? ->
            val show: Boolean = Utils.toggleUpDownWithAnimation(binding!!.get()!!.seeMoreImageView)
            if (show) {
                expandSenceFunction(product)
            } else {
                collapseSenceFunction()
            }
        })

        //endregion
    }

    private fun setSaveToBasket() {

        // convert map to JSON String
        val gsonObj = Gson()
        val jsonStr = gsonObj.toJson(productAttributeHeaderViewModel!!.basketItemHolderHashMap)
        val priceStr = gsonObj.toJson(productAttributeHeaderViewModel!!.attributeHeaderHashMap)

        //validation
        if (validateBasket()) {

            // save
            if (productAttributeHeaderViewModel!!.priceAfterAttribute == 0) {
                basketViewModel!!.setSaveToBasketListObj(
                        productDetailViewModel!!.basketId,
                        productDetailViewModel!!.productId,
                        num,
                        jsonStr,
                        productColorViewModel!!.colorSelectId,
                        productColorViewModel!!.colorSelectValue,
                        (additionPrice)!!,
                        productAttributeHeaderViewModel!!.price,
                        productAttributeHeaderViewModel!!.originalPrice,
                        "",
                        priceStr
                )
            } else {
                basketViewModel!!.setSaveToBasketListObj(
                        productDetailViewModel!!.basketId,
                        productDetailViewModel!!.productId,
                        num,
                        jsonStr,
                        productColorViewModel!!.colorSelectId,
                        productColorViewModel!!.colorSelectValue,
                        (additionPrice)!!,
                        productAttributeHeaderViewModel!!.priceAfterAttribute,
                        productAttributeHeaderViewModel!!.originalPriceAfterAttribute,
                        "",
                        priceStr)
            }
        }
    }

    private fun validateBasket(): Boolean {
        // color
        if (productColorViewModel!!.isColorData) {
            if ((productColorViewModel!!.colorSelectId == Constants.EMPTY_STRING) && (productColorViewModel!!.colorSelectValue == Constants.EMPTY_STRING)) {
                psDialogMsg!!.showWarningDialog(getString(R.string.product_detail__warning_select_attr), getString(R.string.app__ok))
                psDialogMsg!!.show()
                return false
            }
        }
        // attribute
        val key: List<String> = ArrayList(productAttributeHeaderViewModel!!.basketItemHolderHashMap.values)
        val value: List<Int> = ArrayList(productAttributeHeaderViewModel!!.attributeHeaderHashMap.values)
        if (productAttributeHeaderViewModel!!.isHeaderData) {
//            for (int i = 0; i < key.size(); i++) {
//                if (key.get(i).contains((getString(R.string.product_detail__choose_zg))) ) {
//                    psDialogMsg.showWarningDialog(key.get(i), getString(R.string.app__ok));
//                    psDialogMsg.show();
//                    return false;
//                }
//            }
            if (key.size != value.size) {
                psDialogMsg!!.showWarningDialog(getString(R.string.product_detail__select_all), getString(R.string.app__ok))
                psDialogMsg!!.show()
                return false
            }
        }
        return true
    }

    private fun confirmBox() {
        setSaveToBasket()
    }

    private fun toggleDescription(v: View) {
        val show = Utils.toggleUpDownWithAnimation(v)
        if (show) {
            ViewAnimationUtil.expand(binding!!.get()!!.descTextView)
        } else {
            ViewAnimationUtil.collapse(binding!!.get()!!.descTextView)
        }
    }

    private fun expandChoiceFunction() {
        ViewAnimationUtil.expand(binding!!.get()!!.howManyTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.qtyEditText)
        ViewAnimationUtil.expand(binding!!.get()!!.floatingAddButton)
        ViewAnimationUtil.expand(binding!!.get()!!.floatingMinusButton)
        binding!!.get()!!.attributeHeaderRecycler.visibility = View.VISIBLE
        if (productAttributeHeaderViewModel!!.isHeaderData) {
            ViewAnimationUtil.expand(binding!!.get()!!.otherFactsTextView)
        }
        binding!!.get()!!.attributeHeaderRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding!!.get()!!.floatingMinusButton.setOnClickListener({ view: View? ->
            if (num != 1) {
                if (minOrder != 0) {
                    if (num <= minOrder) {
                        Toast.makeText(getContext(), getString(R.string.product_detail__min_order_error) + Constants.SPACE_STRING + minOrder, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                num -= 1
                binding!!.get()!!.qtyEditText.setText(num.toString())
            }
        })
        binding!!.get()!!.floatingAddButton.setOnClickListener({ view: View? ->
            num += 1
            binding!!.get()!!.qtyEditText.setText(num.toString())
        })
    }

    private fun collapseChoiceFunction() {
        ViewAnimationUtil.collapse(binding!!.get()!!.howManyTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.qtyEditText)
        ViewAnimationUtil.collapse(binding!!.get()!!.floatingAddButton)
        ViewAnimationUtil.collapse(binding!!.get()!!.floatingMinusButton)
        ViewAnimationUtil.collapse(binding!!.get()!!.otherFactsTextView)
        binding!!.get()!!.attributeHeaderRecycler.visibility = View.GONE
    }

    private fun expandTabFunction() {
        ViewAnimationUtil.expand(binding!!.get()!!.findBySimilarFactTextView)
        binding!!.get()!!.tabRecyclerView.visibility = View.VISIBLE
        ViewAnimationUtil.expand(binding!!.get()!!.alsoBuyTextView)
        binding!!.get()!!.alsoBuyRecyclerView.visibility = View.VISIBLE
        binding!!.get()!!.alsoBuyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding!!.get()!!.tabRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun collapseTabFunction() {
        binding!!.get()!!.tabRecyclerView.visibility = View.GONE
        ViewAnimationUtil.collapse(binding!!.get()!!.findBySimilarFactTextView)
        binding!!.get()!!.alsoBuyRecyclerView.visibility = View.GONE
        ViewAnimationUtil.collapse(binding!!.get()!!.alsoBuyTextView)
    }

    private fun expandDetailFunction() {
        ViewAnimationUtil.expand(binding!!.get()!!.productNameTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.brandTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productUnitTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productUnitValueTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productMeasurementTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productMeasurementValueTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productMinOrderTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productMinOrderValueTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productShippingCostTextView)
        ViewAnimationUtil.expand(binding!!.get()!!.productShippingCostValueTextView)
        if (productColorViewModel!!.isColorData) {
            ViewAnimationUtil.expand(binding!!.get()!!.cangetColorTextView)
            ViewAnimationUtil.expand(binding!!.get()!!.colorRecyclerView)
        } else {
            ViewAnimationUtil.collapse(binding!!.get()!!.cangetColorTextView)
            ViewAnimationUtil.collapse(binding!!.get()!!.colorRecyclerView)
        }
        if (productSpecsViewModel!!.isSpecsData) {
            ViewAnimationUtil.expand(binding!!.get()!!.factsRecyclerView)
            ViewAnimationUtil.expand(binding!!.get()!!.detailFactTextView)
        } else {
            ViewAnimationUtil.collapse(binding!!.get()!!.factsRecyclerView)
            ViewAnimationUtil.collapse(binding!!.get()!!.detailFactTextView)
        }
        if (!productColorViewModel!!.isColorData && !productSpecsViewModel!!.isSpecsData) {
            binding!!.get()!!.viewDetailInfo.visibility = View.VISIBLE
        }
        bindProductDetailInfo(productDetailViewModel!!.productContainer)
    }

    private fun collapseDetailFunction() {
        ViewAnimationUtil.collapse(binding!!.get()!!.productNameTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.brandTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productUnitTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productUnitValueTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productMeasurementTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productMeasurementValueTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productMinOrderTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productMinOrderValueTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productShippingCostTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.productShippingCostValueTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.cangetColorTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.detailFactTextView)
        ViewAnimationUtil.collapse(binding!!.get()!!.colorRecyclerView)
        ViewAnimationUtil.collapse(binding!!.get()!!.factsRecyclerView)
        binding!!.get()!!.viewDetailInfo.visibility = View.GONE
    }

    private fun expandStrictFunction() {
        ViewAnimationUtil.expand(binding!!.get()!!.seeAllFactButton)
        ViewAnimationUtil.expand(binding!!.get()!!.refundPolicyButton)
    }

    private fun collapseStrictFunction() {
        ViewAnimationUtil.collapse(binding!!.get()!!.seeAllFactButton)
        ViewAnimationUtil.collapse(binding!!.get()!!.refundPolicyButton)
    }

    private fun expandSenceFunction(product: Product?) {
        binding!!.get()!!.noScenceTextview.visibility = View.GONE
        ViewAnimationUtil.expand(binding!!.get()!!.seeCommentButton)
        ViewAnimationUtil.expand(binding!!.get()!!.writeCommentButton)
        binding!!.get()!!.writeCommentButton.setOnClickListener({ view: View? -> navigationController!!.navigateToCommentListActivity((getActivity())!!, (product)!!) })
    }

    private fun collapseSenceFunction() {
        ViewAnimationUtil.collapse(binding!!.get()!!.noScenceTextview)
        ViewAnimationUtil.collapse(binding!!.get()!!.writeCommentButton)
        ViewAnimationUtil.collapse(binding!!.get()!!.seeCommentButton)
    }

    private fun setTagData(listResource: Product?) {
        if (tabObjectList!!.get().size > 0) {
            tabObjectList!!.get().clear()
        }
        tabObjectList!!.get().add(TabObject(Constants.CATEGORY, listResource!!.catId, listResource.category.name))
        tabObjectList!!.get().add(TabObject(Constants.SUBCATEGORY, listResource.subCatId, listResource.subCategory.name))
        if (!listResource.searchTag.isEmpty()) {
            val tags = listResource.searchTag.split(",".toRegex()).toTypedArray()
            for (tag: String in tags) {
                tabObjectList!!.get().add(TabObject(Constants.PRODUCT_TAG, tag, tag))
            }
        }
        replaceProductTabData(tabObjectList!!.get())
    }

    private fun setBasketMenuItemVisible(isVisible: Boolean) {
        if (basketMenuItem != null) {
            basketMenuItem.isVisible = isVisible
        }
    }

    private val basketMenuItem: MenuItem? = null

    /* @Override
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
    fun callBasket() {
        if ((productDetailViewModel!!.basketFlag == Constants.ONE)) {
            refreshBasketData()
        } else {
            navigationController!!.navigateToBasketList((activity)!!)

//            bottomBoxLayoutBinding.get().qtyEditText.setText(Constants.ONE);
        }
    }

    fun refreshBasketData() {
        if ((productDetailViewModel!!.basketFlag == Constants.ONE)) {
            if (activity != null) {
                navigationController!!.navigateBackToBasketActivity((activity)!!)
                activity!!.finish()
            }
        }
    }

    private fun unFavFunction(product: Product?, likeButton: LikeButton?) {
        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, activity, navigationController, likeButton, {

            // Success Action
            if (!favouriteViewModel!!.isLoading) {
                favouriteViewModel!!.setFavouritePostDataObj(product!!.id, (loginUserId)!!)
                likeButton!!.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null))
            }
        })
    }

    private fun favFunction(product: Product?, likeButton: LikeButton?) {
        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, activity, navigationController, likeButton, {

            // Success Action
            if (!favouriteViewModel!!.isLoading) {
                favouriteViewModel!!.setFavouritePostDataObj(product!!.id, (loginUserId)!!)
                likeButton!!.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null))
            }
        })
    }
}