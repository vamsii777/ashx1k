package com.dewonderstruck.apps.ashx0.ui.product.search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentSearchBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.viewmodel.homelist.HomeSearchProductViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.google.android.gms.ads.AdRequest

class SearchFragment : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var catId: String? = Constants.NO_DATA
    private var subCatId: String? = Constants.NO_DATA
    private var psDialogMsg: PSDialogMsg? = null
    private var homeSearchProductViewModel: HomeSearchProductViewModel? = null
    private var basketViewModel: BasketViewModel? = null
    private val basketMenuItem: MenuItem? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentSearchBinding>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
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
            //// navigationController.navigateToBasketList(activity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CATEGORY) {
            catId = data!!.getStringExtra(Constants.CATEGORY_ID)
            binding!!.get().categoryTextView.text = data.getStringExtra(Constants.CATEGORY_NAME)
            homeSearchProductViewModel!!.holder.catId = catId
            subCatId = ""
            homeSearchProductViewModel!!.holder.subCatId = subCatId
            binding!!.get().subCategoryTextView.text = ""
        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY) {
            subCatId = data!!.getStringExtra(Constants.SUBCATEGORY_ID)
            binding!!.get().subCategoryTextView.text = data.getStringExtra(Constants.SUBCATEGORY_NAME)
            homeSearchProductViewModel!!.holder.subCatId = subCatId
        }
    }

    override fun onDispatched() {}

    @SuppressLint("MissingPermission")
    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
        psDialogMsg = PSDialogMsg(activity, false)
        binding!!.get().itemNameEditText.setHint(R.string.search__notSet)
        binding!!.get().categoryTextView.setHint(R.string.search__notSet)
        binding!!.get().subCategoryTextView.setHint(R.string.search__notSet)
        //binding.get().lowestPriceEditText.setHint(R.string.search__notSet);
        //binding.get().highestPriceEditText.setHint(R.string.search__notSet);
        val builder = AlertDialog.Builder(this.activity)
        val alertDialog = AutoClearedValue(this, builder)
        alertDialog.get().setTitle(resources.getString(R.string.Feature_UI__search_alert_cat_title))
        binding!!.get().productNameTextView.text = binding!!.get().productNameTextView.text.toString()
        binding!!.get().productTypeTextView.text = binding!!.get().productTypeTextView.text.toString()
        binding!!.get().subProductTypeTextView.text = binding!!.get().subProductTypeTextView.text.toString()
        binding!!.get().priceTextView.text = binding!!.get().priceTextView.text.toString()
        //binding.get().lowestPriceTextView.setText(binding.get().lowestPriceTextView.getText().toString());
        //binding.get().highestPriceTextView.setText(binding.get().highestPriceTextView.getText().toString());
        binding!!.get().specialCheckTextView.text = binding!!.get().specialCheckTextView.text.toString()
        binding!!.get().selectionTextView.text = binding!!.get().selectionTextView.text.toString()
        binding!!.get().discountPriceTextView.text = binding!!.get().discountPriceTextView.text.toString()
        binding!!.get().searchButton.text = binding!!.get().searchButton.text.toString()
        binding!!.get().categoryTextView.text = ""
        binding!!.get().subCategoryTextView.text = ""
        binding!!.get().categorySelectionView.setOnClickListener { view: View? -> navigationController.navigateToSearchActivityCategoryFragment(this@SearchFragment.activity, Constants.CATEGORY, catId, subCatId, Constants.NO_DATA, Constants.NO_DATA) }
        binding!!.get().subCategorySelectionView.setOnClickListener { view: View? ->
            if (catId == Constants.NO_DATA) {
                psDialogMsg!!.showWarningDialog(getString(R.string.error_message__choose_category), getString(R.string.app__ok))
                psDialogMsg!!.show()
            } else {
                navigationController.navigateToSearchActivityCategoryFragment(this@SearchFragment.activity, Constants.SUBCATEGORY, catId, subCatId, Constants.NO_DATA, Constants.NO_DATA)
            }
        }
        binding!!.get().oneStar.setOnClickListener { v: View? ->
            if (homeSearchProductViewModel!!.holder.rating_value_one == "") {
                selectStar(Constants.RATING_ONE)
                unSelectStar(Constants.RATING_TWO)
                unSelectStar(Constants.RATING_THREE)
                unSelectStar(Constants.RATING_FOUR)
                unSelectStar(Constants.RATING_FIVE)
            } else {
                unSelectStar(Constants.RATING_ONE)
            }
        }
        binding!!.get().twoStar.setOnClickListener { v: View? ->
            if (homeSearchProductViewModel!!.holder.rating_value_two == "") {
                selectStar(Constants.RATING_TWO)
                unSelectStar(Constants.RATING_ONE)
                unSelectStar(Constants.RATING_THREE)
                unSelectStar(Constants.RATING_FOUR)
                unSelectStar(Constants.RATING_FIVE)
            } else {
                unSelectStar(Constants.RATING_TWO)
            }
        }
        binding!!.get().threeStar.setOnClickListener { v: View? ->
            if (homeSearchProductViewModel!!.holder.rating_value_three == "") {
                selectStar(Constants.RATING_THREE)
                unSelectStar(Constants.RATING_TWO)
                unSelectStar(Constants.RATING_ONE)
                unSelectStar(Constants.RATING_FOUR)
                unSelectStar(Constants.RATING_FIVE)
            } else {
                unSelectStar(Constants.RATING_THREE)
            }
        }
        binding!!.get().fourStar.setOnClickListener { v: View? ->
            if (homeSearchProductViewModel!!.holder.rating_value_four == "") {
                selectStar(Constants.RATING_FOUR)
                unSelectStar(Constants.RATING_TWO)
                unSelectStar(Constants.RATING_THREE)
                unSelectStar(Constants.RATING_ONE)
                unSelectStar(Constants.RATING_FIVE)
            } else {
                unSelectStar(Constants.RATING_FOUR)
            }
        }
        binding!!.get().fiveStar.setOnClickListener { v: View? ->
            if (homeSearchProductViewModel!!.holder.rating_value_five == "") {
                selectStar(Constants.RATING_FIVE)
                unSelectStar(Constants.RATING_TWO)
                unSelectStar(Constants.RATING_THREE)
                unSelectStar(Constants.RATING_FOUR)
                unSelectStar(Constants.RATING_ONE)
            } else {
                unSelectStar(Constants.RATING_FIVE)
            }
        }

        // set final value listener
        binding!!.get().seekBarPrice.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            Log.d("=>", "$minValue : $maxValue")
            binding!!.get().highestPriceEditText.setText(maxValue.toString())
            binding!!.get().priceSmin.text = minValue.toString()
            binding!!.get().priceSmax.text = maxValue.toString()
            binding!!.get().lowestPriceEditText.setText(minValue.toString())
        }
        binding!!.get().searchButton.setOnClickListener { view: View? ->

            // Get Name
            homeSearchProductViewModel!!.holder.search_term = binding!!.get().itemNameEditText.text.toString()

            // Get Price
            //homeSearchProductViewModel.holder.max_price = binding.get().highestPriceEditText.getText().toString();
            //homeSearchProductViewModel.holder.min_price = binding.get().seekBarPrice.setMin();
            //getConfigBuilder().max(Float.parseFloat(homeSearchProductViewModel.holder.max_price)).build()
            //homeSearchProductViewModel.holder.max_price = String.valueOf(binding.get().seekBarPrice.getConfigBuilder().max(homeSearchProductViewModel.holder.max_price).build()));
            //binding.get().seekBarPrice.getConfigBuilder().max(Float.parseFloat(homeSearchProductViewModel.holder.max_price)).build();
            //homeSearchProductViewModel.holder.min_price = String.valueOf(binding.get().seekBarPrice.getConfigBuilder().getMin());
            //homeSearchProductViewModel.holder.max_price = String.valueOf(binding.get().seekBarPrice.getConfigBuilder().getMax());

            /*  // set listener
            binding.get().seekBarPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                @Override
                public void valueChanged(Number minValue, Number maxValue) {
                    //tvMin.setText(String.valueOf(minValue));
                    //tvMax.setText(String.valueOf(maxValue));

                }
            });*/homeSearchProductViewModel!!.holder.max_price = binding!!.get().priceSmax.text.toString()
            homeSearchProductViewModel!!.holder.min_price = binding!!.get().priceSmin.text.toString()

            // Prepare Rating
//            homeSearchProductViewModel.holder.overall_rating = "";
            if (homeSearchProductViewModel!!.holder.rating_value_one != "") {
                homeSearchProductViewModel!!.holder.overall_rating = Constants.RATING_ONE
            }
            if (homeSearchProductViewModel!!.holder.rating_value_two != "") {
                homeSearchProductViewModel!!.holder.overall_rating = Constants.RATING_TWO
            }
            if (homeSearchProductViewModel!!.holder.rating_value_three != "") {
                homeSearchProductViewModel!!.holder.overall_rating = Constants.RATING_THREE
            }
            if (homeSearchProductViewModel!!.holder.rating_value_four != "") {
                homeSearchProductViewModel!!.holder.overall_rating = Constants.RATING_FOUR
            }
            if (homeSearchProductViewModel!!.holder.rating_value_five != "") {
                homeSearchProductViewModel!!.holder.overall_rating = Constants.RATING_FIVE
            }
            if (homeSearchProductViewModel!!.holder.rating_value_one == "" && homeSearchProductViewModel!!.holder.rating_value_two == "" && homeSearchProductViewModel!!.holder.rating_value_three == "" && homeSearchProductViewModel!!.holder.rating_value_four == "" && homeSearchProductViewModel!!.holder.rating_value_five == "") {
                homeSearchProductViewModel!!.holder.overall_rating = ""
            }

            // Get Feature Switch Data
            if (binding!!.get().featuredSwitch.isChecked) {
                homeSearchProductViewModel!!.holder.isFeatured = Constants.ONE
            } else {
                homeSearchProductViewModel!!.holder.isFeatured = ""
            }

            // Get Discount Switch Data
            if (binding!!.get().discountSwitch.isChecked) {
                homeSearchProductViewModel!!.holder.isDiscount = Constants.ONE
            } else {
                homeSearchProductViewModel!!.holder.isDiscount = ""
            }

            // For Sorting
            if (homeSearchProductViewModel!!.holder.isFeatured == Constants.ONE) {
                homeSearchProductViewModel!!.holder.order_by = Constants.FILTERING_FEATURE
            }

            // Set to Intent
            navigationController.navigateToHomeFilteringActivity(this@SearchFragment.activity, homeSearchProductViewModel!!.holder, null)
        }
    }

    private fun unSelectStar(star: Button) {
        star.setTextColor(resources.getColor(R.color.text__primary))
        star.setBackgroundResource(R.drawable.button_border)
        star.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full_gray, 0, 0)
    }

    private fun selectStar(star: Button) {
        star.setTextColor(resources.getColor(R.color.text__white))
        star.setBackgroundColor(resources.getColor(R.color.global__primary))
        star.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_white, 0, 0)
    }

    private fun unSelectStar(stars: String) {
        when (stars) {
            Constants.RATING_ONE -> {
                unSelectStar(binding!!.get().oneStar)
                homeSearchProductViewModel!!.holder.rating_value_one = ""
            }
            Constants.RATING_TWO -> {
                unSelectStar(binding!!.get().twoStar)
                homeSearchProductViewModel!!.holder.rating_value_two = ""
            }
            Constants.RATING_THREE -> {
                unSelectStar(binding!!.get().threeStar)
                homeSearchProductViewModel!!.holder.rating_value_three = ""
            }
            Constants.RATING_FOUR -> {
                unSelectStar(binding!!.get().fourStar)
                homeSearchProductViewModel!!.holder.rating_value_four = ""
            }
            Constants.RATING_FIVE -> {
                unSelectStar(binding!!.get().fiveStar)
                homeSearchProductViewModel!!.holder.rating_value_five = ""
            }
        }
    }

    private fun selectStar(stars: String) {
        when (stars) {
            Constants.RATING_ONE -> {
                selectStar(binding!!.get().oneStar)
                homeSearchProductViewModel!!.holder.rating_value_one = Constants.RATING_ONE
            }
            Constants.RATING_TWO -> {
                selectStar(binding!!.get().twoStar)
                homeSearchProductViewModel!!.holder.rating_value_two = Constants.RATING_TWO
            }
            Constants.RATING_THREE -> {
                selectStar(binding!!.get().threeStar)
                homeSearchProductViewModel!!.holder.rating_value_three = Constants.RATING_THREE
            }
            Constants.RATING_FOUR -> {
                selectStar(binding!!.get().fourStar)
                homeSearchProductViewModel!!.holder.rating_value_four = Constants.RATING_FOUR
            }
            Constants.RATING_FIVE -> {
                selectStar(binding!!.get().fiveStar)
                homeSearchProductViewModel!!.holder.rating_value_five = Constants.RATING_FIVE
            }
        }
    }

    override fun initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel::class.java)
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        basketData()
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
}