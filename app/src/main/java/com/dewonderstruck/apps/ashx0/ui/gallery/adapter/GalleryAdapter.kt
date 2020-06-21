package com.dewonderstruck.apps.ashx0.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemGalleryAdapterBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Image

/**
 * Created by Vamsi Madduluri on 12/6/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class GalleryAdapter(private val dataBindingComponent: DataBindingComponent,
                     private val callback: ImageClickCallback?,
                     private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface?) : DataBoundListAdapter<Image?, ItemGalleryAdapterBinding?>() {
    override fun createBinding(parent: ViewGroup?): ItemGalleryAdapterBinding {
        val binding: ItemGalleryAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.item_gallery_adapter, parent, false,
                        dataBindingComponent)
        binding.root.setOnClickListener { v: View? ->
            val image = binding.image
            if (image != null && callback != null) {
                Utils.psLog("Clicked Image" + image.imgDesc)
                callback.onClick(image)
            }
        }
        return binding
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: ItemGalleryAdapterBinding?, item: Image) {
        binding!!.image = item
    }

    protected override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return Objects.equals(oldItem.imgId, newItem.imgId)
    }

    protected override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return Objects.equals(oldItem.imgId, newItem.imgId)
    }

    interface ImageClickCallback {
        fun onClick(item: Image?)
    }

}