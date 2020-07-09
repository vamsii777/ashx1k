package com.dewonderstruck.apps.ashx0.ui.gallery.detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentGalleryDetailBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.TouchImageView
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.image.ImageViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Image
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource

/**
 * Gallery Detail Fragment
 */
class GalleryDetailFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentGalleryDetailBinding>? = null
    private var imageViewModel: ImageViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentGalleryDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery_detail, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        binding!!.get().viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(arg0: Int) {}
            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageSelected(currentPage: Int) {
                var currentPage = currentPage
                if (imageViewModel!!.newsImageList != null) {
                    if (currentPage >= imageViewModel!!.newsImageList.size) {
                        currentPage = currentPage % imageViewModel!!.newsImageList.size
                    }
                    binding!!.get().imgDesc.text = imageViewModel!!.newsImageList[currentPage].imgDesc
                }
            }
        })
    }

    override fun initViewModels() {
        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel::class.java)
    }

    override fun initAdapters() {}

    //endregion
    //region Pager Adapter Class
    @SuppressLint("UseRequireInsteadOfGet")
    override fun initData() {
        try {
            if (activity != null) {
                imageViewModel!!.imgType = activity!!.intent.getStringExtra(Constants.IMAGE_TYPE)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting intent.", e)
        }
        try {
            if (activity != null) {
                imageViewModel!!.id = activity!!.intent.getStringExtra(Constants.PRODUCT_ID)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting intent.", e)
        }
        try {
            if (activity != null) {
                imageViewModel!!.imgId = activity!!.intent.getStringExtra(Constants.IMAGE_ID)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting intent.", e)
        }
        val imageListLiveData = imageViewModel!!.imageListLiveData
        imageViewModel!!.setImageParentId(imageViewModel!!.imgType, imageViewModel!!.id)
        imageListLiveData.observe(this, Observer { listResource: Resource<List<Image?>?>? ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data")

                // Update the data
                imageViewModel!!.newsImageList = listResource.data
                var selectedItemPosition = 0
                for (i in imageViewModel!!.newsImageList.indices) {
                    if (imageViewModel!!.newsImageList[i].imgId == imageViewModel!!.imgId) {
                        selectedItemPosition = i
                        break
                    }
                }
                binding!!.get().viewPager.adapter = TouchImageAdapter()
                try {
                    binding!!.get().viewPager.currentItem = selectedItemPosition
                    binding!!.get().imgDesc.text = imageViewModel!!.newsImageList[selectedItemPosition].imgDesc
                } catch (e: Exception) {
                    Utils.psErrorLog("", e)
                }
            } else {
                Utils.psLog("Empty Data")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity != null) {
            fixLeakCanary696(activity)
        }
    }

    internal inner class TouchImageAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return if (imageViewModel!!.newsImageList != null) {
                imageViewModel!!.newsImageList.size
            } else 0
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            var position = position
            val imgView = TouchImageView(container.context)
            if (imageViewModel!!.newsImageList != null) {
                if (position >= imageViewModel!!.newsImageList.size) {
                    position = position % imageViewModel!!.newsImageList.size
                }
                if (activity != null) {
                    dataBindingComponent.fragmentBindingAdapters.bindFullImage(imgView, imageViewModel!!.newsImageList[position].imgPath)
                    container.addView(imgView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                }
            }
            return imgView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    } //endregion

    companion object {
        //endregion
        //region Override Methods
        private fun fixLeakCanary696(context: Context?) {
//        if (!isEmui()) {
//            Lo "not emui");
//            return;
//        }

            //https://github.com/square/leakcanary/issues/696
            try {
                val clazz = Class.forName(Constants.GALLERY_BOOST)
                Utils.psLog("clazz $clazz")
                val _sGestureBoostManager = clazz.getDeclaredField(Constants.GALLERY_GESTURE)
                _sGestureBoostManager.isAccessible = true
                val _mContext = clazz.getDeclaredField(Constants.GALLERY_CONTEXT)
                _mContext.isAccessible = true
                val sGestureBoostManager = _sGestureBoostManager[null]
                if (sGestureBoostManager != null) {
                    _mContext[sGestureBoostManager] = context
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}