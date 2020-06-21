package com.dewonderstruck.apps.ashx0.ui.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentShopProfileBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.product.BasketViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.shop.ShopViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.Shop
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

class ShopProfileFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var shopViewModel: ShopViewModel? = null
    private val basketMenuItem: MenuItem? = null
    private var basketViewModel: BasketViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentShopProfileBinding>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentShopProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_profile, container, false, dataBindingComponent)
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
            navigationController.navigateToBasketList(activity)
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
        binding!!.get().aboutImageView.setOnClickListener { view: View? -> navigationController.navigateToGalleryActivity(activity, Constants.IMAGE_TYPE_ABOUT, shopViewModel!!.selectedShopId) }


        //For Phone 1
        binding!!.get().phoneTitleTextView.setOnClickListener { view1: View? ->
            val number = binding!!.get().phoneTitleTextView.text.toString()
            if (!(number.trim { it <= ' ' }.isEmpty() || number.trim { it <= ' ' } == "-")) {
                Utils.callPhone(this@ShopProfileFragment, number)
            }
        }


        //For phone 2
        binding!!.get().phone1TextView.setOnClickListener { view: View? ->
            val number1 = binding!!.get().phone1TextView.text.toString()
            if (!(number1.trim { it <= ' ' }.isEmpty() || number1.trim { it <= ' ' } == "-")) {
                Utils.callPhone(this@ShopProfileFragment, number1)
            }
        }


        //For phone 3
        binding!!.get().phone2TextView.setOnClickListener { view: View? ->
            val number2 = binding!!.get().phone2TextView.text.toString()
            if (!(number2.trim { it <= ' ' }.isEmpty() || number2.trim { it <= ' ' } == "-")) {
                Utils.callPhone(this@ShopProfileFragment, number2)
            }
        }


        //For phone 4
        binding!!.get().phone3TextView.setOnClickListener { view: View? ->
            val number3 = binding!!.get().phone3TextView.text.toString()
            if (!(number3.trim { it <= ' ' }.isEmpty() || number3.trim { it <= ' ' } == "-")) {
                Utils.callPhone(this@ShopProfileFragment, number3)
            }
        }

        //For email
        binding!!.get().emailTextView.setOnClickListener { view: View? ->
            try {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = Constants.EMAIL_TYPE
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(binding!!.get().emailTextView.text.toString()))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.hello))
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)))
            } catch (e: Exception) {
                Utils.psErrorLog("doEmail", e)
            }
        }

        //For website
        binding!!.get().WebsiteTextView.setOnClickListener { view: View? ->
            if (binding!!.get().WebsiteTextView.text.toString().startsWith(Constants.HTTP) || binding!!.get().WebsiteTextView.text.toString().startsWith(Constants.HTTPS)) {
                val url = binding!!.get().WebsiteTextView.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } else {
                Toast.makeText(activity, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }

        //For facebook
        binding!!.get().facebookTextView.setOnClickListener { view: View? ->
            if (binding!!.get().facebookTextView.text.toString().startsWith(Constants.HTTP) || binding!!.get().facebookTextView.text.toString().startsWith(Constants.HTTPS)) {
                val url = binding!!.get().facebookTextView.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } else {
                Toast.makeText(activity, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }

        //For google plus
        binding!!.get().gplusTextView.setOnClickListener { view: View? ->
            if (binding!!.get().gplusTextView.text.toString().startsWith(Constants.HTTP) || binding!!.get().gplusTextView.text.toString().startsWith(Constants.HTTPS)) {
                val url = binding!!.get().gplusTextView.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } else {
                Toast.makeText(activity, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }

        //For twitter
        binding!!.get().twitterTextView.setOnClickListener { view: View? ->
            if (binding!!.get().twitterTextView.text.toString().startsWith(Constants.HTTP) || binding!!.get().twitterTextView.text.toString().startsWith(Constants.HTTPS)) {
                val url = binding!!.get().twitterTextView.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } else {
                Toast.makeText(activity, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }

        //For instagram
        binding!!.get().instaTextView.setOnClickListener { view: View? ->
            if (binding!!.get().instaTextView.text.toString().startsWith(Constants.HTTP) || binding!!.get().instaTextView.text.toString().startsWith(Constants.HTTPS)) {
                val url = binding!!.get().instaTextView.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } else {
                Toast.makeText(activity, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }

        //For youtube
        binding!!.get().youtubeTextView.setOnClickListener { view: View? ->
            if (binding!!.get().youtubeTextView.text.toString().startsWith(Constants.HTTP) || binding!!.get().youtubeTextView.text.toString().startsWith(Constants.HTTPS)) {
                val url = binding!!.get().youtubeTextView.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } else {
                Toast.makeText(activity, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }

        /*//For pinterest
        binding.get().pinterestTextView.setOnClickListener(view -> {

            if (binding.get().pinterestTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().pinterestTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().pinterestTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    override fun initViewModels() {
        basketViewModel = ViewModelProvider(this, viewModelFactory).get(BasketViewModel::class.java)
        shopViewModel = ViewModelProvider(this, viewModelFactory).get(ShopViewModel::class.java)
    }

    override fun initAdapters() {}

    //  private  void replaceAboutUsData()
    override fun initData() {
        basketData()
        shopViewModel!!.setShopObj(Config.API_KEY)
        shopViewModel!!.shopData.observe(this, Observer<Resource<Shop>?> { resource ->
            if (resource != null) {
                Utils.psLog("Got Data" + resource.message + resource.toString())
                when (resource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {
                            fadeIn(binding!!.get().root)
                            binding!!.get().shop = resource.data
                            setAboutUsData(resource.data)
                        }
                    Status.SUCCESS ->                             // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            binding!!.get().shop = resource.data
                            setAboutUsData(resource.data)
                        }
                    Status.ERROR -> {
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.")
            } else {
                Utils.psLog("No Data of About Us.")
            }
        })
    }

    private fun basketData() {
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
    }

    private fun setAboutUsData(shop: Shop?) {
        binding!!.get().shop = shop
        shopViewModel!!.selectedShopId = shop!!.id

        // For Contact
        // For SourceAddress
        binding!!.get().textView36.text = binding!!.get().textView36.text.toString()
        binding!!.get().textView39.text = binding!!.get().textView39.text.toString()

        // For Address 1
        if (shop.address1 == "") {
            binding!!.get().address1.text = Constants.DASH
        } else {
            binding!!.get().address1.text = shop.address1
        }

        // For Address 2
        if (shop.address2 == "") {
            binding!!.get().address2.text = Constants.DASH
        } else {
            binding!!.get().address2.text = shop.address2
        }

        // For Address 3
        if (shop.address3 == "") {
            binding!!.get().address3.text = Constants.DASH
        } else {
            binding!!.get().address3.text = shop.address3
        }

        /*//For Pinterest.com
        if (shop.pinterest.equals("")) {
            binding.get().pinterestTextView.setText(Constants.DASH);

        } else {
            binding.get().pinterestTextView.setText(shop.pinterest);
        }*/

        // For youtube.com
        if (shop.youtube == "") {
            binding!!.get().youtubeTextView.text = Constants.DASH
        } else {
            binding!!.get().youtubeTextView.text = shop.youtube
        }

        // For instagram.com
        if (shop.instagram == "") {
            binding!!.get().instaTextView.text = Constants.DASH
        } else {
            binding!!.get().instaTextView.text = shop.instagram
        }

        // For twitter.com
        if (shop.twitter == "") {
            binding!!.get().twitterTextView.text = Constants.DASH
        } else {
            binding!!.get().twitterTextView.text = shop.twitter
        }

        /*// For googleplus.com
        if (shop.googlePlus.equals("")) {
            binding.get().gplusTextView.setText(Constants.DASH);
        } else {
            binding.get().gplusTextView.setText(shop.googlePlus);
        }
*/
        // For facebook.com
        if (shop.facebook == "") {
            binding!!.get().facebookTextView.text = Constants.DASH
        } else {
            binding!!.get().facebookTextView.text = shop.facebook
        }

        // For shop phone
        if (shop.aboutPhone1 == "") {
            binding!!.get().phoneTitleTextView.text = Constants.DASH
        } else {
            binding!!.get().phoneTitleTextView.text = shop.aboutPhone1
        }

        // For website.com
        if (shop.aboutWebsite == "") {
            binding!!.get().WebsiteTextView.text = Constants.DASH
        } else {
            binding!!.get().WebsiteTextView.text = shop.aboutWebsite
        }

        // For email
        if (shop.email == "") {
            binding!!.get().emailTextView.text = Constants.DASH
        } else {
            binding!!.get().emailTextView.text = shop.email
        }

        // For phone1
        if (shop.aboutPhone1 == "") {
            binding!!.get().phone1TextView.text = Constants.DASH
        } else {
            binding!!.get().phone1TextView.text = shop.aboutPhone1
        }

        // For phone2
        if (shop.aboutPhone2 == "") {
            binding!!.get().phone2TextView.text = Constants.DASH
        } else {
            binding!!.get().phone2TextView.text = shop.aboutPhone2
        }

        // For phone3
        if (shop.aboutPhone3 == "") {
            binding!!.get().phone3TextView.text = Constants.DASH
        } else {
            binding!!.get().phone3TextView.text = shop.aboutPhone3
        }


        // For description
        if (shop.description == "") {
            binding!!.get().descTextView.text = Constants.DASH
        } else {
            binding!!.get().descTextView.text = shop.description
        }

        // For title
        if (shop.name == "") {
            binding!!.get().titleTextView.text = Constants.DASH
        } else {
            binding!!.get().titleTextView.text = shop.name
        }
    } //endregion

    companion object {
        private const val REQUEST_CALL = 1
    }
}