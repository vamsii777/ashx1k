package com.dewonderstruck.apps.ashx0.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent;

import com.dewonderstruck.apps.ashx0.databinding.FragmentUserRegisterBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment;
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.User;

/**
 * UserRegisterFragment
 */
public class UserRegisterFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    private boolean checkFlag;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserRegisterBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserRegisterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_register, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        dataBindingComponent.getFragmentBindingAdapters().bindFullImageDrawbale(binding.get().bgImageView, getResources().getDrawable(R.drawable.hotel_cell_background));

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view -> {

            if (connectivity.isConnected()) {

                Utils.navigateToLogin(UserRegisterFragment.this.getActivity(), navigationController);
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));

                psDialogMsg.show();
            }

        });

        binding.get().policyAndPrivacyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.get().policyAndPrivacyCheckBox.isChecked()) {
//                    Toast.makeText(getContext(),"Is Check ",Toast.LENGTH_SHORT).show();
                    navigationController.navigateToTermsAndConditionsActivity(getActivity(),Constants.SHOP_TERMS);
                    checkFlag = true;
                } else {
//                    Toast.makeText(getContext(),"Not Check ",Toast.LENGTH_SHORT).show();
                    checkFlag = false;
                }
            }
        });


        binding.get().registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFlag){
                UserRegisterFragment.this.registerUser();
                }else{
                    Toast.makeText(getContext(), "You need to check is on.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {


        String _email = pref.getString(Constants.USER_EMAIL_TO_VERIFY, "");
        String _password = pref.getString(Constants.USER_PASSWORD_TO_VERIFY, "");
        String _name = pref.getString(Constants.USER_NAME_TO_VERIFY, "");
        binding.get().emailEditText.setText(_email);
        binding.get().passwordEditText.setText(_password);
        binding.get().nameEditText.setText(_name);

        userViewModel.getRegisterUser().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if(listResource.data != null) {
                            try {
                                Utils.registerUserLoginData(pref,listResource.data,binding.get().passwordEditText.getText().toString());
                                Utils.navigateAfterUserRegister(getActivity(),navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();
                            updateRegisterBtnStatus();


                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showWarningDialog(listResource.message, getString(R.string.app__ok));
                        binding.get().registerButton.setText(getResources().getString(R.string.register__register));
                        psDialogMsg.show();

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");


            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null) {
                Utils.psLog("Got Data Of About Us.");

            } else {
                //noinspection Constant Conditions
                Utils.psLog("No Data of About Us.");
            }
        });
    }

    //endregion


    //region Private Methods

    private void updateRegisterBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().registerButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().registerButton.setText(getResources().getString(R.string.register__register));
        }
    }

    private void registerUser() {

        Utils.hideKeyboard(getActivity());

        String userName = binding.get().nameEditText.getText().toString().trim();
        if (userName.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }

        String userEmail = binding.get().emailEditText.getText().toString().trim();
        if (userEmail.equals("")) {

            psDialogMsg.showWarningDialog( getString(R.string.error_message__blank_email),getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }

        String userPassword = binding.get().passwordEditText.getText().toString().trim();
        if (userPassword.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }


        userViewModel.isLoading = true;
        updateRegisterBtnStatus();

//        userViewModel.setRegisterUser(new User());
        String token = pref.getString(Constants.NOTI_TOKEN,Constants.USER_NO_DEVICE_TOKEN);
        userViewModel.setRegisterUser(new User(
                "",
                "",
                "",
                "",
                "",
                userName,
                userEmail,
                "",
                userPassword,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                 token,
                "",
                "",
                "",null,null));

//
//
//            if (listResource != null) {
//
//                Utils.psLog("Got Data" + listResource.message + listResource.toString());
//
//                switch (listResource.status) {
//                    case LOADING:
//                        // Loading State
//                        // Data are from Local DB
//
//                        prgDialog.get().show();
//
//                        break;
//                    case SUCCESS:
//                        // Success State
//                        // Data are from Server
//
//                        if(listResource.data != null) {
//                            try {
//                                if (getActivity() != null) {
//                                    pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
//                                    pref.edit().putString(Constants.USER_NAME, listResource.data.user.userName).apply();
//                                    pref.edit().putString(Constants.USER_EMAIL, listResource.data.user.userEmail).apply();
//                                }
//
//                            } catch (NullPointerException ne) {
//                                Utils.psErrorLog("Null Pointer Exception.", ne);
//                            } catch (Exception e) {
//                                Utils.psErrorLog("Error in getting notification flag data.", e);
//                            }
//
//                            userViewModel.isLoading = false;
//                            prgDialog.get().cancel();
//                            updateRegisterBtnStatus();
//
//                            if (getActivity() instanceof MainActivity) {
//                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
//                                navigationController.navigateToUserProfile( (MainActivity) getActivity());
//                            } else {
//                                try {
//                                    getActivity().finish();
//                                } catch (Exception e) {
//                                    Utils.psErrorLog("Error in closing parent activity.", e);
//                                }
//                            }
//                        }
//
//                        break;
//                    case ERROR:
//                        // Error State
//
////                        if(listResource.data != null) {
//                            psDialogMsg.showWarningDialog(getString(R.string.error_message__email_exists), getString(R.string.app__ok));
//                            binding.get().registerButton.setText(getResources().getString(R.string.register__register));
//                            psDialogMsg.show();
////                        }else {
////                            psDialogMsg.cancel();
////                        }
//
//                        userViewModel.isLoading = false;
//                        prgDialog.get().cancel();
//
//                        break;
//                    default:
//                        // Default
//                        userViewModel.isLoading = false;
//                        prgDialog.get().cancel();
//                        break;
//                }
//
//            } else {
//
//                // Init Object or Empty Data
//                Utils.psLog("Empty Data");
//            }
//
//        });

    }

    //endregion


}

