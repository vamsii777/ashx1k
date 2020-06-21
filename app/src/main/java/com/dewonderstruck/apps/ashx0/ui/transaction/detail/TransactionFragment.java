package com.dewonderstruck.apps.ashx0.ui.transaction.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent;
import com.dewonderstruck.apps.ashx0.databinding.FragmentTransactionBinding;
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter;
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment;
import com.dewonderstruck.apps.ashx0.ui.transaction.detail.adapter.TransactionAdapter;
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewmodel.transaction.TransactionListViewModel;
import com.dewonderstruck.apps.ashx0.viewmodel.transaction.TransactionOrderViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.TransactionObject;
import com.dewonderstruck.apps.ashx0.viewobject.TransactionDetail;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class TransactionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private TransactionListViewModel transactionListViewModel;
    private TransactionOrderViewModel transactionOrderViewModel;
    private String transactionId,transactionIsZoneShipping;
    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentTransactionBinding> binding;
    private AutoClearedValue<TransactionAdapter> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTransactionBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);

        binding.get().copyImageView.setOnClickListener(view -> {
            if (getContext() != null) {
                ClipboardManager cManger = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText(Constants.TRANSACTON_TEXT, binding.get().transactionNumberTextView.getText());
                if (cManger != null) {
                    cManger.setPrimaryClip(cData);

                    Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    protected void initViewModels() {
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel.class);
        transactionOrderViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionOrderViewModel.class);
    }

    @Override
    protected void initAdapters() {
        TransactionAdapter nvAdapter = new TransactionAdapter(dataBindingComponent, transaction -> {
            //binding.get().getRoot().setOnClickListener(view -> navigationController.navigateToCommentDetailActivity(getActivity()));
        }, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().transactionOrderList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    transactionId = getActivity().getIntent().getExtras().getString(Constants.TRANSACTION_ID);
                    transactionIsZoneShipping = getActivity().getIntent().getExtras().getString(Constants.TRANSACTION_IS_ZONE_SHIPPING);
                    adapter.get().setZoneShipping(transactionIsZoneShipping);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
        transactionListViewModel.setTransactionDetailObj(loginUserId, transactionId);

        LiveData<Resource<TransactionObject>> transactionDetail = transactionListViewModel.getTransactionDetailData();

        if (transactionDetail != null) {
            transactionDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceTransactionDetailData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceTransactionDetailData(listResource.data);

                            }

                            transactionListViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            transactionListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    transactionListViewModel.setLoadingState(false);
                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                }

            });

        }


        //Transaction Order
        transactionOrderViewModel.setTransactionOrderListObj(String.valueOf(transactionOrderViewModel.offset), transactionId);

        LiveData<Resource<List<TransactionDetail>>> news = transactionOrderViewModel.getTransactionListData();

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

                            transactionOrderViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            transactionOrderViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (transactionOrderViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        transactionOrderViewModel.forceEndLoading = true;
                    }

                }

            });
        }

    }

    //endregion
    private void replaceData(List<TransactionDetail> transactionDetailList) {
        adapter.get().replace(transactionDetailList);
        binding.get().executePendingBindings();


    }

    private void replaceTransactionDetailData(TransactionObject transactionObject) {

        binding.get().setTransactionDetail(transactionObject);


        binding.get().transactionNumberTextView.setText(transactionObject.transCode);

        if (!transactionObject.totalItemCount.equals(Constants.ZERO)) {
            binding.get().totalItemCountValueTextView.setText(( transactionObject.totalItemCount ));
        }

        if (!transactionObject.totalItemAmount.equals(Constants.ZERO)) {

            String totalValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.totalItemAmount));
            binding.get().totalValueTextView.setText(totalValueString);

        }

        if (!transactionObject.discountAmount.equals(Constants.ZERO)) {

            String discountValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.discountAmount));
            binding.get().discountValueTextView.setText(discountValueString);
        }

        if (!transactionObject.subTotalAmount.equals(Constants.ZERO)) {
            String subTotalValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.subTotalAmount));
            binding.get().subtotalValueTextView.setText(subTotalValueString);
        }

        if (!transactionObject.taxAmount.equals(Constants.ZERO)) {
            String taxValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.taxAmount));
            binding.get().taxValueTextView.setText(taxValueString);
        }

        if (!transactionObject.shippingAmount.equals(Constants.ZERO)) {
            String shippingTaxValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.shippingAmount));
            binding.get().shippingTaxValueTextView.setText(shippingTaxValueString);
        }

        if (!transactionObject.balanceAmount.equals(Constants.ZERO)) {
            String finalTotalValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.balanceAmount));
            binding.get().finalTotalValueTextView.setText(finalTotalValueString);
        }

        if (!transactionObject.shippingMethodAmount.equals(Constants.ZERO)) {
            String shippingCostValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.shippingMethodAmount));
            binding.get().shippingCostValueTextView.setText(shippingCostValueString);
        }

        if (!transactionObject.couponDiscountAmount.equals(Constants.ZERO)) {
            String couponDiscountValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.couponDiscountAmount));
            binding.get().couponDiscountValueTextView.setText(couponDiscountValueString);
        }

        if (!overAllTaxLabel.equals(Constants.ZERO)) {

            binding.get().taxTitleTextView.setText(getString(R.string.tax, overAllTaxLabel));
        }

        if (!shippingTaxLabel.equals(Constants.ZERO)) {

            binding.get().shippingTaxTitleTextView.setText(getString(R.string.shipping_tax, shippingTaxLabel));
        }

//        binding.get().statusText.setText(getString(R.string.transaction_detail__status));
//        binding.get().orderDateText.setText(getString(R.string.transaction_detail__order_date));
//        binding.get().originalPriceTextView.setText(getString(R.string.transaction_detail__total_charges));
//        binding.get().discountPriceTextView.setText(getString(R.string.transaction_detail__discount_price));
//        binding.get().priceTextView.setText(getString(R.string.transaction_detail__total_charges));
//        binding.get().shippingCostTextView.setText(getString(R.string.transaction_detail__shipping_cost));
//        binding.get().shippingTaxTextView.setText(getString(R.string.transaction_detail__shipping_tax));
//
//        binding.get().transactionNoTextView.setText(getString(R.string.transaction_detail__transaction_number));
//
//        binding.get().transactionNoValueTextView.setText(transactionObject.transCode);
//        binding.get().countValueText.setText(transactionObject.transStatusTitle);
//        binding.get().orderDateValueText.setText(transactionObject.addedDate);
//
//        String originalPriceValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.subTotalAmount));
//        binding.get().originalPriceValueTextView.setText(originalPriceValueString);
//        String discountValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.discountAmount));
//        binding.get().discountValueTextView.setText(discountValueString);
//        String priceValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.balanceAmount));
//        binding.get().priceValueTextView.setText(priceValueString);
//
        binding.get().shippingPhoneValueTextView.setText(transactionObject.shippingPhone);
        binding.get().shippingEmailValueTextView.setText(transactionObject.shippingEmail);
        binding.get().shippingAddressValueTextView.setText(transactionObject.shippingAddress1);

        binding.get().billingphoneValueTextView.setText(transactionObject.billingPhone);
        binding.get().billingEmailValueTextView.setText(transactionObject.billingEmail);
        binding.get().billingAddressValueTextView.setText(transactionObject.billingAddress1);
//
//        String shippingCostValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Constants.ZERO;
//
//        if (transactionObject.shippingMethodAmount != null && !transactionObject.shippingMethodAmount.isEmpty() && !transactionObject.shippingMethodAmount.equals("0")) {
//            shippingCostValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.shippingMethodAmount));
//
//        }
//        binding.get().shippingCostValueTextView.setText(shippingCostValueString);
//
//        String shippingTaxValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Constants.ZERO;
//
//        if (transactionObject.shippingAmount != null && !transactionObject.shippingAmount.isEmpty() && !transactionObject.shippingAmount.equals("0")) {
//            shippingTaxValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.shippingAmount));
//        }
//        binding.get().shippingTaxValueTextView.setText(shippingTaxValueString);

    }
}