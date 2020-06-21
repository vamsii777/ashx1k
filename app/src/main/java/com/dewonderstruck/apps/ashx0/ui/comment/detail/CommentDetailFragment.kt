package com.dewonderstruck.apps.ashx0.ui.comment.detail

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentCommentDetailBinding
import com.dewonderstruck.apps.ashx0.ui.comment.detail.adapter.CommentDetailAdapter
import com.dewonderstruck.apps.ashx0.ui.comment.detail.adapter.CommentDetailAdapter.CommentDetailClickCallback
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.comment.CommentDetailListViewModel
import com.dewonderstruck.apps.ashx0.viewobject.CommentDetail
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.google.android.gms.ads.AdRequest
import java.lang.String

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
class CommentDetailFragment : PSFragment(), DiffUtilDispatchedInterface {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var commentDetailListViewModel: CommentDetailListViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentCommentDetailBinding>? = null
    private var adapter: AutoClearedValue<CommentDetailAdapter>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding: FragmentCommentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_detail, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        binding!!.get().loadingMore = connectivity!!.isConnected
        return binding!!.get().root
    }

    override fun onDispatched() {
        if (commentDetailListViewModel!!.loadingDirection == Utils.LoadingDirection.top) {
            if (binding!!.get().commentList != null) {
                val layoutManager = binding!!.get().commentList.layoutManager as LinearLayoutManager?
                layoutManager?.scrollToPosition(0)
            }
        }
    }

    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity!!.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView.loadAd(adRequest)
        } else {
            binding!!.get().adView.visibility = View.GONE
        }
        psDialogMsg = PSDialogMsg(activity, false)

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
        prgDialog!!.get().setCancelable(false)

        // Set reverse layout
        val layoutManager = binding!!.get().commentList.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            layoutManager.reverseLayout = true
            binding!!.get().commentList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (layoutManager != null) {
                        val lastPosition = layoutManager
                                .findLastVisibleItemPosition()
                        if (lastPosition == adapter!!.get().itemCount - 1) {
                            if (!binding!!.get().loadingMore && !commentDetailListViewModel!!.forceEndLoading) {
                                if (connectivity!!.isConnected) {
                                    commentDetailListViewModel!!.loadingDirection = Utils.LoadingDirection.bottom
                                    val limit = Config.COMMENT_COUNT
                                    commentDetailListViewModel!!.offset = commentDetailListViewModel!!.offset + limit
                                    commentDetailListViewModel!!.setNextPageLoadingCommentDetailObj(String.valueOf(commentDetailListViewModel!!.offset), commentDetailListViewModel!!.commentId)
                                }
                            }
                        }
                    }
                }
            })
        }
        binding!!.get().sendImageButton.setOnClickListener { view: View? ->
            if (!commentDetailListViewModel!!.isLoading) {
                Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, this@CommentDetailFragment.activity, navigationController, Utils.NavigateOnUserVerificationActivityCallback { sendComment() })
            }
        }
    }

    override fun initViewModels() {
        commentDetailListViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentDetailListViewModel::class.java)
    }

    override fun initAdapters() {
        val nvAdapter = CommentDetailAdapter(dataBindingComponent, CommentDetailClickCallback { comment: CommentDetail? -> }, this)
        adapter = AutoClearedValue(this, nvAdapter)
        binding!!.get().commentList.adapter = nvAdapter
    }

    override fun initData() {
        try {
            if (activity != null) {
                if (activity!!.intent.extras != null) {
                    commentDetailListViewModel!!.commentId = activity!!.intent.extras!!.getString(Constants.COMMENT_ID)!!
                }
            }
        } catch (e: Exception) {
            Utils.psErrorLog("", e)
        }
        loadCommentDetail()
    }

    override fun onResume() {
        super.onResume()
        loadLoginUserId()
    }

    private fun loadCommentDetail() {
        // Load Latest Product
        commentDetailListViewModel!!.setCommentDetailListObj(String.valueOf(commentDetailListViewModel!!.offset), commentDetailListViewModel!!.commentId)
        val news = commentDetailListViewModel!!.commentDetailListData
        news?.observe(this, Observer { listResource: Resource<List<CommentDetail>>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.LOADING ->                             // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get().root)

                            // Update the data
                            replaceData(listResource.data)
                        }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            // Update the data
                            replaceData(listResource.data)
                        }
                        commentDetailListViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        commentDetailListViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (commentDetailListViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    commentDetailListViewModel!!.forceEndLoading = true
                }
            }
        })
        commentDetailListViewModel!!.nextPageCommentDetailLoadingData.observe(this, Observer { state: Resource<Boolean>? ->
            if (state != null) {
                if (state.status === Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data)
                    commentDetailListViewModel!!.setLoadingState(false) //hide
                    commentDetailListViewModel!!.forceEndLoading = true //stop
                }
            }
        })
        commentDetailListViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? -> binding!!.get().loadingMore = commentDetailListViewModel!!.isLoading })
        commentDetailListViewModel!!.getsendCommentDetailPostData().observe(this, Observer<Resource<Boolean?>> { result: Resource<Boolean?>? ->
            prgDialog!!.get().cancel()
            if (result != null) {
                Utils.psLog("Got Data")
                if (result.status === Status.SUCCESS) {
                    commentDetailListViewModel!!.loadingDirection = Utils.LoadingDirection.top
                    onDispatched()
                    binding!!.get().editText.setText("")
                    commentDetailListViewModel!!.setLoadingState(false)
                    if (commentDetailListViewModel!!.commentDetailListData.value == null || commentDetailListViewModel!!.commentDetailListData.value!!.data == null || commentDetailListViewModel!!.commentDetailListData.value!!.data!!.size <= 1) {
                        commentDetailListViewModel!!.offset = 0

                        // reset productViewModel.forceEndLoading
                        commentDetailListViewModel!!.forceEndLoading = false

                        // update live data
                        commentDetailListViewModel!!.setCommentDetailListObj(String.valueOf(commentDetailListViewModel!!.offset), commentDetailListViewModel!!.commentId)
                    }
                    navigationController!!.navigateBackToCommentListFragment(activity!!, commentDetailListViewModel!!.commentId)
                } else if (result.status === Status.ERROR) {
                    commentDetailListViewModel!!.setLoadingState(false)
                }
            } else {
                psDialogMsg!!.showInfoDialog(getString(R.string.error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        })
    }

    private fun sendComment() {
        val description = binding!!.get().editText.text.toString()
        if (description == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.comment__empty_comment), getString(R.string.app__ok))
            psDialogMsg!!.show()
        } else {
            prgDialog!!.get().show()
            commentDetailListViewModel!!.setSendCommentDetailPostDataObj(commentDetailListViewModel!!.commentId, loginUserId!!, description)
        }
    }

    private fun replaceData(commentList: List<CommentDetail>) {
        adapter!!.get().replace(commentList)
        binding!!.get().executePendingBindings()
    }
}