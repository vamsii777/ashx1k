package com.dewonderstruck.apps.ashx0.ui.product.filtering

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentFilterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.viewmodel.homelist.HomeSearchProductViewModel
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import com.google.android.gms.ads.AdRequest

class FilterFragment : PSFragment(), DiffUtilDispatchedInterface2 {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var homeSearchProductViewModel: HomeSearchProductViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentFilterBinding>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentFilterBinding: FragmentFilterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, fragmentFilterBinding)
        setHasOptionsMenu(true)
        binding!!.get().loadingMore = connectivity.isConnected
        return binding!!.get().root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
        binding!!.get().setItemName.setHint(R.string.sf__notSet)
        binding!!.get().minimumEditText.setHint(R.string.sf__notSet)
        binding!!.get().maximumEditText.setHint(R.string.sf__notSet)
        if (activity != null) {

            // Get Data from Intent
            val intent = activity!!.intent
            homeSearchProductViewModel!!.holder = intent.getSerializableExtra(Constants.FILTERING_HOLDER) as ProductParameterHolder?

            // Name Binding
            binding!!.get().setItemName.setText(homeSearchProductViewModel!!.holder.search_term)

            // Price Binding
            /*binding.get().minimumEditText.setText(homeSearchProductViewModel.holder.min_price);
            binding.get().maximumEditText.setText(homeSearchProductViewModel.holder.max_price);*/

            //binding.get().seekBarFilter.getConfigBuilder().max(Float.parseFloat(homeSearchProductViewModel.holder.max_price)).build();

            // Feature Switch Binding
            if (homeSearchProductViewModel!!.holder.isFeatured != null) {
                if (homeSearchProductViewModel!!.holder.isFeatured == Constants.ONE) {
                    binding!!.get().featuredSwitch.isChecked = true
                } else {
                    binding!!.get().featuredSwitch.isChecked = false
                }
            }

            // Discount Switch Binding
            if (homeSearchProductViewModel!!.holder.isDiscount != null) {
                if (homeSearchProductViewModel!!.holder.isDiscount == Constants.ONE) {
                    binding!!.get().discountSwitch.isChecked = true
                } else {
                    binding!!.get().discountSwitch.isChecked = false
                }
            }

            // Rating Binding
            if (!homeSearchProductViewModel!!.holder.rating_value_one.isEmpty()) {
                selectStar(Constants.RATING_ONE)
            }
            if (!homeSearchProductViewModel!!.holder.rating_value_two.isEmpty()) {
                selectStar(Constants.RATING_TWO)
            }
            if (!homeSearchProductViewModel!!.holder.rating_value_three.isEmpty()) {
                selectStar(Constants.RATING_THREE)
            }
            if (!homeSearchProductViewModel!!.holder.rating_value_four.isEmpty()) {
                selectStar(Constants.RATING_FOUR)
            }
            if (!homeSearchProductViewModel!!.holder.rating_value_five.isEmpty()) {
                selectStar(Constants.RATING_FIVE)
            }
        }


        // set final value listener
        binding!!.get().seekBarFilter.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            Log.d("=>", "$minValue : $maxValue")
            binding!!.get().filterMin.text = minValue.toString()
            binding!!.get().filterMax.text = maxValue.toString()
        }
        binding!!.get().filter.setOnClickListener { view: View? ->
            homeSearchProductViewModel!!.holder.max_price = binding!!.get().filterMax.text.toString()
            homeSearchProductViewModel!!.holder.min_price = binding!!.get().filterMin.text.toString()

            // Get Name
            homeSearchProductViewModel!!.holder.search_term = binding!!.get().setItemName.text.toString()

            // Get Price
            /*homeSearchProductViewModel.holder.min_price = binding.get().minimumEditText.getText().toString();
            homeSearchProductViewModel.holder.max_price = binding.get().maximumEditText.getText().toString();*/


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
            navigationController.navigateBackToHomeFeaturedFragmentFromFiltering(this@FilterFragment.activity!!, homeSearchProductViewModel!!.holder)
            this@FilterFragment.activity!!.finish()
        }
        binding!!.get().oneStar.setOnClickListener { v: View? ->
            if (homeSearchProductViewModel!!.holder.rating_value_one == "") {
                this@FilterFragment.selectStar(Constants.RATING_ONE)
                this@FilterFragment.unSelectStar(Constants.RATING_TWO)
                this@FilterFragment.unSelectStar(Constants.RATING_THREE)
                this@FilterFragment.unSelectStar(Constants.RATING_FOUR)
                this@FilterFragment.unSelectStar(Constants.RATING_FIVE)
            } else {
                this@FilterFragment.unSelectStar(Constants.RATING_ONE)
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_ok_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clearButton) {
            binding!!.get().setItemName.setText("")
            binding!!.get().minimumEditText.setText("")
            binding!!.get().maximumEditText.setText("")
            binding!!.get().featuredSwitch.isChecked = false
            binding!!.get().discountSwitch.isChecked = false
            homeSearchProductViewModel!!.holder.overall_rating = ""
            unSelectStar(Constants.RATING_ONE)
            unSelectStar(Constants.RATING_TWO)
            unSelectStar(Constants.RATING_THREE)
            unSelectStar(Constants.RATING_FOUR)
            unSelectStar(Constants.RATING_FIVE)

            //navigationController.navigateBackToHomeFeaturedFragmentFromFiltering(SpecialFilteringFragment.this.getActivity(), homeSearchProductViewModel.holder);
        }
        return super.onOptionsItemSelected(item)
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
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {}
    override fun onDispatched() {}
}