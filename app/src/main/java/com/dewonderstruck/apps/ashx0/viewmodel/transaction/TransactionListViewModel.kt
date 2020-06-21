package com.dewonderstruck.apps.ashx0.viewmodel.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.transaction.TransactionRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.BasketProductListToServerContainer
import com.dewonderstruck.apps.ashx0.viewobject.TransactionHeaderUpload
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class TransactionListViewModel @Inject internal constructor(transactionRepository: TransactionRepository) : PSViewModel() {
    val transactionListData: LiveData<Resource<List<TransactionObject>>>
    private val transactionListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()
    val transactionDetailData: LiveData<Resource<TransactionObject>>
    private val transactionDetailObj = MutableLiveData<TmpDataHolder>()
    val sendTransactionDetailData: LiveData<Resource<TransactionObject>>
    private val sendTransactionDetailDataObj = MutableLiveData<TmpDataHolder>()

    // region Getter And Setter for Transaction List
    fun setSendTransactionDetailDataObj(transactionHeaderUpload: TransactionHeaderUpload?) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.transactionHeaderUpload = transactionHeaderUpload
        sendTransactionDetailDataObj.value = tmpDataHolder
    }

    fun setTransactionListObj(loadFromDB: String, offset: String, loginUserId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.loadFromDB = loadFromDB
            tmpDataHolder.offset = offset
            tmpDataHolder.userId = loginUserId
            transactionListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    fun setNextPageLoadingStateObj(offset: String, loginUserId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.offset = offset
            tmpDataHolder.userId = loginUserId
            nextPageLoadingStateObj.value = tmpDataHolder
            // start loading
            setLoadingState(true)
        }
    }

    fun setTransactionDetailObj(userId: String, transactionId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.userId = userId
            tmpDataHolder.transactionId = transactionId
            transactionDetailObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    val token: String?
        get() = null

    internal inner class TmpDataHolder {
        var offset = ""
        var userId = ""
        var transactionId = ""
        var isConnected = false
        var loadFromDB = ""
        var subTotalAmount = 0
        var discount_amount = 0
        var balanceAmount = 0
        var contactName: String? = null
        var contactPhone: String? = null
        var delivery_address: String? = null
        var billing_address: String? = null
        var paymentMethod: String? = null
        var transStatusId: String? = null
        var currency_symbol: String? = null
        var currency_short_form: String? = null
        var basketProductListToServerContainer: BasketProductListToServerContainer? = null
        var shopId: String? = null
        var transactionHeaderUpload: TransactionHeaderUpload? = null
    }

    init {
        // Transaction List
        transactionListData = Transformations.switchMap(transactionListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<TransactionObject>>>()
            }
            Utils.psLog("transactionListData")
            transactionRepository.getTransactionList(Config.API_KEY, obj.userId, obj.loadFromDB, obj.offset)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("nextPageTransactionLoadingData")
            transactionRepository.getNextPageTransactionList(obj.userId, obj.offset)
        }
        transactionDetailData = Transformations.switchMap(transactionDetailObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<TransactionObject>>()
            }
            Utils.psLog("transactionDetailData")
            transactionRepository.getTransactionDetail(Config.API_KEY, obj.userId, obj.transactionId)
        }
        sendTransactionDetailData = Transformations.switchMap(sendTransactionDetailDataObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<TransactionObject>>()
            }
            transactionRepository.uploadTransDetailToServer(obj.transactionHeaderUpload)
        }
    }
}