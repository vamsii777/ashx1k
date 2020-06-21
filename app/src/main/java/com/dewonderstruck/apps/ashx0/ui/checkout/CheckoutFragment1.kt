package com.dewonderstruck.apps.ashx0.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.CheckoutFragment1Binding
import com.dewonderstruck.apps.ashx0.ui.common.DataBoundListAdapter.DiffUtilDispatchedInterface
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.City
import com.dewonderstruck.apps.ashx0.viewobject.Country
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status

class CheckoutFragment1 : PSFragment(), DiffUtilDispatchedInterface {
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    var countryId: String? = Constants.NO_DATA
    var cityId: String? = Constants.NO_DATA

    @VisibleForTesting
    private var binding: AutoClearedValue<CheckoutFragment1Binding>? = null

    //    private AutoClearedValue<ProgressDialog> prgDialog;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding: CheckoutFragment1Binding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_1, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        return binding!!.get().root
    }

    override fun onDispatched() {}
    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(this.activity, true)
        if (activity is CheckoutActivity) {
            (activity as CheckoutActivity?)!!.progressDialog!!.setMessage(Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT))
            (activity as CheckoutActivity?)!!.progressDialog!!.setCancelable(false)
        }
        if (userViewModel!!.user != null) {
            if (userViewModel!!.user.billingFirstName != null && userViewModel!!.user.billingFirstName != "") {
                binding!!.get().card2FirstNameEditText.setText(userViewModel!!.user.billingFirstName)
            } else {
                binding!!.get().card2FirstNameEditText.setText(userViewModel!!.user.userName)
            }
            if (userViewModel!!.user.billingLastName != null && userViewModel!!.user.billingLastName != "") {
                binding!!.get().card2LastNameEditText.setText(userViewModel!!.user.billingLastName)
            }
            if (userViewModel!!.user.billingEmail != null && userViewModel!!.user.billingEmail != "") {
                binding!!.get().card2EmailEditText.setText(userViewModel!!.user.billingEmail)
            } else {
                binding!!.get().card2EmailEditText.setText(userViewModel!!.user.userEmail)
            }
            if (userViewModel!!.user.billingPhone != null && userViewModel!!.user.billingPhone != "") {
                binding!!.get().card2PhoneEditText.setText(userViewModel!!.user.billingPhone)
            }
            if (userViewModel!!.user.billingCompany != null && userViewModel!!.user.billingCompany != "") {
                binding!!.get().card2CompanyEditText.setText(userViewModel!!.user.billingCompany)
            }
            if (userViewModel!!.user.billingAddress1 != null && userViewModel!!.user.billingAddress1 != "") {
                binding!!.get().card2Address1EditText.setText(userViewModel!!.user.billingAddress1)
            }
            if (userViewModel!!.user.billingAddress2 != null && userViewModel!!.user.billingAddress2 != "") {
                binding!!.get().card2Address2EditText.setText(userViewModel!!.user.billingAddress2)
            }
            if (userViewModel!!.user.billingCountry != null && userViewModel!!.user.billingCountry != "") {
                binding!!.get().card2CountryEditText.setText(userViewModel!!.user.billingCountry)
            }
            if (userViewModel!!.user.billingState != null && userViewModel!!.user.billingState != "") {
                binding!!.get().card2StateEditText.setText(userViewModel!!.user.billingState)
            }
            if (userViewModel!!.user.billingCity != null && userViewModel!!.user.billingCity != "") {
                binding!!.get().card2CityEditText.setText(userViewModel!!.user.billingCity)
            }
            if (userViewModel!!.user.billingPostalCode != null && userViewModel!!.user.billingPostalCode != "") {
                binding!!.get().card2PostalEditText.setText(userViewModel!!.user.billingPostalCode)
            }
            if (userViewModel!!.user.userEmail != null && userViewModel!!.user.userEmail != "") {
                binding!!.get().userEmailEditText.setText(userViewModel!!.user.userEmail)
            }
            if (userViewModel!!.user.userPhone != null && userViewModel!!.user.userPhone != "") {
                binding!!.get().userPhoneEditText.setText(userViewModel!!.user.userPhone)
            }


            //Shipping Address
            if (userViewModel!!.user.shippingFirstName != null && userViewModel!!.user.shippingFirstName != "") {
                binding!!.get().firstNameEditText.setText(userViewModel!!.user.shippingFirstName)
            } else {
                binding!!.get().firstNameEditText.setText(userViewModel!!.user.userName)
            }
            if (userViewModel!!.user.shippingLastName != null && userViewModel!!.user.shippingLastName != "") {
                binding!!.get().lastNameEditText.setText(userViewModel!!.user.shippingLastName)
            }
            if (userViewModel!!.user.shippingEmail != null && userViewModel!!.user.shippingEmail != "") {
                binding!!.get().emailEditText.setText(userViewModel!!.user.shippingEmail)
            } else {
                binding!!.get().emailEditText.setText(userViewModel!!.user.userEmail)
            }
            if (userViewModel!!.user.shippingPhone != null && userViewModel!!.user.shippingPhone != "") {
                binding!!.get().phoneEditText.setText(userViewModel!!.user.shippingPhone)
            }
            if (userViewModel!!.user.shippingCompany != null && userViewModel!!.user.shippingCompany != "") {
                binding!!.get().companyEditText.setText(userViewModel!!.user.shippingCompany)
            }
            if (userViewModel!!.user.shippingAddress1 != null && userViewModel!!.user.shippingAddress1 != "") {
                binding!!.get().address1EditText.setText(userViewModel!!.user.shippingAddress1)
            }
            if (userViewModel!!.user.shippingAddress2 != null && userViewModel!!.user.shippingAddress2 != "") {
                binding!!.get().address2EditText.setText(userViewModel!!.user.shippingAddress2)
            }
            if (userViewModel!!.user.shippingCountry != null && userViewModel!!.user.shippingCountry != "") {
                binding!!.get().countryTextView.text = userViewModel!!.user.country.name
                userViewModel!!.countryId = userViewModel!!.user.country.id
            }
            if (userViewModel!!.user.shippingState != null && userViewModel!!.user.shippingState != "") {
                binding!!.get().stateEditText.setText(userViewModel!!.user.shippingState)
            }
            if (userViewModel!!.user.shippingCity != null && userViewModel!!.user.shippingCity != "") {
                binding!!.get().cityTextView.text = userViewModel!!.user.city.name
                userViewModel!!.cityId = userViewModel!!.user.city.id
            }
            if (userViewModel!!.user.shippingPostalCode != null && userViewModel!!.user.shippingPostalCode != "") {
                binding!!.get().postalEditText.setText(userViewModel!!.user.shippingPostalCode)
            }
        }
        binding!!.get().switch3.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (binding!!.get().switch3.isChecked) {
                copyTheText()
            } else {
                returnToOriginalValue()
            }
        }
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

                //fadeIn Animation
                fadeIn(binding!!.get().root)
                userViewModel!!.user = listResource[0].user
                //Utils.psLog("ADDRESS :" +userViewModel.user.shippingAddress1);
                userViewModel!!.countryId = userViewModel!!.user.country.id
                if (activity != null) {
                    (this@CheckoutFragment1.activity as CheckoutActivity?)!!.currentUser = userViewModel!!.user
                    initUIAndActions()
                }
            }
        })
        userViewModel!!.updateUserData.observe(this, Observer { listResource: Resource<User?>? ->
            if (listResource != null) {
                when (listResource.status) {
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            userViewModel!!.setLoadingState(false)
                            // TODO
                            if (this@CheckoutFragment1.activity != null && this@CheckoutFragment1.activity is CheckoutActivity) {
                                (this@CheckoutFragment1.activity as CheckoutActivity?)!!.progressDialog!!.hide()
                            }
                            Toast.makeText(this@CheckoutFragment1.activity, this@CheckoutFragment1.getString(R.string.success), Toast.LENGTH_SHORT).show()
                            userViewModel!!.setLoginUser()
                            //Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                            if (this@CheckoutFragment1.activity != null) {
                                (this@CheckoutFragment1.activity as CheckoutActivity?)!!.navigateFragment((this@CheckoutFragment1.activity as CheckoutActivity?)!!.binding, 2)
                            }
                        }
                    Status.ERROR -> {
                        // Error State
                        if (this@CheckoutFragment1.activity != null) {
                            (this@CheckoutFragment1.activity as CheckoutActivity?)!!.number = 1
                        }
                        psDialogMsg!!.showErrorDialog(listResource.message, this@CheckoutFragment1.getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        psDialogMsg!!.okButton.setOnClickListener { v: View? -> psDialogMsg!!.cancel() }

                        // TODO
                        if (this@CheckoutFragment1.activity != null && this@CheckoutFragment1.activity is CheckoutActivity) {
                            (this@CheckoutFragment1.activity as CheckoutActivity?)!!.progressDialog!!.hide()
                        }
                        userViewModel!!.setLoadingState(false)
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun copyTheText() {
        binding!!.get().card2FirstNameEditText.text = binding!!.get().firstNameEditText.text
        binding!!.get().card2LastNameEditText.text = binding!!.get().lastNameEditText.text
        binding!!.get().card2EmailEditText.text = binding!!.get().emailEditText.text
        binding!!.get().card2PhoneEditText.text = binding!!.get().phoneEditText.text
        binding!!.get().card2CompanyEditText.text = binding!!.get().companyEditText.text
        binding!!.get().card2Address1EditText.text = binding!!.get().address1EditText.text
        binding!!.get().card2Address2EditText.text = binding!!.get().address2EditText.text
        binding!!.get().card2CountryEditText.setText(binding!!.get().countryTextView.text)
        binding!!.get().card2CityEditText.setText(binding!!.get().cityTextView.text)
        binding!!.get().card2StateEditText.text = binding!!.get().stateEditText.text
        binding!!.get().card2PostalEditText.text = binding!!.get().postalEditText.text
    }

    private fun returnToOriginalValue() {
        if (userViewModel!!.user.billingFirstName != null && userViewModel!!.user.billingFirstName != "") {
            binding!!.get().card2FirstNameEditText.setText(userViewModel!!.user.billingFirstName)
        }
        if (userViewModel!!.user.billingLastName != null && userViewModel!!.user.billingLastName != "") {
            binding!!.get().card2LastNameEditText.setText(userViewModel!!.user.billingLastName)
        }
        if (userViewModel!!.user.billingEmail != null && userViewModel!!.user.billingEmail != "") {
            binding!!.get().card2EmailEditText.setText(userViewModel!!.user.billingEmail)
        }
        if (userViewModel!!.user.billingPhone != null && userViewModel!!.user.billingPhone != "") {
            binding!!.get().card2PhoneEditText.setText(userViewModel!!.user.billingPhone)
        }
        if (userViewModel!!.user.billingCompany != null && userViewModel!!.user.billingCompany != "") {
            binding!!.get().card2CompanyEditText.setText(userViewModel!!.user.billingCompany)
        }
        if (userViewModel!!.user.billingAddress1 != null && userViewModel!!.user.billingAddress1 != "") {
            binding!!.get().card2Address1EditText.setText(userViewModel!!.user.billingAddress1)
        }
        if (userViewModel!!.user.billingAddress2 != null && userViewModel!!.user.billingAddress2 != "") {
            binding!!.get().card2Address2EditText.setText(userViewModel!!.user.billingAddress2)
        }
        if (userViewModel!!.user.billingCountry != null && userViewModel!!.user.billingCountry != "") {
            binding!!.get().card2CountryEditText.setText(userViewModel!!.user.billingCountry)
        }
        if (userViewModel!!.user.billingState != null && userViewModel!!.user.billingState != "") {
            binding!!.get().card2StateEditText.setText(userViewModel!!.user.billingState)
        }
        if (userViewModel!!.user.billingCity != null && userViewModel!!.user.billingCity != "") {
            binding!!.get().card2CityEditText.setText(userViewModel!!.user.billingCity)
        }
        if (userViewModel!!.user.billingPostalCode != null && userViewModel!!.user.billingPostalCode != "") {
            binding!!.get().card2PostalEditText.setText(userViewModel!!.user.billingPostalCode)
        }
    }

    fun checkToUpdateProfile(): Boolean {
        return binding!!.get().firstNameEditText.text.toString() == userViewModel!!.user.shippingFirstName && binding!!.get().lastNameEditText.text.toString() == userViewModel!!.user.shippingLastName && binding!!.get().companyEditText.text.toString() == userViewModel!!.user.shippingCompany && binding!!.get().address1EditText.text.toString() == userViewModel!!.user.shippingAddress1 && binding!!.get().address2EditText.text.toString() == userViewModel!!.user.shippingAddress2 && binding!!.get().countryTextView.text.toString() == userViewModel!!.user.shippingCountry && binding!!.get().stateEditText.text.toString() == userViewModel!!.user.shippingState && binding!!.get().cityTextView.text.toString() == userViewModel!!.user.shippingCity && binding!!.get().postalEditText.text.toString() == userViewModel!!.user.shippingPostalCode && binding!!.get().emailEditText.text.toString() == userViewModel!!.user.shippingEmail && binding!!.get().phoneEditText.text.toString() == userViewModel!!.user.shippingPhone && binding!!.get().card2FirstNameEditText.text.toString() == userViewModel!!.user.billingFirstName && binding!!.get().card2LastNameEditText.text.toString() == userViewModel!!.user.billingLastName && binding!!.get().card2CompanyEditText.text.toString() == userViewModel!!.user.billingCompany && binding!!.get().card2Address1EditText.text.toString() == userViewModel!!.user.billingAddress1 && binding!!.get().card2Address2EditText.text.toString() == userViewModel!!.user.billingAddress2 && binding!!.get().card2CountryEditText.text.toString() == userViewModel!!.user.billingCountry && binding!!.get().card2StateEditText.text.toString() == userViewModel!!.user.billingState && binding!!.get().card2CityEditText.text.toString() == userViewModel!!.user.billingCity && binding!!.get().card2PostalEditText.text.toString() == userViewModel!!.user.billingPostalCode && binding!!.get().card2EmailEditText.text.toString() == userViewModel!!.user.billingEmail && binding!!.get().card2PhoneEditText.text.toString() == userViewModel!!.user.billingPhone
    }

    fun checkShippingAddressEditTextIsEmpty(): Boolean {
        return binding!!.get().address1EditText.text.toString().isEmpty()
    }

    fun checkBillingAddressEditTextIsEmpty(): Boolean {
        return binding!!.get().card2Address1EditText.text.toString().isEmpty()
    }

    fun checkUserEmailEditTextIsEmpty(): Boolean {
        return binding!!.get().userEmailEditText.text.toString().isEmpty()
    }

    fun checkUserShippingEmailAndBillingEmail() {
        if (binding!!.get().emailEditText.text.toString() == "") {
            binding!!.get().emailEditText.setText(binding!!.get().userEmailEditText.text.toString())
        }
        if (binding!!.get().card2EmailEditText.text.toString() == "") {
            binding!!.get().card2EmailEditText.setText(binding!!.get().userEmailEditText.text.toString())
        }
    }

    fun updateUserProfile() {
        checkUserShippingEmailAndBillingEmail()
        val user = User(userViewModel!!.user.userId,
                userViewModel!!.user.userIsSysAdmin,
                userViewModel!!.user.isShopAdmin,
                userViewModel!!.user.facebookId,
                userViewModel!!.user.googleId,
                userViewModel!!.user.userName,
                binding!!.get().userEmailEditText.text.toString(),
                binding!!.get().userPhoneEditText.text.toString(),
                userViewModel!!.user.userPassword,
                userViewModel!!.user.userAboutMe,
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
                binding!!.get().stateEditText.text.toString(),
                binding!!.get().cityTextView.text.toString(),
                binding!!.get().postalEditText.text.toString(),
                binding!!.get().emailEditText.text.toString(),
                binding!!.get().phoneEditText.text.toString(),
                userViewModel!!.user.deviceToken,
                userViewModel!!.user.code,
                userViewModel!!.user.verifyTypes,
                userViewModel!!.user.addedDateStr,
                Country(userViewModel!!.countryId,
                        binding!!.get().countryTextView.text.toString(),
                        null, null, null, null,
                        null, null, null),
                City(userViewModel!!.cityId,
                        binding!!.get().cityTextView.text.toString(),
                        userViewModel!!.countryId, null, null,
                        null, null, null, null, null)
        )

        // TODO
        if (activity != null && activity is CheckoutActivity) {
            (activity as CheckoutActivity?)!!.progressDialog!!.show()
        }
        userViewModel!!.setUpdateUserObj(user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_COUNTRY) {
            countryId = data!!.getStringExtra(Constants.COUNTRY_ID)
            binding!!.get().countryTextView.text = data.getStringExtra(Constants.COUNTRY_NAME)
            userViewModel!!.countryId = countryId
            cityId = ""
            userViewModel!!.cityId = cityId
            binding!!.get().cityTextView.text = ""
            if (activity != null) {
                (activity as CheckoutActivity?)!!.currentUser!!.city.id = userViewModel!!.cityId
            }
        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CITY) {
            cityId = data!!.getStringExtra(Constants.CITY_ID)
            binding!!.get().cityTextView.text = data.getStringExtra(Constants.CITY_NAME)
            userViewModel!!.cityId = cityId
            if (activity != null) {
                (activity as CheckoutActivity?)!!.currentUser!!.city.id = userViewModel!!.cityId
            }
        }
    }
}