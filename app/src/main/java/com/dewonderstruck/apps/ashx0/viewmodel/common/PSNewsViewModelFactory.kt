package com.dewonderstruck.apps.ashx0.viewmodel.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dewonderstruck.apps.ashx0.utils.Utils
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 11/16/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
class PSNewsViewModelFactory @Inject internal constructor(creators: Map<Class<out ViewModel?>?, Provider<ViewModel>?>) : ViewModelProvider.Factory {
    private val creators: Map<Class<out ViewModel?>?, Provider<ViewModel>?>
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        requireNotNull(creator) { "unknown model class $modelClass" }
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    init {
        Utils.psLog("Inside PSNewsViewModelFactory")
        this.creators = creators
    }
}