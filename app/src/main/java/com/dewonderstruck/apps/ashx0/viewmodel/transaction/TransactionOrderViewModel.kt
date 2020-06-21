package com.dewonderstruck.apps.ashx0.viewmodel.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.transaction.TransactionOrderRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.TransactionDetail
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class TransactionOrderViewModel @Inject constructor(transactionOrderRepository: TransactionOrderRepository) : PSViewModel() {
    val transactionListData: LiveData<Resource<List<TransactionDetail>>>
    private val transactionOrderListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()
    fun setTransactionOrderListObj(offset: String, transactionHeaderId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.offset = offset
            tmpDataHolder.transactionHeaderId = transactionHeaderId
            transactionOrderListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    fun setNextPageLoadingStateObj(offset: String, transactionHeaderId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.offset = offset
            tmpDataHolder.transactionHeaderId = transactionHeaderId
            nextPageLoadingStateObj.value = tmpDataHolder
            // start loading
            setLoadingState(true)
        }
    }

    internal inner class TmpDataHolder {
        var offset = ""
        var transactionHeaderId = ""
        var isConnected = false
    }

    init {
        // Transaction Order List
        transactionListData = Transformations.switchMap(transactionOrderListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<TransactionDetail>>>()
            }
            Utils.psLog("Transaction List.")
            transactionOrderRepository.getTransactionOrderList(Config.API_KEY, obj.transactionHeaderId, obj.offset)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Transaction List.")
            transactionOrderRepository.getNextPageTransactionOrderList(obj.transactionHeaderId, obj.offset)
        }
    }
}