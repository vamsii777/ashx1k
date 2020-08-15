package com.dewonderstruck.apps.ashx0.ui.product.filtering.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.ItemCategoryFilterBinding
import com.dewonderstruck.apps.ashx0.databinding.ItemSubCategoryFilterBinding
import com.dewonderstruck.apps.ashx0.ui.product.filtering.Grouping
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory
import java.util.*

class CategoryAdapter(private val dataBindingComponent: DataBindingComponent,
                      private val callback: filteringClickCallback, private var catId: String, private var subCatId: String) : BaseExpandableListAdapter() {
    private var categoryList: List<Category>? = ArrayList()
    private var subCategoryListOrg: List<SubCategory> = ArrayList()
    private var map = LinkedHashMap<Category, List<SubCategory>>()
    private val grouping = Grouping()
    private var selectedView: TextView? = null
    fun replaceCategory(categoryList: List<Category>?) {
        this.categoryList = categoryList
        if (categoryList != null && categoryList.size != 0) {
            map = grouping.group(categoryList, subCategoryListOrg)
        }
    }

    fun replaceSubCategory(subCategoryList: List<SubCategory>) {
        subCategoryListOrg = subCategoryList
        if (categoryList!!.size != 0 && subCategoryListOrg.size != 0) {
            map = grouping.group(categoryList!!, subCategoryListOrg)
        }
    }

    override fun getGroupCount(): Int {
        return if (categoryList == null) {
            0
        } else categoryList!!.size
    }

    override fun getChildrenCount(i: Int): Int {
        val sub: List<SubCategory>?
        if (categoryList != null) {
            sub = map[categoryList!![i]]
            if (sub != null) {
                return sub.size
            }
        }
        return 0
    }

    override fun getGroup(i: Int): Any {
        return categoryList!![i]
    }

    override fun getChild(i: Int, i1: Int): SubCategory {
        val subCategoryList = map[categoryList!![i]]
        return subCategoryList!!.get(i1)
    }

    override fun getGroupId(i: Int): Long {
        return i.toLong()
    }

    override fun getChildId(i: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onGroupCollapsed(groupPosition: Int) {
        super.onGroupCollapsed(groupPosition)
    }

    override fun onGroupExpanded(groupPosition: Int) {
        super.onGroupExpanded(groupPosition)
    }

    override fun getGroupView(i: Int, isExpanded: Boolean, view: View, viewGroup: ViewGroup): View {
        val binding: ItemCategoryFilterBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_category_filter, viewGroup, false, dataBindingComponent)
        binding.category = categoryList!![i]
        if (categoryList!![i].id == catId) {
            binding.groupview.setBackgroundColor(viewGroup.resources.getColor(R.color.md_green_50))
        }
        if (isExpanded) {
            Utils.toggleUporDown(binding.dropdownimage)
        }
        return binding.root
    }

    override fun getChildView(i: Int, childPosition: Int, b: Boolean, view: View, viewGroup: ViewGroup): View {
        val subCategoryItemBinding: ItemSubCategoryFilterBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_sub_category_filter, viewGroup, false, dataBindingComponent)
        val subCategory = getChild(i, childPosition)
        subCategoryItemBinding.subcategory = subCategory
        if (categoryList!![i].id == catId && subCategory.id == subCatId) {
            subCategoryItemBinding.subcategoryItem.setCompoundDrawablesWithIntrinsicBounds(null, null, viewGroup.resources.getDrawable(R.drawable.baseline_check_circle_24), null)
            selectedView = subCategoryItemBinding.subcategoryItem
        }
        subCategoryItemBinding.root.setOnClickListener { view1: View? ->
            if (selectedView != null) {
                selectedView!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            subCategoryItemBinding.subcategoryItem.setCompoundDrawablesWithIntrinsicBounds(null, null, viewGroup.resources.getDrawable(R.drawable.baseline_check_circle_24), null)
            catId = categoryList!![i].id
            subCatId = if (childPosition != 0) {
                subCategory.id
            } else {
                Constants.ZERO
            }
            callback.onClick(catId, subCatId)
            (viewGroup.context as Activity).finish()
        }
        return subCategoryItemBinding.root
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }

    interface filteringClickCallback {
        fun onClick(catId: String?, subCatId: String?)
    }

}