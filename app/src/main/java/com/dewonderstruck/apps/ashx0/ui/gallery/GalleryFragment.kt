package com.dewonderstruck.apps.ashx0.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentGalleryBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.ui.gallery.adapter.GalleryAdapter
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.image.ImageViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Image
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource

/**
 * GalleryFragment
 */
class GalleryFragment : PSFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var imageViewModel: ImageViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentGalleryBinding>? = null
    private var adapter: AutoClearedValue<GalleryAdapter>? = null

    //private String imgParentId = "";
    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        binding!!.get().newsImageList.setHasFixedSize(true)
        binding!!.get().newsImageList.isNestedScrollingEnabled = false
        val mLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding!!.get().newsImageList.layoutManager = mLayoutManager
    }

    override fun initViewModels() {
        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel::class.java)
    }

    private fun imageClicked(image: Image) {
        navigationController.navigateToDetailGalleryActivity(requireActivity(), imageViewModel!!.imgType, imageViewModel!!.id, image.imgId)
    }

    override fun initAdapters() {
        // ViewModel need to get from ViewModelProviders
        val nvAdapter = GalleryAdapter(dataBindingComponent, object: GalleryAdapter.ImageClickCallback {
            override fun onClick(item: Image?) {
                imageClicked(item!!)
            }
        }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().newsImageList.adapter = nvAdapter
    }

    override fun onDispatched() {}
    override fun initData() {
        //load category
        try {
            if (activity != null) {
                imageViewModel!!.imgType = requireActivity().intent.getStringExtra(Constants.IMAGE_TYPE)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting intent.", e)
        }
        try {
            if (activity != null) {
                imageViewModel!!.id = requireActivity().intent.getStringExtra(Constants.IMAGE_PARENT_ID)
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in getting intent.", e)
        }
        val imageListLiveData = imageViewModel!!.imageListLiveData
        imageViewModel!!.setImageParentId(imageViewModel!!.imgType, imageViewModel!!.id)
        imageListLiveData.observe(this, Observer { listResource: Resource<List<Image?>>? ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data")

                //fadeIn Animation
                fadeIn(binding!!.get().root)

                // Update the data
                adapter!!.get().replace(listResource.data)
                binding!!.get().executePendingBindings()
            } else {
                Utils.psLog("Empty Data")
            }
        })
    } //endregion
}