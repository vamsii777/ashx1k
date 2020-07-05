package com.dewonderstruck.apps.ashx0.ui.blog.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentBlogListBinding
import com.dewonderstruck.apps.ashx0.ui.blog.list.adapter.BlogListAdapter
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.blog.BlogViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Blog
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class BlogListFragment : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var blogViewModel: BlogViewModel? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentBlogListBinding>? = null
    private var adapter: AutoClearedValue<BlogListAdapter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: FragmentBlogListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog_list, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        binding!!.get().shopListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null) {
                    val lastPosition = layoutManager
                            .findLastVisibleItemPosition()
                    if (lastPosition == adapter!!.get().itemCount - 1) {
                        if (!binding!!.get().loadingMore && !blogViewModel!!.forceEndLoading) {
                            blogViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                            val limit = Config.LIST_NEW_FEED_COUNT
                            blogViewModel!!.offset = blogViewModel!!.offset + limit
                            blogViewModel!!.setLoadingState(true)
                            blogViewModel!!.setNextPageNewsFeedObj(Config.LIST_NEW_FEED_COUNT.toString(), blogViewModel!!.offset.toString())
                        }
                    }
                }
            }
        })
        binding!!.get().swipeRefresh.setColorSchemeColors(resources.getColor(R.color.view__primary_line))
        binding!!.get().swipeRefresh.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.global__primary))
        binding!!.get().swipeRefresh.setOnRefreshListener {
            blogViewModel!!.loadingDirection = Utils.LoadingDirection.top

            // reset productViewModel.offset
            blogViewModel!!.offset = 0

            // reset productViewModel.forceEndLoading
            blogViewModel!!.forceEndLoading = false
            blogViewModel!!.setNewsFeedObj(Config.LIST_NEW_FEED_COUNT.toString(), blogViewModel!!.offset.toString())
        }
    }

    override fun initViewModels() {
        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = BlogListAdapter(dataBindingComponent, BlogListAdapter.NewsClickCallback { newsFeed: Blog -> navigationController.navigateToBlogDetailActivity(this@BlogListFragment.activity, newsFeed.id) }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().shopListRecyclerView.adapter = adapter!!.get()
    }

    override fun initData() {
        blogViewModel!!.setNewsFeedObj(Config.LIST_NEW_FEED_COUNT.toString(), blogViewModel!!.offset.toString())
        blogViewModel!!.newsFeedData.observe(this, Observer { result: Resource<List<Blog>>? ->
            if (result != null) {
                when (result.status) {
                    Status.SUCCESS -> {
                        replaceNewsFeedList(result.data!!)
                        blogViewModel!!.setLoadingState(false)
                    }
                    Status.LOADING -> replaceNewsFeedList(result.data!!)
                    Status.ERROR -> blogViewModel!!.setLoadingState(false)
                }
            }
        })
        blogViewModel!!.nextPageNewsFeedData.observe(this, Observer { state: Resource<Boolean?>? ->
            if (state != null) {
                if (state.status == Status.ERROR) {
                    blogViewModel!!.setLoadingState(false)
                    blogViewModel!!.forceEndLoading = true
                }
            }
        })
        blogViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            binding!!.get().loadingMore = blogViewModel!!.isLoading
            if (loadingState != null && !loadingState) {
                binding!!.get().swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun replaceNewsFeedList(blogs: List<Blog>) {
        adapter!!.get().replace(blogs)
        binding!!.get().executePendingBindings()
    }

    override fun onDispatched() {
        if (blogViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().shopListRecyclerView != null) {
                val layoutManager = binding!!.get().shopListRecyclerView.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }
}