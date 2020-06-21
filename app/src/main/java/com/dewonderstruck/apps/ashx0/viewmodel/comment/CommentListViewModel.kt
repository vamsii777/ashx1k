package com.dewonderstruck.apps.ashx0.viewmodel.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.comment.CommentRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Comment
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class CommentListViewModel @Inject constructor(commentRepository: CommentRepository) : PSViewModel() {
    //for recent comment list
    @JvmField
    val PRODUCT_ID_KEY = "product_id"
    @JvmField
    var productId = ""
    val commentListData: LiveData<Resource<List<Comment>>>
    private val commentListObj = MutableLiveData<TmpDataHolder>()
    val nextPageLoadingStateData: LiveData<Resource<Boolean>>
    private val nextPageLoadingStateObj = MutableLiveData<TmpDataHolder>()
    private val sendCommentHeaderPostData: LiveData<Resource<Boolean>>
    private val sendCommentHeaderPostDataObj = MutableLiveData<TmpDataHolder>()
    val commentCountLoadingStateData: LiveData<Resource<Boolean>>
    private val commentCountLoadingStateObj = MutableLiveData<TmpDataHolder>()

    //endregion
    fun setSendCommentHeaderPostDataObj(product_id: String,
                                        userId: String,
                                        headerComment: String
    ) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.productId = product_id
            tmpDataHolder.userId = userId
            tmpDataHolder.headerComment = headerComment
            sendCommentHeaderPostDataObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    fun getsendCommentHeaderPostData(): LiveData<Resource<Boolean>> {
        return sendCommentHeaderPostData
    }

    //region Getter And Setter for Comment List
    fun setCommentListObj(limit: String, offset: String, productId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.offset = offset
            tmpDataHolder.productId = productId
            commentListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //Get Comment Next Page
    fun setNextPageCommentLoadingObj(product_id: String, limit: String, offset: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.limit = limit
            tmpDataHolder.productId = product_id
            tmpDataHolder.offset = offset
            nextPageLoadingStateObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    fun setCommentCountLoadingObj(comment_id: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.comment_id = comment_id
            commentCountLoadingStateObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal inner class TmpDataHolder {
        var limit = ""
        var offset = ""
        var productId = ""
        var userId = ""
        var headerComment = ""
        var comment_id = ""
        var isConnected = false
        var shopId: String? = null
    } //endregion

    //region Constructor
    init {
        // Latest comment List
        commentListData = Transformations.switchMap(commentListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Comment>>>()
            }
            Utils.psLog("Comment List.")
            commentRepository.getCommentList(Config.API_KEY, obj.productId, obj.limit, obj.offset)
        }
        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Comment List.")
            commentRepository.getNextPageCommentList(obj.productId, obj.limit, obj.offset)
        }
        sendCommentHeaderPostData = Transformations.switchMap(sendCommentHeaderPostDataObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            commentRepository.uploadCommentHeaderToServer(
                    obj.productId,
                    obj.userId,
                    obj.headerComment)
        }
        commentCountLoadingStateData = Transformations.switchMap(commentCountLoadingStateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Comment List.")
            commentRepository.getCommentDetailReplyCount(obj.comment_id)
        }
    }
}