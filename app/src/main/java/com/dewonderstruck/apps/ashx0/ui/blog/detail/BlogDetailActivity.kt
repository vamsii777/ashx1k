package com.dewonderstruck.apps.ashx0.ui.blog.detail

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityBlogDetailBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity

class BlogDetailActivity : PSAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBlogDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_blog_detail)
        initUI(binding)
    }

    private fun initUI(binding: ActivityBlogDetailBinding) {

        // Toolbar
        initToolbar(binding.toolbar, resources.getString(R.string.blog_detail__title))

        // setup Fragment
        setupFragment(BlogDetailFragment())
    }
}