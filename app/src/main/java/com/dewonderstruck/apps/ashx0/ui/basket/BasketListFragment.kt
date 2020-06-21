package com.dewonderstruck.apps.ashx0.ui.basket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentBasketListBinding
import com.dewonderstruck.apps.ashx0.ui.basket.adapter.BasketAdapter
import com.dewonderstruck.apps.ashx0.ui.basket.adapter.BasketAdapter.BasketClickCallBack
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest
import java.lang.String

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class BasketListFragment : PSFragment(), DiffUtilDispatchedInterface {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var psDialogMsg: PSDialogMsg? = null
    private var basketViewModel: BasketViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentBasketListBinding>? = null
    private var basketAdapter: AutoClearedValue<BasketAdapter>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentBasketListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        if (Config.SHOW_ADMOB && connectivity!!.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
        binding!!.get().checkoutButton.text = binding!!.get().checkoutButton.text.toString()
        binding!!.get().checkoutButton.setOnClickListener { view: View? -> doCheckOut() }
    }

    private fun doCheckOut() {
        Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, this@BasketListFragment.activity, navigationController) { navigationController!!.navigateToCheckoutActivity(activity) }
    }

    override fun initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
    }

    override fun initAdapters() {

        //basket
        val basketAdapter1 = BasketAdapter(dataBindingComponent, object : BasketClickCallBack {
            override fun onMinusClick(basket: Basket) {
                basketViewModel!!.setUpdateToBasketListObj(basket.id, basket.count)
            }

            override fun onAddClick(basket: Basket) {
                basketViewModel!!.setUpdateToBasketListObj(basket.id, basket.count)
            }

            override fun onDeleteConfirm(basket: Basket) {
                psDialogMsg!!.showConfirmDialog(getString(R.string.delete_item_from_basket), getString(R.string.app__ok), getString(R.string.app__cancel))
                psDialogMsg!!.show()
                psDialogMsg!!.okButton.setOnClickListener { view: View? ->
                    basketViewModel!!.setDeleteToBasketListObj(basket.id)
                    psDialogMsg!!.cancel()
                }
                psDialogMsg!!.cancelButton.setOnClickListener { view: View? -> psDialogMsg!!.cancel() }
            }

            override fun onClick(basket: Basket) {
                navigationController!!.navigateToProductDetailActivity(activity, basket)
            }
        }, this)
        basketAdapter = AutoClearedValue(this, basketAdapter1)
        bindingBasketAdapter(basketAdapter!!.get())
    }

    private fun bindingBasketAdapter(nvbasketAdapter: BasketAdapter) {
        basketAdapter = AutoClearedValue(this, nvbasketAdapter)
        //        binding.get().basketRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding!!.get().basketRecycler.adapter = basketAdapter!!.get()
    }

    override fun initData() {
        if (context != null) {
            binding!!.get().noItemTitleTextView.text = context!!.getString(R.string.basket__no_item_title)
            binding!!.get().noItemDescTextView.text = context!!.getString(R.string.basket__no_item_desc)
        }
        LoadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE__BASKET_FRAGMENT
                && resultCode == Constants.RESULT_CODE__REFRESH_BASKET_LIST) {
            basketViewModel!!.setBasketListWithProductObj()
            loadLoginUserId()
        }
    }

    override fun onResume() {
        super.onResume()
        loadLoginUserId()
        basketViewModel!!.setBasketListWithProductObj()
    }

    private fun LoadData() {
        //load basket
        basketViewModel!!.setBasketListWithProductObj()
        val basketData = basketViewModel!!.allBasketWithProductList
        basketData?.observe(this, Observer { listResource: List<Basket>? ->
            if (listResource != null) {
                if (listResource.size > 0) {
                    binding!!.get().noItemConstraintLayout.visibility = View.GONE
                    binding!!.get().checkoutConstraintLayout.visibility = View.VISIBLE
                } else {
                    binding!!.get().checkoutConstraintLayout.visibility = View.GONE
                    binding!!.get().noItemConstraintLayout.visibility = View.VISIBLE
                    if (activity is BasketListActivity) {
                        activity!!.finish()
                    }
                }
                replaceProductSpecsData(listResource)
            } else {
                if (basketViewModel!!.allBasketWithProductList != null) {
                    if (basketViewModel!!.allBasketWithProductList.value != null) {
                        if (basketViewModel!!.allBasketWithProductList.value!!.size == 0) {
                            binding!!.get().checkoutConstraintLayout.visibility = View.GONE
                            binding!!.get().noItemConstraintLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
        basketViewModel!!.basketUpdateData.observe(this, Observer<Resource<Boolean?>> { resourse: Resource<Boolean?>? ->
            if (resourse != null) {
                if (resourse.status === Status.SUCCESS) {
                    basketViewModel!!.totalPrice = 0
                    basketViewModel!!.basketCount = 0
                    basketViewModel!!.setBasketListWithProductObj()
                }
            }
        })
        basketViewModel!!.basketDeleteData.observe(this, Observer<Resource<Boolean?>> { resource: Resource<Boolean?>? ->
            if (resource != null) {
                if (resource.status === Status.SUCCESS) {
                    basketViewModel!!.totalPrice = 0
                    basketViewModel!!.basketCount = 0
                    basketViewModel!!.setBasketListWithProductObj()
                }
            }
        })
    }

    private fun replaceProductSpecsData(basketList: List<Basket>) {
        basketAdapter!!.get().replace(basketList)
        if (basketList != null) {
            basketAdapter!!.get().replace(basketList)
            if (basketList.size > 0) {
                basketViewModel!!.totalPrice = 0
                for (i in basketList.indices) {
                    basketViewModel!!.totalPrice += basketList[i].basketPrice * basketList[i].count
                }
                basketViewModel!!.basketCount = 0
                for (i in basketList.indices) {
                    basketViewModel!!.basketCount += basketList[i].count
                }
                val totalPriceString = basketList[0].product!!.currencySymbol + Constants.SPACE_STRING + Utils.format(Utils.round(basketViewModel!!.totalPrice, 2).toDouble())
                binding!!.get().totalPriceTextView.text = totalPriceString
                binding!!.get().countTextView.text = String.valueOf(basketViewModel!!.basketCount)
            } else {
                binding!!.get().totalPriceTextView.text = Constants.ZERO
                binding!!.get().countTextView.text = Constants.ZERO
                basketViewModel!!.totalPrice = 0
                basketViewModel!!.basketCount = 0
                if (basketList.size > 0) {
                    for (i in basketList.indices) {
                        basketViewModel!!.totalPrice += basketList[i].basketPrice * basketList[i].count
                    }
                    for (i in basketList.indices) {
                        basketViewModel!!.basketCount += basketList[i].count
                    }
                    val totalPriceString = basketList[0].product!!.currencySymbol + Constants.SPACE_STRING + Utils.format(basketViewModel!!.totalPrice.toDouble())
                    binding!!.get().totalPriceTextView.text = totalPriceString
                    binding!!.get().countTextView.text = String.valueOf(basketViewModel!!.basketCount)
                } else {
                    binding!!.get().totalPriceTextView.text = Constants.ZERO
                    binding!!.get().countTextView.text = Constants.ZERO
                }
                binding!!.get().executePendingBindings()
            }
        }
    }
}