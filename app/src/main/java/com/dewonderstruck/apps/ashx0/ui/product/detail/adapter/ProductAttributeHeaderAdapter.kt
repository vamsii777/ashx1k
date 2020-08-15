package com.dewonderstruck.apps.ashx0.ui.product.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.databinding.CardviewHeaderBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Objects
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeDetail
import com.dewonderstruck.apps.ashx0.viewobject.ProductAttributeHeader
import java.util.*

abstract class ProductAttributeHeaderAdapter protected constructor(private val dataBindingComponent: DataBindingComponent, var basketItemHolderHashMap: Map<String, String?>, private val callback: HeaderProductClickCallBack) : DataBoundListAdapter<ProductAttributeHeader?, CardviewHeaderBinding?>(), OnItemSelectedListener {
    private val diffUtilDispatchedInterface: DiffUtilDispatchedInterface2? = null
    var currencySymbol: String? = null
    override fun createBinding(parent: ViewGroup?): CardviewHeaderBinding? {
        return DataBindingUtil
                .inflate(LayoutInflater.from(parent!!.context),
                        R.layout.cardview_header
                        , parent,
                        false,
                        dataBindingComponent)
    }

    override fun dispatched() {
        diffUtilDispatchedInterface?.onDispatched()
    }

    protected override fun bind(binding: CardviewHeaderBinding?, item: ProductAttributeHeader?) {
        if (item != null) {
            binding!!.headerproduct = item
            val context = binding.root.context
            binding.spinner.onItemSelectedListener = this
            val detail: MutableList<ProductAttributeDetail> = ArrayList()
            var selectedIndex = 0
            detail.add(ProductAttributeDetail("-1",
                    item.id,
                    item.id,
                    item.productId,
                    context.getString(R.string.product_detail__choose_zg, item.name),
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO))
            var selectedAttr: String? = ""
            if (basketItemHolderHashMap.containsKey(item.id)) {
                selectedAttr = basketItemHolderHashMap[item.id]
            }
            for (i in item.attributesDetailList!!.indices) {
                if (item.attributesDetailList!![i]!!.additionalPrice == Constants.ZERO) {
                    item.attributesDetailList!![i]!!.additionalPriceWithCurrency = ""
                } else {
                    item.attributesDetailList!![i]!!.additionalPriceWithCurrency = "( + " + currencySymbol + Constants.SPACE_STRING + item.attributesDetailList!![i].additionalPrice + " )"
                }
                Utils.psLog("""
    $selectedAttr

    """.trimIndent())
                if (item.attributesDetailList!![i].name == selectedAttr) {
                    selectedIndex = i + 1
                }
                detail.add(item.attributesDetailList!![i])
            }

            //Creating the ArrayAdapter instance having the list
            val adapter = ProductHeaderDetailAdapter(binding.root.context,
                    R.layout.spinner_header_detail, detail, dataBindingComponent)

            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.adapter = adapter
            binding.spinner.setSelection(selectedIndex)
        }
    }

    protected override fun areItemsTheSame(oldItem: ProductAttributeHeader?, newItem: ProductAttributeHeader?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.productId == newItem.productId)
    }

    protected override fun areContentsTheSame(oldItem: ProductAttributeHeader?, newItem: ProductAttributeHeader?): Boolean {
        return (Objects.equals(oldItem!!.id, newItem!!.id)
                && oldItem.productId == newItem.productId)
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        val productAttributeDetail = adapterView.getItemAtPosition(i) as ProductAttributeDetail
        callback.onClick(productAttributeDetail)
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    interface HeaderProductClickCallBack {
        fun onClick(productAttributeDetail: ProductAttributeDetail?)
    }

}