package com.dewonderstruck.apps.ashx0.ui.comment.list;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dewonderstruck.apps.ashx0.R;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent;
import com.dewonderstruck.apps.ashx0.databinding.FragmentCommentListBinding;
import com.dewonderstruck.apps.ashx0.ui.comment.list.adapter.CommentListAdapter;
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter;
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment;
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewmodel.comment.CommentListViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.Comment;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;
import com.dewonderstruck.apps.ashx0.viewobject.common.Status;

import java.util.List;

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
public class CommentListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private CommentListViewModel commentListViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private
    AutoClearedValue<FragmentCommentListBinding> binding;
    private AutoClearedValue<CommentListAdapter> adapter;
    private AutoClearedValue<ProgressDialog> prgDialog;


    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCommentListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();

    }

    @Override
    public void onDispatched() {
        if (commentListViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().commentList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().commentList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        // Set reverse layout
        LinearLayoutManager layoutManager = (LinearLayoutManager)
                binding.get().commentList.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.setReverseLayout(true);

            binding.get().commentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager)
                            recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int lastPosition = layoutManager
                                .findLastVisibleItemPosition();
                        if (lastPosition == adapter.get().getItemCount() - 1) {

                            if (!binding.get().getLoadingMore() && !commentListViewModel.forceEndLoading) {

                                if (connectivity.isConnected()) {

                                    commentListViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                    int limit = Config.COMMENT_COUNT;
                                    commentListViewModel.offset = commentListViewModel.offset + limit;

                                    commentListViewModel.setNextPageCommentLoadingObj(commentListViewModel.productId, String.valueOf(Config.COMMENT_COUNT), String.valueOf(commentListViewModel.offset));

                                }
                            }
                        }
                    }
                }
            });
        }


        binding.get().sendImageButton.setOnClickListener(view -> {

            if (!commentListViewModel.isLoading) {

                Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, CommentListFragment.this.getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
                    @Override
                    public void onSuccess() {
                        sendComment();
                    }
                });
            }
        });
    }

    @Override
    protected void initViewModels() {
        commentListViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentListViewModel.class);
    }

    @Override
    protected void initAdapters() {

        CommentListAdapter nvAdapter = new CommentListAdapter(dataBindingComponent,
                comment -> navigationController.navigateToCommentDetailActivity(CommentListFragment.this.getActivity(), comment), this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().commentList.setAdapter(nvAdapter);

    }

    @Override
    protected void initData() {
        loadData();
    }

    private void sendComment() {

        String description = binding.get().editText.getText().toString();

        if (description.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.comment__empty_comment), getString(R.string.app__ok));
            psDialogMsg.show();

        } else {

            prgDialog.get().show();
            commentListViewModel.setSendCommentHeaderPostDataObj(commentListViewModel.productId, loginUserId, description);

        }
    }

    private void loadData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    commentListViewModel.productId = getActivity().getIntent().getExtras().getString(commentListViewModel.PRODUCT_ID_KEY);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
        // Load Latest Product
        commentListViewModel.setCommentListObj(String.valueOf(Config.COMMENT_COUNT), String.valueOf(commentListViewModel.offset), commentListViewModel.productId);

        LiveData<Resource<List<com.dewonderstruck.apps.ashx0.viewobject.Comment>>> news = commentListViewModel.getCommentListData();

        if (news != null) {
            news.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            commentListViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            commentListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (commentListViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        commentListViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        commentListViewModel.getsendCommentHeaderPostData().observe(this, result -> {
            prgDialog.get().cancel();
            if (result != null) {
                Utils.psLog("Got Data");
                if (result.status == Status.SUCCESS) {
                    commentListViewModel.loadingDirection = Utils.LoadingDirection.top;
                    onDispatched();

                    binding.get().editText.setText("");
                    commentListViewModel.setLoadingState(false);

                    navigationController.navigateBackToProductDetailFragment(getActivity(), commentListViewModel.productId);

                    try {
                        Resource<List<Comment>> resourceCommentList = commentListViewModel.getCommentListData().getValue();

                        if (resourceCommentList != null && resourceCommentList.data != null) {
                            if (resourceCommentList.data.size() <= 1) {
                                commentListViewModel.setCommentListObj(String.valueOf(Config.COMMENT_COUNT), String.valueOf(commentListViewModel.offset), commentListViewModel.productId);
                            }
                        }

                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("null pinter", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("", e);
                    }


                } else if (result.status == Status.ERROR) {
                    commentListViewModel.setLoadingState(false);
                }

            } else {

                psDialogMsg.showErrorDialog(getString(R.string.error), getString(R.string.app__ok));
                psDialogMsg.show();

            }
        });


        commentListViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    commentListViewModel.setLoadingState(false);//hide
                    commentListViewModel.forceEndLoading = true;//stop
                }
            }
        });

        commentListViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(commentListViewModel.isLoading);

        });

        commentListViewModel.getCommentCountLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    commentListViewModel.setLoadingState(false);//hide
                    commentListViewModel.forceEndLoading = true;//stop
                }
            }
        });
    }

    private void replaceData(List<com.dewonderstruck.apps.ashx0.viewobject.Comment> commentList) {
        adapter.get().replace(commentList);
        binding.get().executePendingBindings();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__COMMENT_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__REFRESH_COMMENT_LIST) {

            commentListViewModel.setCommentCountLoadingObj(data.getStringExtra(Constants.COMMENT_HEADER_ID));

        }
    }
}
