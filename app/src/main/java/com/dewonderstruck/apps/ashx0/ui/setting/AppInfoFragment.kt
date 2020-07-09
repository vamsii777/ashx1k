package com.dewonderstruck.apps.ashx0.ui.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentAppInfoBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.aboutus.AboutUsViewModel
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

/**
 * A simple [Fragment] subclass.
 */
class AppInfoFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var aboutUsViewModel: AboutUsViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentAppInfoBinding>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentAppInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_info, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        setHasOptionsMenu(true)
        return binding!!.get().root
    }

    override fun initUIAndActions() {

        //For phone 3
        binding!!.get().phone2TextView.setOnClickListener { view: View? ->
            val number2 = binding!!.get().phone2TextView.text.toString()
            if (ContextCompat.checkSelfPermission(binding!!.get().root.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (this.activity != null) {
                    ActivityCompat.requestPermissions(activity!!, arrayOf(
                            Manifest.permission.CALL_PHONE
                    ), REQUEST_CALL)
                }
            } else {
                val dial = "tel:$number2"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        }


        //For phone 4
        binding!!.get().phone3TextView.setOnClickListener { view: View? ->
            val number3 = binding!!.get().phone3TextView.text.toString()
            if (ContextCompat.checkSelfPermission(binding!!.get().root.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (this.activity != null) {
                    ActivityCompat.requestPermissions(activity!!, arrayOf(
                            Manifest.permission.CALL_PHONE
                    ), REQUEST_CALL)
                }
            } else {
                val dial = "tel:$number3"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        }

        //For website
        binding!!.get().WebsiteTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().WebsiteTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //For facebook
        binding!!.get().facebookTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().facebookTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //For google plus
        binding!!.get().gplusTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().gplusTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //For twitter
        binding!!.get().twitterTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().twitterTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //For instagram
        binding!!.get().instaTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().instaTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //For youtube
        binding!!.get().youtubeTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().youtubeTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //For pinterest
        binding!!.get().pinterestTextView.setOnClickListener { view: View? ->
            val url = binding!!.get().pinterestTextView.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    override fun initViewModels() {
        aboutUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutUsViewModel::class.java)
    }

    override fun initAdapters() {}

    //  private  void replaceAboutUsData()
    override fun initData() {
        aboutUsViewModel!!.setAboutUsObj("about us")
        aboutUsViewModel!!.aboutUsData.observe(this, Observer { resource: Resource<AboutUs?>? ->
            if (resource != null) {
                when (resource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {
                            fadeIn(binding!!.get().root)
                        }
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (resource.data != null) {
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

    private fun setAboutUsData(aboutUs: AboutUs?) {
        binding!!.get().aboutUs = aboutUs

        //For Pinterest.com
        if (aboutUs!!.pinterest == "") {
            binding!!.get().pinterestImage.visibility = View.GONE
            binding!!.get().pinterestTextView.visibility = View.GONE
            binding!!.get().pinterestTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().pinterestTextView.text = aboutUs.pinterest
            binding!!.get().pinterestImage.visibility = View.VISIBLE
            binding!!.get().pinterestTextView.visibility = View.VISIBLE
            binding!!.get().pinterestTitleTextView.visibility = View.VISIBLE
        }

        // For youtube.com
        if (aboutUs.youtube == "") {
            binding!!.get().youtubeImage.visibility = View.GONE
            binding!!.get().youtubeTextView.visibility = View.GONE
            binding!!.get().youTubeTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().youtubeTextView.text = aboutUs.youtube
            binding!!.get().youtubeImage.visibility = View.VISIBLE
            binding!!.get().youtubeTextView.visibility = View.VISIBLE
            binding!!.get().youTubeTitleTextView.visibility = View.VISIBLE
        }

        // For instagram.com
        if (aboutUs.instagram == "") {
            binding!!.get().instagramImage.visibility = View.GONE
            binding!!.get().instaTextView.visibility = View.GONE
            binding!!.get().instaTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().instaTextView.text = aboutUs.instagram
            binding!!.get().instagramImage.visibility = View.VISIBLE
            binding!!.get().instaTextView.visibility = View.VISIBLE
            binding!!.get().instaTitleTextView.visibility = View.VISIBLE
        }

        // For twitter.com
        if (aboutUs.twitter == "") {
            binding!!.get().twitterImage.visibility = View.GONE
            binding!!.get().twitterTextView.visibility = View.GONE
            binding!!.get().twitterTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().twitterTextView.text = aboutUs.twitter
            binding!!.get().twitterImage.visibility = View.VISIBLE
            binding!!.get().twitterTextView.visibility = View.VISIBLE
            binding!!.get().twitterTitleTextView.visibility = View.VISIBLE
        }

        // For googleplus.com
        if (aboutUs.googlePlus == "") {
            binding!!.get().gplusImage.visibility = View.GONE
            binding!!.get().gplusTextView.visibility = View.GONE
            binding!!.get().gplusTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().gplusTextView.text = aboutUs.googlePlus
            binding!!.get().gplusImage.visibility = View.VISIBLE
            binding!!.get().gplusTextView.visibility = View.VISIBLE
            binding!!.get().gplusTitleTextView.visibility = View.VISIBLE
        }

        // For facebook.com
        if (aboutUs.facebook == "") {
            binding!!.get().facebookImage.visibility = View.GONE
            binding!!.get().facebookTextView.visibility = View.GONE
            binding!!.get().facebookTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().facebookTextView.text = aboutUs.facebook
            binding!!.get().facebookImage.visibility = View.VISIBLE
            binding!!.get().facebookTextView.visibility = View.VISIBLE
            binding!!.get().facebookTitleTextView.visibility = View.VISIBLE
        }

        // For website.com
        if (aboutUs.aboutWebsite == "") {
            binding!!.get().webImage.visibility = View.GONE
            binding!!.get().WebsiteTextView.visibility = View.GONE
            binding!!.get().websiteTitleTextView.visibility = View.GONE
        } else {
            binding!!.get().WebsiteTextView.text = aboutUs.aboutWebsite
            binding!!.get().webImage.visibility = View.VISIBLE
            binding!!.get().WebsiteTextView.visibility = View.VISIBLE
            binding!!.get().websiteTitleTextView.visibility = View.VISIBLE
        }

        // For description
        if (aboutUs.aboutDescription == "") {
            binding!!.get().descTextView.visibility = View.GONE
        } else {
            binding!!.get().descTextView.text = aboutUs.aboutDescription
            binding!!.get().descTextView.visibility = View.VISIBLE
        }

        // For title
        if (aboutUs.aboutTitle == "") {
            binding!!.get().titleTextView.visibility = View.GONE
        } else {
            binding!!.get().titleTextView.text = aboutUs.aboutTitle
            binding!!.get().titleTextView.visibility = View.VISIBLE
        }
    } //endregion

    companion object {
        private const val REQUEST_CALL = 1
    }
}