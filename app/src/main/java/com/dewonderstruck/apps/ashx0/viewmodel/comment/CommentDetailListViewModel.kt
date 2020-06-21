package com.dewonderstruck.apps.ashx0.viewmodel.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.comment.CommentDetailRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.CommentDetail
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

class CommentDetailListViewModel @Inject internal constructor(commentDetailRepository: CommentDetailRepository) : PSViewModel() {
    //for comment detail list
    @JvmField
    var commentId = ""
    val commentDetailListData: LiveData<Resource<List<CommentDetail>>>
    private val commentDetailListObj = MutableLiveData<TmpDataHolder>()
    val nextPageCommentDetailLoadingData: LiveData<Resource<Boolean>>
    private val nextPageLoadingCommentDetailObj = MutableLiveData<TmpDataHolder>()
    private val sendCommentDetailPostData: LiveData<Resource<Boolean>>
    private val sendCommentDetailPostDataObj = MutableLiveData<TmpDataHolder>()

    //endregion
    fun setSendCommentDetailPostDataObj(headerId: String,
                                        userId: String,
                                        detailComment: String
    ) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.headerId = headerId
            tmpDataHolder.userId = userId
            tmpDataHolder.detailComment = detailComment
            sendCommentDetailPostDataObj.value = tmpDataHolder
        }
    }

    fun getsendCommentDetailPostData(): LiveData<Resource<Boolean>> {
        return sendCommentDetailPostData
    }

    //region Getter And Setter for Comment detail List
    fun setCommentDetailListObj(offset: String, commentId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.offset = offset
            tmpDataHolder.commentId = commentId
            commentDetailListObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //Get Comment detail Next Page
    fun setNextPageLoadingCommentDetailObj(offset: String, commentId: String) {
        if (!isLoading) {
            val tmpDataHolder = TmpDataHolder()
            tmpDataHolder.offset = offset
            tmpDataHolder.commentId = commentId
            nextPageLoadingCommentDetailObj.value = tmpDataHolder

            // start loading
            setLoadingState(true)
        }
    }

    //endregion
    //region Holder
    internal inner class TmpDataHolder {
        var offset = ""
        var headerId = ""
        var userId = ""
        var commentId = ""
        var detailComment = ""
        var shopId = ""
    } //endregion

    //region Constructor
    init {
        //  comment detail List
        commentDetailListData = Transformations.switchMap(commentDetailListObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<CommentDetail>>>()
            }
            Utils.psLog("Comment detail List.")
            commentDetailRepository.getCommentDetailList(Config.API_KEY, obj.offset, obj.commentId)
        }
        nextPageCommentDetailLoadingData = Transformations.switchMap(nextPageLoadingCommentDetailObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            Utils.psLog("Comment detail List.")
            commentDetailRepository.getNextPageCommentDetailList(obj.offset, obj.commentId)
        }
        sendCommentDetailPostData = Transformations.switchMap(sendCommentDetailPostDataObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            commentDetailRepository.uploadCommentDetailToServer(
                    obj.headerId,
                    obj.userId,
                    obj.detailComment)
        }
    }
}