package com.dewonderstruck.apps.ashx0.ui.danceoholics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentDanceoholicsBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.ui.danceoholics.data.SliderAdapter
import com.dewonderstruck.apps.ashx0.ui.danceoholics.data.SliderItem
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.google.android.gms.ads.AdRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_danceoholics.*
import java.util.*
import kotlin.collections.ArrayList


class DanceoholicsFragment : DeFragment() {


    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("SliderData").child("DncAbout")

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentDanceoholicsBinding>? = null
    private var viewPager2: ViewPager2? = null
    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentDanceoholicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_danceoholics, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        initUIAndActions()
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

        viewPager2 = binding!!.get().slider1;

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                    binding!!.get().fragDnc.visibility = View.VISIBLE
                    binding!!.get().progressBar4.visibility = View.GONE
                    val desc = ds.child("desc").getValue().toString()
                    val title = ds.child("title").getValue().toString()
                    val website = ds.child("web").getValue().toString()
                    val fb = ds.child("fb").getValue().toString()
                    val insta = ds.child("insta").getValue().toString()
                    val yutube = ds.child("yutube").getValue().toString()
                    val mail = ds.child("email").getValue().toString()
                    val dp = ds.child("dp").getValue().toString()
                    val zero = ds.child("zero").getValue().toString()
                    val one = ds.child("one").getValue().toString()
                    val two = ds.child("two").getValue().toString()
                    val three = ds.child("three").getValue().toString()

                    Picasso.get().load(dp).into(aboutDefaultIcon)
                    binding!!.get().titleTextView.setText(title)
                    binding!!.get().descTextView.setText(desc)
                    binding!!.get().WebsiteTextView.setText(website)
                    binding!!.get().facebookTextView.setText(fb)
                    binding!!.get().instaTextView.setText(insta)
                    binding!!.get().youtubeTextView.setText(yutube)
                     binding!!.get().emailTextView.setText(mail)

                val slideritem: ArrayList<Any> = ArrayList<Any>()
                    slideritem.add(SliderItem(zero))
                    slideritem.add(SliderItem(one))
                    slideritem.add(SliderItem(two))
                    slideritem.add(SliderItem(three))
                    viewPager2!!.setAdapter(SliderAdapter(slideritem, viewPager2!!))

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


                //Log.d(requireContext().toString(), "$desc / $title")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(requireContext().toString(), "Failed to read value.", error.toException())
            }
        })




        viewPager2!!.setClipToPadding(false)
        viewPager2!!.setOffscreenPageLimit(3)
        viewPager2!!.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)

        val compositePageTransformer: CompositePageTransformer  = CompositePageTransformer();
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        viewPager2!!.setPageTransformer(compositePageTransformer)

        /*After setting the adapter use the timer */
        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === 5 - 1) {
                currentPage = 0
            }
            viewPager2!!.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)

    }

    override fun initViewModels() {
        //contactUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactUsViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {}


}