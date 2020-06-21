package com.dewonderstruck.apps.ashx0.binding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment?) : DataBindingComponent {
    private val adapter: FragmentBindingAdapters
    override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
        return adapter
    }

    init {
        adapter = FragmentBindingAdapters(fragment)
    }
}