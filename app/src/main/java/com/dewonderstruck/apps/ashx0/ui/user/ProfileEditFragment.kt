package com.dewonderstruck.apps.ashx0.ui.user

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentProfileEditBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

/**
 * ProfileEditFragment
 */
class ProfileEditFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    var countryId: String? = Constants.NO_DATA
    var cityId: String? = Constants.NO_DATA

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentProfileEditBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null

    //endregion
    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val dataBinding: FragmentProfileEditBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        userViewModel!!.setLoginUser()
        userViewModel!!.loginUser.observe(this, Observer { listResource: List<UserLogin>? ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.size > 0) {
                Utils.psLog("Got Data")

                //fadeIn Animation
                fadeIn(binding!!.get().root)
                binding!!.get().user = listResource[0].user
                userViewModel!!.user = listResource[0].user
                userViewModel!!.countryId = userViewModel!!.user.country.id
                userViewModel!!.cityId = userViewModel!!.user.city.id
                Utils.psLog("Photo : " + listResource[0].user.userProfilePhoto)
            } else {
                Utils.psLog("Empty Data")
            }
        })
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)
        if (context != null) {
            binding!!.get().userNameEditText.setHint(R.string.edit_profile__user_name)
            binding!!.get().userEmailEditText.setHint(R.string.edit_profile__email)
            binding!!.get().userPhoneEditText.setHint(R.string.edit_profile__phone)
            binding!!.get().userAboutMeEditText.setHint(R.string.edit_profile__about_me)
            binding!!.get().countryTextView.setHint(R.string.edit_profile__country)
            binding!!.get().nameProfile.text = requireContext().getString(R.string.edit_profile__user_name)
            binding!!.get().emailTextView.text = requireContext().getString(R.string.edit_profile__email)
            binding!!.get().phoneProfile.text = requireContext().getString(R.string.edit_profile__phone)
            binding!!.get().aboutMeTextView.text = requireContext().getString(R.string.edit_profile__about_me)
        }

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        prgDialog!!.get().setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
        prgDialog!!.get().setCancelable(false)
        binding!!.get().profileImageView.setOnClickListener { view: View? ->
            if (connectivity.isConnected) {
                try {
                    if (Utils.isStoragePermissionGranted(activity)) {
                        val i = Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(i, Constants.RESULT_LOAD_IMAGE)
                    }
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in Image Gallery.", e)
                }
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
        binding!!.get().saveButton.setOnClickListener { view: View? -> editProfileData() }
        binding!!.get().passwordChangeButton.setOnClickListener { view: View? -> navigationController.navigateToPasswordChangeActivity(activity) }
        binding!!.get().countrySelectionView.setOnClickListener { v: View? -> navigationController.navigateToSearchActivityCategoryFragment(activity, Constants.COUNTRY, Constants.NO_DATA, Constants.NO_DATA, userViewModel!!.countryId, userViewModel!!.cityId) }
        binding!!.get().citySelectionView.setOnClickListener { v: View? ->
            if (binding!!.get().countryTextView.text.toString() == Constants.EMPTY_STRING) {
                psDialogMsg!!.showWarningDialog(getString(R.string.error_message__choose_country), getString(R.string.app__ok))
                psDialogMsg!!.show()
            } else {
                navigationController.navigateToSearchActivityCategoryFragment(activity, Constants.CITY, Constants.NO_DATA, Constants.NO_DATA, userViewModel!!.countryId, userViewModel!!.cityId)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == Constants.RESULT_OK && null != data) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (activity != null && selectedImage != null) {
                    val cursor = requireActivity().contentResolver.query(selectedImage,
                            filePathColumn, null, null, null)
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        userViewModel!!.profileImagePath = cursor.getString(columnIndex)
                        cursor.close()
                        uploadImage()
                    }
                }
            } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_COUNTRY) {
                countryId = data!!.getStringExtra(Constants.COUNTRY_ID)
                userViewModel!!.countryName = data.getStringExtra(Constants.COUNTRY_NAME)
                binding!!.get().countryTextView.text = userViewModel!!.countryName
                userViewModel!!.countryId = countryId
                userViewModel!!.user.country.id = countryId!!
                cityId = ""
                userViewModel!!.cityId = cityId
                binding!!.get().cityTextView.text = ""
                if (activity != null) {
                    userViewModel!!.user.country.id = userViewModel!!.countryId
                    userViewModel!!.user.city.id = userViewModel!!.cityId
                }
            } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CITY) {
                cityId = data!!.getStringExtra(Constants.CITY_ID)
                userViewModel!!.cityName = data.getStringExtra(Constants.CITY_NAME)
                binding!!.get().cityTextView.text = userViewModel!!.cityName
                userViewModel!!.cityId = cityId
                userViewModel!!.user.city.id = cityId!!
                if (activity != null) {
                    userViewModel!!.user.city.id = userViewModel!!.cityId
                }
            }
        } catch (e: Exception) {
            Utils.psErrorLog("Error in load image.", e)
        }
    }

    //endregion
    //region Private Methods
    private fun editProfileData() {
        if (!connectivity.isConnected) {
            psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        val userName = binding!!.get().userNameEditText.text.toString()
        if (userName == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        val userEmail = binding!!.get().userEmailEditText.text.toString()
        if (userEmail == "") {
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok))
            psDialogMsg!!.show()
            return
        }
        if (!checkToUpdateProfile()) {
            userViewModel!!.user.city.name = binding!!.get().cityTextView.text.toString()
            userViewModel!!.user.country.name = binding!!.get().countryTextView.text.toString()
            updateUserProfile()
        }
        userViewModel!!.updateUserData.observe(this, Observer { listResource: Resource<User?>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        // Success State
                        // Data are from Server
                        if (listResource.data != null) {

                            //userViewModel.updateUser(userViewModel.user);
                            psDialogMsg!!.showSuccessDialog("", getString(R.string.app__ok))
                            psDialogMsg!!.show()
                            psDialogMsg!!.okButton.setOnClickListener { view: View? -> psDialogMsg!!.cancel() }
                        }
                        userViewModel!!.setLoadingState(false)
                        prgDialog!!.get().cancel()
                    }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        prgDialog!!.get().cancel()
                        userViewModel!!.setLoadingState(false)
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
    }

    private fun uploadImage() {
        prgDialog!!.get().show()
        Utils.psLog("Uploading Image.")
        userViewModel!!.uploadImage(userViewModel!!.profileImagePath, loginUserId).observe(this, Observer { listResource: Resource<User?>? ->
            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                if (listResource.message != null && listResource.message != "") {
                    prgDialog!!.get().cancel()
                } else {
                    // Update the data
                    prgDialog!!.get().cancel()
                }
            } else if (listResource != null && listResource.message != null) {
                Utils.psLog("Message from server.")
                psDialogMsg!!.showInfoDialog(listResource.message, getString(R.string.app__ok))
                psDialogMsg!!.show()
                prgDialog!!.get().cancel()
            } else {
                Utils.psLog("Empty Data")
            }
        })
    }

    private fun checkToUpdateProfile(): Boolean {
        return binding!!.get().userNameEditText.text.toString() == userViewModel!!.user.userName && binding!!.get().userEmailEditText.text.toString() == userViewModel!!.user.userEmail && binding!!.get().userPhoneEditText.text.toString() == userViewModel!!.user.userPhone && binding!!.get().userAboutMeEditText.text.toString() == userViewModel!!.user.userAboutMe && binding!!.get().firstNameEditText.text.toString() == userViewModel!!.user.shippingFirstName && binding!!.get().lastNameEditText.text.toString() == userViewModel!!.user.shippingLastName && binding!!.get().companyEditText.text.toString() == userViewModel!!.user.shippingCompany && binding!!.get().address1EditText.text.toString() == userViewModel!!.user.shippingAddress1 && binding!!.get().address2EditText.text.toString() == userViewModel!!.user.shippingAddress2 && binding!!.get().countryTextView.text.toString() == userViewModel!!.user.country.name && binding!!.get().card1StateEditText.text.toString() == userViewModel!!.user.shippingState && binding!!.get().cityTextView.text.toString() == userViewModel!!.user.city.name && binding!!.get().card1PostalEditText.text.toString() == userViewModel!!.user.shippingPostalCode && binding!!.get().emailEditText.text.toString() == userViewModel!!.user.shippingEmail && binding!!.get().phoneEditText.text.toString() == userViewModel!!.user.shippingPhone && binding!!.get().card2FirstNameEditText.text.toString() == userViewModel!!.user.billingFirstName && binding!!.get().card2LastNameEditText.text.toString() == userViewModel!!.user.billingLastName && binding!!.get().card2CompanyEditText.text.toString() == userViewModel!!.user.billingCompany && binding!!.get().card2Address1EditText.text.toString() == userViewModel!!.user.billingAddress1 && binding!!.get().card2Address2EditText.text.toString() == userViewModel!!.user.billingAddress2 && binding!!.get().card2CountryEditText.text.toString() == userViewModel!!.user.billingCountry && binding!!.get().card2StateEditText.text.toString() == userViewModel!!.user.billingState && binding!!.get().card2CityEditText.text.toString() == userViewModel!!.user.billingCity && binding!!.get().card2PostalEditText.text.toString() == userViewModel!!.user.billingPostalCode && binding!!.get().card2EmailEditText.text.toString() == userViewModel!!.user.billingEmail && binding!!.get().card2PhoneEditText.text.toString() == userViewModel!!.user.billingPhone
    }

    private fun updateUserProfile() {
        val user = User(userViewModel!!.user.userId,
                userViewModel!!.user.userIsSysAdmin,
                userViewModel!!.user.isShopAdmin,
                userViewModel!!.user.facebookId,
                userViewModel!!.user.googleId,
                binding!!.get().userNameEditText.text.toString(),
                binding!!.get().userEmailEditText.text.toString(),
                binding!!.get().userPhoneEditText.text.toString(),
                userViewModel!!.user.userPassword,
                binding!!.get().userAboutMeEditText.text.toString(),
                userViewModel!!.user.userCoverPhoto,
                userViewModel!!.user.userProfilePhoto,
                userViewModel!!.user.roleId,
                userViewModel!!.user.status,
                userViewModel!!.user.isBanned,
                userViewModel!!.user.addedDate,
                binding!!.get().card2FirstNameEditText.text.toString(),
                binding!!.get().card2LastNameEditText.text.toString(),
                binding!!.get().card2CompanyEditText.text.toString(),
                binding!!.get().card2Address1EditText.text.toString(),
                binding!!.get().card2Address2EditText.text.toString(),
                binding!!.get().card2CountryEditText.text.toString(),
                binding!!.get().card2StateEditText.text.toString(),
                binding!!.get().card2CityEditText.text.toString(),
                binding!!.get().card2PostalEditText.text.toString(),
                binding!!.get().card2EmailEditText.text.toString(),
                binding!!.get().card2PhoneEditText.text.toString(),
                binding!!.get().firstNameEditText.text.toString(),
                binding!!.get().lastNameEditText.text.toString(),
                binding!!.get().companyEditText.text.toString(),
                binding!!.get().address1EditText.text.toString(),
                binding!!.get().address2EditText.text.toString(),
                binding!!.get().countryTextView.text.toString(),
                binding!!.get().card1StateEditText.text.toString(),
                binding!!.get().cityTextView.text.toString(),
                binding!!.get().card1PostalEditText.text.toString(),
                binding!!.get().emailEditText.text.toString(),
                binding!!.get().phoneEditText.text.toString(),
                userViewModel!!.user.deviceToken,
                userViewModel!!.user.code,
                userViewModel!!.user.verifyTypes,
                userViewModel!!.user.addedDateStr,
                userViewModel!!.user.country,
                userViewModel!!.user.city)
        userViewModel!!.setUpdateUserObj(user)
        prgDialog!!.get().show()
    } //endregion
}