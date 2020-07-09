package com.dewonderstruck.apps.ashx0.ui.product.filtering

import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.viewobject.Category
import com.dewonderstruck.apps.ashx0.viewobject.SubCategory
import java.util.*

class Grouping {
    var catId: String? = null
    var subCatId: String? = null
    var map = LinkedHashMap<Category, List<SubCategory>>()
    fun group(categoryList: List<Category>, subCategoryList: List<SubCategory>): LinkedHashMap<Category, List<SubCategory>> {
        map.clear()
        for (i in categoryList.indices) {
            val subCategories: MutableList<SubCategory> = ArrayList()
            val subCategory = SubCategory(Constants.ZERO, "", Constants.CATEGORY_ALL, "", "", "", "", "", "", "" +
                    "", "", null, null)
            subCategories.add(0, subCategory)
            catId = categoryList[i].id
            for (j in subCategoryList.indices) {
                subCatId = subCategoryList[j].catId
                if (catId == subCatId) {
                    subCategories.add(subCategoryList[j])
                }
            }
            if (!subCategories.isEmpty()) {
                map[categoryList[i]] = subCategories
            }
        }
        return map
    }
}