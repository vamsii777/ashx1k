package com.dewonderstruck.apps.ashx0.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentProfileBinding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface2
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.transaction.TransactionListViewModel
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

/**
 * ProfileFragment
 */
class ProfileFragment : DeFragment(), DiffUtilDispatchedInterface2 {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var transactionListViewModel: TransactionListViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentProfileBinding>? = null

    //private AutoClearedValue<TransactionListAdapter> adapter;
    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)

        //binding.get().transactionList.setNestedScrollingEnabled(false);
        binding!!.get().segmentEdit.setOnClickListener { view: View? -> navigationController.navigateToProfileEditActivity(requireActivity()) }
        //binding.get().seeAllTextView.setOnClickListener(view -> navigationController.navigateToTransactionDetailActivity(getActivity()));
        //binding.get().userHistoryTextView.setOnClickListener(view -> navigationController.navigateToUserHistoryActivity(getActivity()));
        binding!!.get().segFav.setOnClickListener { view: View? -> navigationController.navigateToFavouriteActivity(requireActivity()) }
        binding!!.get().segmentFavheart.setOnClickListener { view: View? -> navigationController.navigateToFavouriteActivity(requireActivity()) }
        binding!!.get().segmentSettings.setOnClickListener { view: View? -> navigationController.navigateToSettingActivity(requireActivity()) }
        //binding.get().userTransactionTextView.setOnClickListener(view -> navigationController.navigateToTransactionDetailActivity(getActivity()));
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel::class.java)
    }

    override fun initAdapters() {

        /*TransactionListAdapter nvAdapter = new TransactionListAdapter(dataBindingComponent, new TransactionListAdapter.TransactionClickCallback() {
            @Override
            public void onClick(TransactionObject transaction) {
                navigationController.navigateToTransactionDetail(getActivity(), transaction);
            }

            @Override
            public void onCopyClick() {

                psDialogMsg.showSuccessDialog(getString(R.string.copied_to_clipboard), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        },this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().transactionList.setAdapter(nvAdapter);*/
    }

    override fun initData() {
        //User
        userViewModel!!.getUser(loginUserId).observe(this, Observer { listResource: Resource<User?>? ->

//            transactionListViewModel.setTransactionListObj(String.valueOf(Config.LOAD_FROM_DB), String.valueOf(transactionListViewModel.offset), loginUserId);
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding!!.get().root)
                            binding!!.get().user = listResource.data
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto)
                            replaceUserData(listResource.data)
                        }
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {

                            //fadeIn Animation
                            //fadeIn(binding.get().getRoot());
                            binding!!.get().user = listResource.data
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto)
                            replaceUserData(listResource.data)
                        }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        userViewModel!!.isLoading = false
                    }
                    else ->                         // Default
                        userViewModel!!.isLoading = false
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }

            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data")
            } else {
                Utils.psLog("Empty Data")
            }
        })

        //Transaction
        transactionListViewModel!!.setTransactionListObj(Config.LOAD_FROM_DB.toString(), transactionListViewModel!!.offset.toString(), loginUserId)
        val news = transactionListViewModel!!.transactionListData
        news?.observe(this, Observer { listResource: Resource<List<TransactionObject>?>? ->
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
                        transactionListViewModel!!.setLoadingState(false)
                    }
                    Status.ERROR ->                             // Error State
                        transactionListViewModel!!.setLoadingState(false)
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
                if (transactionListViewModel!!.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    transactionListViewModel!!.forceEndLoading = true
                }
            }
        })
    }

    override fun onDispatched() {}

    //endregion
    private fun replaceData(transactionList: List<TransactionObject>?) {
        //adapter.get().replace(transactionList);
        binding!!.get().executePendingBindings()
    }

    private fun replaceUserData(user: User?) {
        binding!!.get().segmentEdit.text = binding!!.get().segmentEdit.text.toString()
        //binding.get().userTransactionTextView.setText(binding.get().userTransactionTextView.getText().toString());
        //binding.get().userHistoryTextView.setText(binding.get().userHistoryTextView.getText().toString());
        //binding.get().favouriteTextView.setText(binding.get().favouriteTextView.getText().toString());
        binding!!.get().segmentSettings.text = binding!!.get().segmentSettings.text.toString()
        //binding.get().ordersTextView.setText(binding.get().ordersTextView.getText().toString());
        //binding.get().seeAllTextView.setText(binding.get().seeAllTextView.getText().toString());
        binding!!.get().joinDate.text = binding!!.get().joinDate.text.toString()
        binding!!.get().joinedDate.text = user!!.addedDate
        binding!!.get().nameProfile.text = user.userName
        binding!!.get().phoneProfile.text = user.userPhone
        //binding.get().statusTextView.setText(user.userAboutMe);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE__PROFILE_FRAGMENT
                && resultCode == Constants.RESULT_CODE__LOGOUT_ACTIVATED) {
            if (activity is MainActivity) {
                (activity as MainActivity?)!!.setToolbarText((activity as MainActivity?)!!.binding!!.toolbar, getString(R.string.profile__title))
                //navigationController.navigateToUserFBRegister((MainActivity) getActivity());
                navigationController.navigateToUserLogin((activity as MainActivity?)!!)
            }
        }
    }
}