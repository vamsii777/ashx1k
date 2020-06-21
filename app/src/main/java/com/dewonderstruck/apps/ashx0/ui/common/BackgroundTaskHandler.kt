package com.dewonderstruck.apps.ashx0.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

/**
 * Created by Vamsi Madduluri on 12/5/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
open class BackgroundTaskHandler : Observer<Resource<Boolean>?> {
    @JvmField
    protected var holdLiveData: LiveData<Resource<Boolean>>? = null
    @JvmField
    val loadingState = MutableLiveData<LoadingState>()
    protected var limit: String? = null
    protected var offset: String? = null
    protected open var repository: PSRepository? = null
    private var hasMore = false

    protected constructor(repository: PSRepository?) {
        this.repository = repository
        reset()
    }

    constructor() {
        reset()
    }

    fun save(obj: Any?) {
        if (obj == null) {
            return
        }
        unregister()
        holdLiveData = repository!!.save(obj)
        loadingState.setValue(LoadingState(true, null))
        holdLiveData!!.observeForever(this)
    }

    fun delete(obj: Any?) {
        if (obj == null) {
            return
        }
        unregister()
        holdLiveData = repository!!.delete(obj)
        loadingState.setValue(LoadingState(true, null))
        holdLiveData!!.observeForever(this)
    }

    override fun onChanged(result: Resource<Boolean>?) {
        if (result == null) {
            reset()
        } else {
            when (result.status) {
                Status.SUCCESS -> {
                    hasMore = java.lang.Boolean.TRUE == result.data
                    unregister()
                    loadingState.setValue(LoadingState(false, null))
                }
                Status.ERROR -> {
                    hasMore = true
                    unregister()
                    loadingState.setValue(LoadingState(false,
                            result.message))
                }
            }
        }
    }

    fun unregister() {
        if (holdLiveData != null) {
            holdLiveData!!.removeObserver(this)
            holdLiveData = null
            if (hasMore) {
                limit = null
                offset = null
            }
        }
    }

    fun reset() {
        unregister()
        hasMore = true
        loadingState.value = LoadingState(false, null)
    }

    class LoadingState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }

    }
}