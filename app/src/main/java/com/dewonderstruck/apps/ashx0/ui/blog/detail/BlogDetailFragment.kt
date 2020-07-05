package com.dewonderstruck.apps.ashx0.ui.blog.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentBlogDetailBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.viewmodel.blog.BlogViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Blog
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest

class BlogDetailFragment : PSFragment() {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var blogViewModel: BlogViewModel? = null
    private var blogId: String? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentBlogDetailBinding>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_blog_detail, container, false, dataBindingComponent) as FragmentBlogDetailBinding
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
    }

    override fun initViewModels() {
        blogViewModel = ViewModelProvider(this, viewModelFactory).get(BlogViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        if (activity != null) {
            blogId = requireActivity().intent.getStringExtra(Constants.BLOG_ID)
        }
        if (blogId != null && !blogId!!.isEmpty()) {
            blogViewModel!!.setBlogByIdObj(blogId)
            blogViewModel!!.blogByIdData.observe(this, Observer { result: Resource<Blog?>? ->
                if (result != null) {
                    if (result.data != null) {
                        when (result.status) {
                            Status.SUCCESS -> binding!!.get().blog = result.data
                            Status.ERROR -> {
                                psDialogMsg!!.showErrorDialog(getString(R.string.blog_detail__error_message), getString(R.string.app__ok))
                                psDialogMsg!!.show()
                            }
                            Status.LOADING -> binding!!.get().blog = result.data
                        }
                    }
                }
            })
        }
    }
}