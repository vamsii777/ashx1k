package com.dewonderstruck.apps.ashx0.ui.blog.list

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ActivityBlogListBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity
class BlogListActivity : PSAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBlogListBinding = DataBindingUtil.setContentView(this, R.layout.activity_blog_list)
        initUI(binding)
    }

    private fun initUI(binding: ActivityBlogListBinding) {

        // Toolbar
        initToolbar(binding.toolbar, resources.getString(R.string.blog_list__title))

        // setup Fragment
        setupFragment(BlogListFragment())
    }
}